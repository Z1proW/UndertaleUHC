package fr.ziprow.undertaleuhc.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;

public class PlayerEvent implements Listener
{
	private GameManager gameManager;
	
	public PlayerEvent(GameManager gameManager) {this.gameManager = gameManager;}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		
		/*// Installation du Pack
		Utils.warnPlayer(p, "Installation du pack de texture");
		p.setResourcePack("https://docs.google.com/uc?export=download&id=1rOai9FIVUSp36o_4z0wwqKK9ZMSFwfSn");
		*/
		// Rejoint pendant l'attente
		if(gameManager.isState(GameState.WAITING))
		{
			p.teleport(new Location(p.getWorld(), 0, 200, 0));
			p.setGameMode(GameMode.ADVENTURE);
			Bukkit.broadcastMessage("");
			event.setJoinMessage(Utils.color("&8[&c+&8] &r&o" + p.getName() + " &8< &c" + Bukkit.getOnlinePlayers().size() + "&8/&c" + Bukkit.getMaxPlayers() + " &8>"));
			Utils.clear(p);
			startBoard(p);
			return;
		}
		
		// Rejoint � nouveau
		for(UUID u : GameManager.playing)
		{
			if(p.getUniqueId().equals(u))
			{
				event.setJoinMessage(Utils.color("&8[&c+&8] &r&o" + p.getName() + " &rest revenu dans la partie !"));
				return;
			}
		}
		
		// Rejoint pendant la partie
		event.setJoinMessage("");
		p.setGameMode(GameMode.SPECTATOR);
		Utils.informPlayer(p, "La partie a d�ja commenc�");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		
		if(gameManager.isState(GameState.WAITING))
		{
			Bukkit.broadcastMessage("");
			event.setQuitMessage(Utils.color("&8[&c-&8] &r&o" + p.getName() + " &8< &c" + Bukkit.getOnlinePlayers().size() + "&8/&c" + Bukkit.getMaxPlayers() + " &8>"));
			return;
		}
		event.setQuitMessage(Utils.color("&8[&c-&8] &r&o" + p.getName() + " &ra perdu connexion !"));
	}
	
	private void startBoard(Player p)
	{
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("UNDERTALEUHC", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(Utils.color("&fUNDERTALE UHC"));
		Score l7 = obj.getScore	(""); l7.setScore(7);
		Score l0 = obj.getScore	(Utils.color("&9en attente...")); l0.setScore(0);
		p.setScoreboard(board);
	}
	
	@EventHandler
	private void DisableSomeCommands(PlayerCommandPreprocessEvent event)
	{
		String input = event.getMessage().split(" ")[0];
		String[] cmds = {"/me", "/minecraft:me", "/tell", "/minecraft:tell", "/say", "/minecraft:say", "/bukkit", "/?", "/bukkit:?", "/help", "/bukkit:help", "/about", "/bukkit:about", "/ver", "/bukkit:ver", "/version", "bukkit:version", "/pl", "/bukkit:pl", "/plugins", "/bukkit:plugins", "/icanhasbukkit"};
		for(String cmd : cmds) if(input.equalsIgnoreCase(cmd)) event.setCancelled(true);
	}
	
	@EventHandler
	public void noRain(WeatherChangeEvent event) {event.setCancelled(event.toWeatherState());}
}
