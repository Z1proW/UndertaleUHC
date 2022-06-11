package fr.ziprow.undertaleuhc.tasks;

import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningArrowTask extends BukkitRunnable
{
	private final Arrow arrow;
	
	public LightningArrowTask(Arrow arrow)
	{
		this.arrow = arrow;
	}
	
	@Override
	public void run()
	{	
		if(arrow.isOnGround() || arrow.isDead())
		{
			for(int i = 0; i < 10; i++) arrow.getWorld().strikeLightning(arrow.getLocation());
			cancel();
		}
		if(arrow == null) cancel();
	}
}
