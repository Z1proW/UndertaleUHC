package fr.ziprow.undertaleuhc.tasks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.Role;

public class ShareHealth extends BukkitRunnable
{
	private boolean firstLoop = true;
	private UUID integrityUUID = Utils.getPlayer(Role.INTEGRITY).getUniqueId();
	private UUID allyUUID = GameManager.ally;
	private double maxHealth;
	private double health;
	
	@Override
	public void run()
	{
		Player integrity = Bukkit.getPlayer(integrityUUID);
		Player ally = Bukkit.getPlayer(allyUUID);
		
		if(integrity == null || ally == null) return;
		
		if(firstLoop)
		{
			maxHealth = integrity.getMaxHealth() + ally.getMaxHealth();
			health = integrity.getHealth() + ally.getHealth();
			
			integrity.setMaxHealth(maxHealth); ally.setMaxHealth(maxHealth);
			integrity.setHealth(health); ally.setHealth(maxHealth);
			
			firstLoop = false;
		}
		else
		{
			if(integrity.getMaxHealth() != maxHealth || ally.getMaxHealth() != maxHealth)
			{
				maxHealth = integrity.getMaxHealth() + ally.getMaxHealth() - maxHealth;
				if(maxHealth < 0) maxHealth = 0;
				integrity.setMaxHealth(maxHealth); ally.setMaxHealth(maxHealth);
			}
			else if(integrity.getHealth() != maxHealth || ally.getHealth() != maxHealth)
			{
				health = integrity.getHealth() + ally.getHealth() - health;
				if(health < 0) health = 0;
				integrity.setHealth(health); ally.setHealth(health);
			}
		}
	}
}
