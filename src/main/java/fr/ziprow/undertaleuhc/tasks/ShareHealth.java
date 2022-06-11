package fr.ziprow.undertaleuhc.tasks;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class ShareHealth extends BukkitRunnable
{
	private boolean firstLoop = true;
	private final UUID integrityUUID = Objects.requireNonNull(Utils.getPlayer(Role.INTEGRITY)).getUniqueId();
	private final UUID allyUUID = GameManager.ally;
	private double maxHealth;
	private double health;
	
	@Override
	public void run()
	{	
		Player integrity = Bukkit.getPlayer(integrityUUID);
		Player ally = Bukkit.getPlayer(allyUUID);
		
		if(integrity == null || ally == null) cancel();

		assert integrity != null;
		assert ally != null;

		if(firstLoop)
		{

			maxHealth = integrity.getMaxHealth() + ally.getMaxHealth();
			health = integrity.getHealth() + ally.getHealth();
			
			integrity.setMaxHealth(maxHealth); ally.setMaxHealth(maxHealth);
			integrity.setHealth(health); ally.setHealth(health);
			
			Utils.inform(ally, "");
			
			firstLoop = false;
		}
		else
		{

			if(integrity.getGameMode().equals(GameMode.SPECTATOR))
			{
				ally.setHealth(0);
				cancel();
			}
			else if(ally.getGameMode().equals(GameMode.SPECTATOR))
			{
				integrity.setHealth(0); 
				cancel();
			}
			else if(integrity.getMaxHealth() != maxHealth || ally.getMaxHealth() != maxHealth)
			{
				maxHealth = integrity.getMaxHealth() + ally.getMaxHealth() - maxHealth;
				integrity.setMaxHealth(maxHealth); ally.setMaxHealth(maxHealth);
			}
			else if(integrity.getHealth() != health || ally.getHealth() != health)
			{
				health = integrity.getHealth() + ally.getHealth() - health;
				integrity.setHealth(health); ally.setHealth(health);
			}
		}
	}
}
