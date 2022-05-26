package fr.ziprow.undertaleuhc.events;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.tasks.DeathTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.PlayerInventory;

public class DeathEvent implements Listener
{
	private UndertaleUHC main;
	private GameManager gameManager;
	public Player player;
	public Location loc;
	public PlayerInventory inv;

	public DeathEvent(UndertaleUHC main, GameManager gameManager) {this.main = main; this.gameManager = gameManager;}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event)
	{
		if(gameManager.isState(GameState.WAITING) || !(event.getEntity() instanceof Player) || !GameManager.playing.contains(event.getEntity().getUniqueId())) return;
		
		player = (Player)event.getEntity();
		loc = player.getLocation();
		inv = player.getInventory();
		
		event.setDeathMessage("");
		player.setBedSpawnLocation(loc, true);
		Bukkit.getScheduler().runTaskLater(main, new Runnable()
		{
			@Override
			public void run()
			{
				player.setGameMode(GameMode.SPECTATOR);
			}
		}, 10);
		player.getWorld().playSound(loc, Sound.VILLAGER_DEATH, 1.0f, 1.0f);
		
		DeathTask task = new DeathTask(gameManager, player, loc, inv);
		task.runTaskTimer(main, 0, 20);
	}
}