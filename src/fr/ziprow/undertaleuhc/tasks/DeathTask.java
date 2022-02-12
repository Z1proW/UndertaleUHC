package fr.ziprow.undertaleuhc.tasks;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.enums.Team;
import fr.ziprow.undertaleuhc.events.DeathEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathTask extends BukkitRunnable
{
	private DeathEvent deathListener;
	private GameManager gameManager;
	
	public int timer = 10;
	public static boolean revive = true;
	public static boolean hasRevive = true;
	public static boolean hasRespawn = true;
	public Player kindness;
	public Player integrity;

	public DeathTask(DeathEvent deathListener, GameManager gameManager)
	{
		this.deathListener = deathListener;
		this.gameManager = gameManager;
	}
	
	@Override
	public void run()
	{	
		if(timer == 10)
		{
			if(hasRespawn)
			{
				// Respawn persévérence
				for(UUID u : GameManager.rolesMap.keySet())
				{
					Player p = Bukkit.getPlayer(u);
					if(p != null && GameManager.rolesMap.get(p.getUniqueId()).equals(Role.PERSEVERANCE) && p.getKiller() != null && (GameManager.rolesMap.get(p.getKiller().getUniqueId()).getColor() != ChatColor.WHITE || GameManager.rolesMap.get(p.getKiller().getUniqueId()).equals(Role.LOVE)))
					{
						Utils.warnPlayer(p, "Vous avez une deuxième vie");
						
						deathListener.player.setGameMode(GameMode.SURVIVAL);
						// On téléporte le joueur de façon aléatoire
						Random random = new Random();
						int x = random.nextInt(200) - 100;
						int z = random.nextInt(200) - 100;
						Location loc = new Location(deathListener.player.getWorld(), x, deathListener.player.getWorld().getHighestBlockYAt(x, z) + 2, z);
						loc.getChunk().load(true);
						deathListener.player.teleport(loc);
						
						hasRespawn = false;
						
						cancel();
						return;
					}
				}
			}
			
			Utils.warnPlayer(deathListener.player, "Ne quittez pas, vous pourriez ressusciter");
			
			revive = false;
			
			for(UUID u : GameManager.playing)
			{
				Player p = Bukkit.getPlayer(u);
				if(GameManager.rolesMap.get(p.getUniqueId()).equals(Role.KINDNESS)) kindness = p;
			}
			
			if(!deathListener.player.equals(kindness) && hasRevive)
			{
				// envoie un message à gentillesse pour revive
				TextComponent reviveMsg = new TextComponent(Utils.color("&o" + deathListener.player.getName() + " &r&6vient de mourir, voulez vous le ressusciter ?"));
				reviveMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&2Oui")).create()));
				reviveMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/revive"));
				if(kindness != null)
				{
					kindness.sendMessage("");
					kindness.spigot().sendMessage(reviveMsg);
				}
			}
		}
		
		if(revive)
		{
			revive = false;
			
			if(hasRevive)
			{
				if(!deathListener.player.equals(kindness))
				{
					hasRevive = false;
					
					Utils.informPlayer(kindness, "Vous avez ressuscité ce joueur");
					deathListener.player.setGameMode(GameMode.SURVIVAL);
					// On téléporte le joueur de façon aléatoire
					Random random = new Random();
					int x = random.nextInt(200) - 100;
					int z = random.nextInt(200) - 100;
					Location loc = new Location(deathListener.player.getWorld(), x, deathListener.player.getWorld().getHighestBlockYAt(x, z) + 2, z);
					loc.getChunk().load(true);
					deathListener.player.teleport(loc);
					
					cancel();
					return;
				}
				else Utils.warnPlayer(kindness, "Vous ne pouvez pas vous ressusciter vous même");
			}
			else Utils.warnPlayer(kindness, "Vous avez déja ressuscité un joueur");
		}
		
		if(timer == 0)
		{
			revive = true;
			
			kill(deathListener.player, deathListener.inv, deathListener.loc);
		}
		timer--;
	}
	
	private void kill(Player p, Inventory inv, Location loc)
	{
		Role role = GameManager.rolesMap.get(p.getUniqueId());
		
		if(role != null)
		{
			// Drop du stuff
			for(ItemStack item : inv)
				if(item != null)
					p.getWorld().dropItemNaturally(loc, item);
			
			p.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLDEN_APPLE, 1)); // drop gapple
			if(role.getSoulItem() != null)
				p.getWorld().dropItemNaturally(loc, role.getSoulItem()); // drop soul
			p.setGameMode(GameMode.SPECTATOR);
			p.getInventory().clear();
			
			// Envoyer le son de mort aux joueurs
			for(Player pl : Bukkit.getOnlinePlayers())
				pl.getWorld().playSound(pl.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
			
			// On affiche le message de mort
			Utils.broadcast(
			"&c&m-------&c\u2764&m-------",
			"&2" + p.getName() + " est mort !",
			"&2Il était &o" + role.getName() + "&r&2.",
			"&c&m-------&c\u2764&m-------");
			
			for(UUID u : GameManager.playing)
			{
				Player pl = Bukkit.getPlayer(u);
				if(GameManager.rolesMap.get(pl.getUniqueId()).equals(Role.INTEGRITY)) integrity = pl;
			}
			
			// On enlève force à Frisk si le joueur épargné est mort
			if(p.equals(Bukkit.getPlayer(GameManager.spared)))
			{
				Player frisk = Utils.getPlayer(Role.FRISK);
				
				if(frisk != null)
				{
					frisk.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					Utils.informPlayer(frisk, "Vous avez perdu Force car le joueur épargné est mort");
				}
			}
			
			// On enlève résistance à Toriel si le joueur sympathisé est mort
			if(p.equals(Bukkit.getPlayer(GameManager.sympathized)))
			{
				Player toriel = Utils.getPlayer(Role.TORIEL);
				
				if(toriel != null)
				{
					toriel.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					Utils.informPlayer(toriel, "Vous avez perdu Résistance car le joueur sympatisé est mort");
				}
			}
			
			// Enlever les effets Amour/Haine
			if(GameManager.rolesMap.get(p.getUniqueId()).equals(Role.LOVE))
			{
				Player hatred = Utils.getPlayer(Role.HATRED);
				
				if(hatred != null)
				{
					hatred.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					Utils.informPlayer(hatred, "Vous avez perdu Force car Amour est mort");
				}
			}
			else if(GameManager.rolesMap.get(p.getUniqueId()).equals(Role.HATRED))
			{
				Player love = Utils.getPlayer(Role.LOVE);
				
				if(love != null)
				{
					love.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					Utils.informPlayer(love, "Vous avez perdu Résistance car Haine est morte");
				}
			}
			
			// Si flowey tue détermination >> se transforme en Photoshop Flowey
			if(GameManager.rolesMap.get(p.getUniqueId()).getColor().equals(ChatColor.RED))
			{
				Player flowey = Utils.getPlayer(Role.FLOWEY);
				
				if(flowey != null)
				{
					GameManager.rolesMap.replace(flowey.getUniqueId(), Role.PHOTOSHOP_FLOWEY);
					Utils.informPlayer(flowey, "Vous avez tué Détermination donc vous vous transformez en Photoshop Flowey");
					Utils.info(flowey);
				}
			}
			
			// Bravoure gagne un coeur si il tue un monstre
			if(p.getKiller() != null)
			{
				if(GameManager.rolesMap.get(p.getUniqueId()).getTeam().equals(Team.MONSTER)
				&& GameManager.rolesMap.get(p.getKiller().getUniqueId()).equals(Role.BRAVERY))
				{
					Player bravery = Utils.getPlayer(Role.BRAVERY);
					
					if(bravery != null)
					{
						bravery.setMaxHealth(bravery.getMaxHealth() + 2);
						Utils.informPlayer(bravery, "Vous avez tué un monstre donc vous avez gagné un coeur permanent");
					}
				}
			}
			
			// On l'enlève des joueurs
			GameManager.playing.remove(p.getUniqueId());
			
			int human = 0;
			int monsters = 0;
			int neutral = 0;
			for(UUID u : GameManager.playing)
			{
				Player pl = Bukkit.getPlayer(u);
				if(GameManager.rolesMap.get(pl.getUniqueId()).getTeam().equals(Team.HUMAN)) human++;
				else if(GameManager.rolesMap.get(pl.getUniqueId()).getTeam().equals(Team.MONSTER)) monsters++;
				else if(GameManager.rolesMap.get(pl.getUniqueId()).getTeam().equals(Team.NEUTRAL)) neutral++;
			}
			
			if(monsters == 2) // Si il ne reste que 2 monstres Flowey devient Asriel
			{
				Player flowey = Utils.getPlayer(Role.FLOWEY);
				
				if(flowey != null)
				{
					GameManager.rolesMap.replace(flowey.getUniqueId(), Role.ASRIEL);
					Utils.informPlayer(flowey, "Il ne reste que 2 monstres donc vous vous transformez en Asriel");
					Utils.info(flowey);
				}
			}
			else if(monsters == 1) // Si il ne reste que 1 monstre Asriel devient Ange de la mort
			{
				Player asriel = Utils.getPlayer(Role.ASRIEL);
				
				if(asriel != null)
				{
					GameManager.rolesMap.replace(asriel.getUniqueId(), Role.ANGEL_OF_DEATH);
					Utils.info(asriel);
					Role.ANGEL_OF_DEATH.giveStuff(asriel);
				}
			}
			
			// Fin de game
			if(monsters == 0 && neutral == 0) // win personne
			{
				Utils.broadcast(
				"&d&m-------&d\u2764&m-------",
				"&cPersonne n'a gagné !",
				"&d&m-------&d\u2764&m-------");
				end();
			}
			else if(monsters == 0 && neutral == 0) // win humains
			{
				Utils.broadcast(
				"&d&m-------&d\u2764&m-------",
				"&cLes humains ont gagné !",
				"&d&m-------&d\u2764&m-------");
				end();
			}
			else if(human == 0 && neutral == 0) // win monsters
			{
				Utils.broadcast(
				"&d&m-------&d\u2764&m-------",
				"&cLes monstres ont gagné !",
				"&d&m-------&d\u2764&m-------");
				end();
			}
			else if(human == 0 && monsters == 0 && neutral == 2) // win neutral
			{
				Player integrity = Utils.getPlayer(Role.INTEGRITY);
				if(integrity != null)
				{
					Utils.broadcast(
					"&d&m-------&d\u2764&m-------",
					"&c" + integrity.getName() + " et " + GameManager.ally + " ont gagné !",
					"&d&m-------&d\u2764&m-------");
					end();
				}
				else if(neutral == 1)
				{
					Utils.broadcast(
					"&d&m-------&d\u2764&m-------",
					"&c" + Bukkit.getPlayer(GameManager.playing.get(0) + " a gagné !"),
					"&d&m-------&d\u2764&m-------");
					end();
				}
			}
		}
		cancel();
		return;
	}
	
	private void end()
	{
		gameManager.setState(GameState.END);
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("UNDERTALEUHC", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(Utils.color("&fUNDERTALE UHC"));
		for(Player p : Bukkit.getOnlinePlayers()) p.setScoreboard(board);
	}
}
