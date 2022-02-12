package fr.ziprow.undertaleuhc.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;

public class StartTask extends BukkitRunnable
{
	private GameManager gameManager;
	private int timer = 2;
	private int p = Bukkit.getOnlinePlayers().size();
	
	public StartTask(GameManager gameManager)
	{
		this.gameManager = gameManager;
	}

	@Override
	public void run()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			p.setLevel(timer);
			p.getWorld().playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
		}
		
		if(p > Bukkit.getOnlinePlayers().size())
		{
			cancel();
			Utils.broadcast("&eUn joueur a quitté, en attente!");
			gameManager.setState(GameState.WAITING);
		}
		
		if(timer <= 0)
		{
			cancel();
			for(Player p : Bukkit.getOnlinePlayers()) p.setLevel(0);
			gameManager.setState(GameState.PLAYING);
		}
		timer--;
	}
}