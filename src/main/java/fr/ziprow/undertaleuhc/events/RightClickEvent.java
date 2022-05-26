package fr.ziprow.undertaleuhc.events;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

public class RightClickEvent implements Listener
{
	private UndertaleUHC main;
	private GameManager gameManager;
	
	public RightClickEvent(UndertaleUHC main, GameManager gameManager)
	{
		this.main = main;
		this.gameManager = gameManager;
	}
	
	int interros = 2;
	String interrogated;
	boolean interrogating = false;
	
	int auscultations = 2;
	String auscultated;
	boolean auscultating = false;
	
	@EventHandler
	public void onRightClickPlayer(PlayerInteractEntityEvent event)
	{
		if(gameManager.isState(GameState.WAITING) || gameManager.isState(GameState.EP1) || !(event.getRightClicked() instanceof Player)) return;
		
		Player p = event.getPlayer();
		Player c = (Player) event.getRightClicked();
		
		if(p.isBlocking() || !p.getGameMode().equals(GameMode.SURVIVAL) || !p.isSneaking()) return;
		
		if(GameManager.rolesMap.get(p.getUniqueId()).equals(Role.JUSTICE))
		{
			if(interros == 0 || interrogating || interrogated == c.getName()) return;
			
			Utils.sendMessage(p,
			"",
			"&cVous avez interrogé &e" + c.getName(),
			"&cVous obtiendrez la couleur de son âme dans 5 minutes !");
			p.setMaxHealth(p.getMaxHealth() - 2);
			
			interros--;
			interrogating = true;
			interrogated = c.getName();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.main, () -> {
				if(!GameManager.rolesMap.containsKey(c.getUniqueId())) return;
				Utils.sendMessage(p,
				"",
				"&cL'âme de &e" + c.getName() + "&c est " + ChatColorToString(GameManager.rolesMap.get(c.getUniqueId()).getColor()) + "&c !");
				Utils.warnPlayer(c, "la couleur de votre ame a été compromise");
				interrogating = false;
			}, 20 * 60 * 5);
		}
		
		if(GameManager.rolesMap.get(p.getUniqueId()).equals(Role.ALPHYS))
		{
			if(auscultations == 0 || auscultating || auscultated == c.getName()) return;
			
			Utils.sendMessage(p,
			"",
			"&cVous avez ausculté &e" + c.getName(),
			"&cVous obtiendrez la couleur de son âme dans 5 minutes !");
			p.setMaxHealth(p.getMaxHealth() - 2);
			
			auscultations--;
			auscultating = true;
			auscultated = c.getName();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)this.main, () -> {
				if(!GameManager.rolesMap.containsKey(c.getUniqueId())) return;
				Utils.sendMessage(p,
				"",
				"&cL'âme de &e" + c.getName() + "&c est " + ChatColorToString(GameManager.rolesMap.get(c.getUniqueId()).getColor()) + "&c !");
				Utils.warnPlayer(c, "la couleur de votre ame a été compromise");
				auscultating = false;
			}, 20 * 60 * 5);
		}
	}
	
	private String ChatColorToString(ChatColor c)
	{
		switch(c)
		{
			case AQUA:
				return c + "cyan"; 
			case BLUE:
				return c + "bleue";
			case DARK_GRAY:
				return c + "noire";
			case GOLD:
				return c + "orange";
			case GREEN:
				return c + "verte";
			case LIGHT_PURPLE:
				return c + "magenta";
			case RED:
				return c + "rouge";
			case WHITE:
				return c + "blanche";
			case YELLOW:
				return c + "jaune";
			default:
				return "";
		}
	}
}
