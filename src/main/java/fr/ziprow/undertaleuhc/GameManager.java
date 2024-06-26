package fr.ziprow.undertaleuhc;

import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.events.*;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.tasks.EpisodesTask;
import fr.ziprow.undertaleuhc.tasks.ShareHealth;
import fr.ziprow.undertaleuhc.tasks.StartTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameManager
{

	private GameState gameState = GameState.WAITING;
	public static Map<UUID, Role> rolesMap = new HashMap<>();
	public static List<UUID> playing = new ArrayList<>();
	public static UUID ally = null;
	public static UUID spared;
	public static UUID sympathized;
	private Player patience;
	
	public GameManager()
	{
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvent(this), UndertaleUHC.getInstance());
		initEvents();
	}
	
	public void setState(GameState gameState)
	{
		this.gameState = gameState;
		
		World world = Bukkit.getWorld("world");
		WorldBorder border = world.getWorldBorder();
		
		switch(gameState)
		{
			case WAITING:
				
				setRules(world);
				
				break;
				
			case STARTING:
				
				regEvents();
				
				// Timer de démarrage
				StartTask startTask = new StartTask(this);
				startTask.runTaskTimer(UndertaleUHC.getInstance(), 0, 20);
				
				break;
				
			case PLAYING:
				
				giveRoles();
				chooseAlly();
				setDefaultBorder(border);
				setWorldRules(world);
				setPlayers();
				
				EpisodesTask epTask = new EpisodesTask(this);
				epTask.runTaskTimer(UndertaleUHC.getInstance(), 0, 20);
				
				break;
				
			case EP1:
				
				for(Player p : Bukkit.getOnlinePlayers())
					p.setGameMode(GameMode.SURVIVAL);
				
				break;
				
			case EP2:
				
				// dévoilement des roles
				for(UUID u : playing)
				{
					Player p = Bukkit.getPlayer(u);
					Role role = rolesMap.get(u);
					
					Utils.info(p);
					role.giveStuff(p);
				}
				
				// Vie partagee Integrite/ally
				if(ally != null)
				{
					ShareHealth shareTask = new ShareHealth();
					shareTask.runTaskTimer(UndertaleUHC.getInstance(), 0, 4);
				}
				
				// Vie de Bravoure
				Player bravery = Utils.getPlayer(Role.BRAVERY);
				if(bravery != null) bravery.setMaxHealth(bravery.getMaxHealth() - 4);
				
				// Msg de choix pour Determination
				Player determination = Utils.getPlayer(Role.DETERMINATION);
				if(determination != null)
				{
					determination.sendMessage("");
					TextComponent charaMsg = new TextComponent(Utils.color("&6Cliquez ici pour choisir &2CHARA&6 : Vous devrez gagner seul avec tranchant IV !"));
					charaMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&cCHARA")).create()));
					charaMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/choosechara"));
					determination.spigot().sendMessage(charaMsg);
					determination.sendMessage("");
					TextComponent friskMsg = new TextComponent(Utils.color("&6Cliquez ici pour choisir &2FRISK&6 : Vous devrez gagner avec les humains !"));
					friskMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&cFRISK")).create()));
					friskMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/choosefrisk"));
					determination.spigot().sendMessage(friskMsg);
					
					Bukkit.getScheduler().runTaskLater(UndertaleUHC.getInstance(), () ->
					{
						if(!Utils.getRole(determination).equals(Role.DETERMINATION)) return;
						Utils.inform(determination, "Vous avez pris trop longtemps pour choisir vétre réle, il a été choisi aléatoirement");
						if(new Random().nextBoolean()) determination.performCommand("choosechara");
						else determination.performCommand("choosefrisk");
					}, 20 * 30);
				}
				
				// Envoyer msg à Toriel
				Player toriel = Utils.getPlayer(Role.TORIEL);
				if(toriel != null)
				{
					toriel.sendMessage("");
					for(UUID u : GameManager.playing)
					{
						Player player = Bukkit.getPlayer(u);
						if(!GameManager.rolesMap.get(u).equals(Role.TORIEL))
						{
							TextComponent sympaMsg = new TextComponent(Utils.color("&6Sympathiser avec &2" + player.getName()));
							sympaMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&2" + player.getName())).create()));
							sympaMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sympa " + player.getName()));
							toriel.spigot().sendMessage(sympaMsg);
						}
					}
					
					Bukkit.getScheduler().runTaskLater(UndertaleUHC.getInstance(), () ->
					{
						if(GameManager.sympathized != null) return;
						Utils.inform(toriel, "Vous avez pris trop longtemps pour sympathiser avec un joueur, il a été choisi aléatoirement");
						Player sympathized;
						do sympathized = Bukkit.getPlayer(GameManager.playing.get(new Random().nextInt(GameManager.playing.size())));
						while(sympathized.equals(toriel));
						toriel.performCommand("sympa " + sympathized.getName());
					}, 20 * 30);
				}
				
				break;
				
			case EP3:
				
				world.setPVP(true);
				Utils.broadcast("&eLe &6PvP &ea été activé !");
				
				break;
				
			case EP4:
				
				// meetup
				Utils.broadcast("&eC'est le &6Meetup &e!");
				
				// Premier coeur pour Patience
				patience = Utils.getPlayer(Role.PATIENCE);
				if(patience != null) patience.setMaxHealth(patience.getMaxHealth() + 2);
				
				break;
				
			case EP5:
				
				border.setSize(200, EpisodesTask.defaultTimer * 4L);
				Utils.broadcast("&eLa &6bordure &ecommence à se resserrer !");
				
				break;
				
			case EP7:
				
				// Deuxième coeur pour Patience
				if(patience != null) patience.setMaxHealth(patience.getMaxHealth() + 2);
				
				break;
				
			case END:
				
				unregEvents();
				
				// fin
				
				break;

			default:
				break;
		}
	}
	
	public boolean isState(GameState... gameStates)
	{
		for(GameState gameState : gameStates) if(this.gameState == gameState) return true;
		return false;
	}


	
	private void setRules(World world)
	{
		world.setDifficulty(Difficulty.PEACEFUL);
		world.setGameRuleValue("doFireTick", 			"false"	);
		world.setGameRuleValue("mobGriefing", 			"true"	);
		world.setGameRuleValue("keepInventory", 		"true"	);
		world.setGameRuleValue("doMobSpawning", 		"true"	);
		world.setGameRuleValue("doMobLoot", 			"true"	);
		world.setGameRuleValue("doTileDrops", 			"true"	);
		world.setGameRuleValue("commandBlockOutput", 	"false"	);
		world.setGameRuleValue("naturalRegeneration", 	"true"	);
		world.setGameRuleValue("doDaylightCycle", 		"false"	);
		world.setGameRuleValue("doWeatherCycle", 		"false"	);
		world.setGameRuleValue("logAdminCommands", 		"false"	);
		world.setGameRuleValue("showDeathMessages", 	"false"	);
		world.setGameRuleValue("sendCommandFeedback", 	"false"	);
		world.setGameRuleValue("doEntityDrops", 		"false"	);
		world.setTime(1000);
		world.setPVP(false);
	}
	
	private void giveRoles()
	{
		ArrayList<Role> roles = new ArrayList<>();
		ArrayList<Role> rolesHuman = new ArrayList<>();
		ArrayList<Role> rolesMonster = new ArrayList<>();
		ArrayList<Role> rolesNeutral = new ArrayList<>();
		rolesMonster.add(Role.ALPHYS);
		rolesMonster.add(Role.ASGORE);
		rolesMonster.add(Role.METTATON);
		rolesMonster.add(Role.NAPSTABLOOK);
		rolesMonster.add(Role.PAPYRUS);
		rolesMonster.add(Role.SANS);
		rolesMonster.add(Role.TORIEL);
		rolesMonster.add(Role.UNDYNE);
		rolesHuman.add(Role.BRAVERY);
		rolesHuman.add(Role.JUSTICE);
		rolesHuman.add(Role.KINDNESS);
		rolesHuman.add(Role.LOVE);
		rolesHuman.add(Role.PATIENCE);
		rolesHuman.add(Role.PERSEVERANCE);
		rolesHuman.add(Role.INTEGRITY);
		rolesNeutral.add(Role.FLOWEY);
		rolesNeutral.add(Role.DETERMINATION);
		rolesNeutral.add(Role.HATRED);
		Collections.shuffle(rolesHuman);
		Collections.shuffle(rolesMonster);
		Collections.shuffle(rolesNeutral);

		roles.addAll(rolesMonster);
		roles.addAll(rolesHuman);
		roles.addAll(rolesNeutral);
		
		for(Player p : Bukkit.getOnlinePlayers()) playing.add(p.getUniqueId());
		
		Collections.shuffle(playing);
		
		for(int i = 0; i < playing.size(); i++) rolesMap.put(playing.get(i), roles.get(i));
		//GameManager.rolesMap.replace(Bukkit.getPlayer("Z1proW").getUniqueId(), Role.SANS);
		//GameManager.rolesMap.replace(Bukkit.getPlayer("Sudemax").getUniqueId(), Role.PAPYRUS);
		// integrity ally
		if(Utils.getPlayer(Role.INTEGRITY) != null)
		{
			for(UUID u : GameManager.playing)
			{
				Player player = Bukkit.getPlayer(u);
				if(!GameManager.rolesMap.get(u).equals(Role.INTEGRITY))
				{
					GameManager.ally = player.getUniqueId();
				}
			}
		}
	}
	
	private void chooseAlly()
	{
		if(rolesMap.size() > 1)
		{
			Random random = new Random();
			do ally = playing.get(random.nextInt(playing.size()));
			while(rolesMap.get(ally).equals(Role.INTEGRITY));
		}
	}
	
	private void setDefaultBorder(WorldBorder border)
	{
		border.setCenter(0, 0);
		border.setSize(1000);
		border.setDamageBuffer(0);
		border.setDamageAmount(0.02);
	}
	
	private void setWorldRules(World world)
	{
		world.setPVP(false);
		world.setDifficulty(Difficulty.EASY);
		world.setGameRuleValue("naturalRegeneration", "false"	);
		world.setTime(1000);
		world.setGameRuleValue("doDayLightCycle", "true"	);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
	}
	
	private void setPlayers()
	{
		Bukkit.broadcastMessage("");
		for(Player p : Bukkit.getServer().getOnlinePlayers())
		{
			// On clear
			Utils.clear(p);
			p.setMaxHealth(20);
			Utils.heal(p);
			
			// On donne du steak
			p.getInventory().setItem(8, new ItemStack(Material.COOKED_BEEF, 64));
			p.updateInventory();
			
			// On téléporte les joueurs aléatoirement
			Utils.randomTeleport(p, 1000, 40);
			Bukkit.broadcastMessage(Utils.color("&6" + p.getName() + " &ea été teleporté !"));
		}
	}
	
	private Listener[] events;
	
	private void initEvents()
	{
		events = new Listener[] {new ScenariosEvent(this), new DeathEvent(this), new ItemEvent(), new RightClickEvent(this), new RemoveArmorEvent(this)};
	}
	
	private void unregEvents()
	{
		for(Listener e : events)
			HandlerList.unregisterAll(e);
	}
	
	private void regEvents()
	{
		for(Listener e : events)
			Bukkit.getServer().getPluginManager().registerEvents(e, UndertaleUHC.getInstance());
	}

}