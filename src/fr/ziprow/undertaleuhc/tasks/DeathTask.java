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
import org.bukkit.inventory.PlayerInventory;
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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class DeathTask extends BukkitRunnable
{
	private GameManager gameManager;
	private Player player;
	private Location loc;
	private Inventory inv;
	
	public int timer = 10;
	public static boolean revive = true;
	public static boolean hasRevive = true;
	public static boolean hasRespawn = true;
	public Player kindness;

	public DeathTask(GameManager gameManager, Player player, Location loc, PlayerInventory inv)
	{
		this.gameManager = gameManager;
		this.player = player;
		this.loc = loc;
		this.inv = inv;
	}
	
	@Override
	public void run()
	{	
		if(timer == 10)
		{
			if(hasRespawn)
			{
				// Respawn pers�v�rence
				for(UUID u : GameManager.rolesMap.keySet())
				{
					Player p = Bukkit.getPlayer(u);
					if(p != null && GameManager.rolesMap.get(p.getUniqueId()).equals(Role.PERSEVERANCE) && p.getKiller() != null && (GameManager.rolesMap.get(p.getKiller().getUniqueId()).getColor() != ChatColor.WHITE || GameManager.rolesMap.get(p.getKiller().getUniqueId()).equals(Role.LOVE)))
					{
						Utils.warnPlayer(p, "Vous avez une deuxi�me vie");
						
						player.setGameMode(GameMode.SURVIVAL);
						// On t�l�porte le joueur de fa�on al�atoire
						Random random = new Random();
						int x = random.nextInt(200) - 100;
						int z = random.nextInt(200) - 100;
						Location loc = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z) + 2, z);
						loc.getChunk().load(true);
						player.teleport(loc);
						
						hasRespawn = false;
						
						cancel();
						return;
					}
				}
			}
			
			Utils.warnPlayer(player, "Ne quittez pas, vous pourriez ressusciter");
			
			revive = false;
			
			for(UUID u : GameManager.playing)
			{
				Player p = Bukkit.getPlayer(u);
				if(p != null && GameManager.rolesMap.get(p.getUniqueId()).equals(Role.KINDNESS)) kindness = p;
			}
			
			if(!player.equals(kindness) && hasRevive)
			{
				// envoie un message � gentillesse pour revive
				TextComponent reviveMsg = new TextComponent(Utils.color("&o" + player.getName() + " &r&6vient de mourir, voulez vous le ressusciter ?"));
				reviveMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&2Oui")).create()));
				reviveMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/revive"));
				if(kindness != null)
				{
					kindness.sendMessage("");
					kindness.spigot().sendMessage(reviveMsg);
				}
			}
		}
		
		if(revive && kindness != null)
		{
			revive = false;
			
			if(hasRevive)
			{
				if(!player.equals(kindness))
				{
					hasRevive = false;
					
					Utils.informPlayer(kindness, "Vous avez ressuscit� ce joueur");
					player.setGameMode(GameMode.SURVIVAL);
					// On t�l�porte le joueur de fa�on al�atoire
					Random random = new Random();
					int x = random.nextInt(200) - 100;
					int z = random.nextInt(200) - 100;
					Location loc = new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt(x, z) + 2, z);
					loc.getChunk().load(true);
					player.teleport(loc);
					
					cancel();
					return;
				}
				else Utils.warnPlayer(kindness, "Vous ne pouvez pas vous ressusciter vous m�me");
			}
			else Utils.warnPlayer(kindness, "Vous avez d�ja ressuscit� un joueur");
		}
		
		if(timer == 0)
		{
			revive = true;
			
			kill(player, inv, loc);
		}
		timer--;
	}
	
	private void kill(Player p, Inventory inv, Location loc)
	{
		Role role = Utils.getRole(p);
		
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
					"&c" + Utils.line + "&c\u2764" + Utils.line,
					"&2" + p.getName() + " est mort !",
					"&2Il �tait &o" + role.getName() + "&r&2.",
					"&c" + Utils.line + "&c\u2764" + Utils.line);
			
			// On enl�ve force � Frisk si le joueur �pargn� est mort
			if(p.equals(Bukkit.getPlayer(GameManager.spared)))
			{
				Player frisk = Utils.getPlayer(Role.FRISK);
				
				if(frisk != null)
				{
					frisk.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					Utils.informPlayer(frisk, "Vous avez perdu Force car le joueur �pargn� est mort");
				}
			}
			
			// On enl�ve r�sistance � Toriel si le joueur sympathis� est mort
			if(p.equals(Bukkit.getPlayer(GameManager.sympathized)))
			{
				Player toriel = Utils.getPlayer(Role.TORIEL);
				
				if(toriel != null)
				{
					toriel.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					Utils.informPlayer(toriel, "Vous avez perdu R�sistance car le joueur sympatis� est mort");
				}
			}
			
			// Enlever les effets Amour/Haine
			if(Utils.getRole(p).equals(Role.LOVE))
			{
				Player hatred = Utils.getPlayer(Role.HATRED);
				
				if(hatred != null)
				{
					hatred.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					Utils.informPlayer(hatred, "Vous avez perdu Force car Amour est mort");
				}
			}
			else if(Utils.getRole(p).equals(Role.HATRED))
			{
				Player love = Utils.getPlayer(Role.LOVE);
				
				if(love != null)
				{
					love.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					Utils.informPlayer(love, "Vous avez perdu R�sistance car Haine est morte");
				}
			}
			
			// Si flowey tue d�termination >> se transforme en Photoshop Flowey
			if(Utils.getRole(p).getColor().equals(ChatColor.RED))
			{
				Player flowey = Utils.getPlayer(Role.FLOWEY);
				
				if(flowey != null)
				{
					GameManager.rolesMap.replace(flowey.getUniqueId(), Role.PHOTOSHOP_FLOWEY);
					Utils.informPlayer(flowey, "Vous avez tu� D�termination donc vous vous transformez en Photoshop Flowey");
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
						Utils.informPlayer(bravery, "Vous avez tu� un monstre donc vous avez gagn� un coeur permanent");
					}
				}
			}
			
			// Change Sans quand Papyrus meurt
			if(Utils.getRole(p).equals(Role.PAPYRUS))
			{
				Player sans = Utils.getPlayer(Role.SANS);
				
				if(sans != null)
				{
					GameManager.rolesMap.replace(sans.getUniqueId(), Role.SANS_GLOWING_EYE);
					Utils.informPlayer(sans, "Papyrus est mort donc vous avez l'Oeil Brillant");
					Utils.info(sans);
					Utils.getRole(sans).giveStuff(sans);
				}
			}
			
			// On l'enl�ve des joueurs
			GameManager.playing.remove(p.getUniqueId());
			GameManager.ally = null;
			
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
			String line = "&d" + Utils.line + "&d\u2764" + Utils.line;
			
			if(human == 0 && monsters == 0 && neutral == 0) // win personne
			{
				Utils.broadcast(
				line,
				"&cPersonne n'a gagn� !",
				line);
				end();
			}
			else if(monsters == 0 && neutral == 0) // win humains
			{
				Utils.broadcast(
				line,
				"&cLes humains ont gagn� !",
				line);
				end();
			}
			else if(human == 0 && neutral == 0) // win monsters
			{
				Utils.broadcast(
				line,
				"&cLes monstres ont gagn� !",
				line);
				end();
			}
			else if(human == 0 && monsters == 0 && neutral == 2) // win neutral
			{
				Player integrity = Utils.getPlayer(Role.INTEGRITY);
				if(integrity != null)
				{
					Utils.broadcast(
					line,
					"&c" + integrity.getName() + " et " + GameManager.ally + " ont gagn� !",
					line);
					end();
				}
			}
			else if(human == 0 && monsters == 0 && neutral == 1)
			{
				Utils.broadcast(
				line,
				"&c" + Bukkit.getPlayer(GameManager.playing.get(0) + " a gagn� !"),
				line);
				end();
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
