package fr.ziprow.undertaleuhc.events;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RemoveArmorEvent implements Listener
{
	private UndertaleUHC main;
	private GameManager gameManager;
	
	public RemoveArmorEvent(UndertaleUHC main, GameManager gameManager)
	{
		this.main = main;
		this.gameManager = gameManager;
	}
	
	@EventHandler
	public void onRemoveArmor(InventoryClickEvent e)
	{
		if(gameManager.isState(GameState.EP1, GameState.EP2, GameState.EP3)) return;
		
		Player p = (Player)e.getWhoClicked();
		
		if(Utils.getRole(p) != Role.NAPSTABLOOK) return;
		
		boolean hadArmor = hasArmor(p);
		Bukkit.getScheduler().runTaskLater(main, new Runnable()
		{
			@Override
			public void run()
			{
				for(PotionEffectType type : new PotionEffectType[] {PotionEffectType.INVISIBILITY, PotionEffectType.JUMP, PotionEffectType.SPEED, PotionEffectType.WEAKNESS})
				{
					if(hadArmor && !hasArmor(p)) 
					{
						if(p.hasPotionEffect(type)) p.removePotionEffect(type);
						p.addPotionEffect(new PotionEffect(type, 20*60*5, 1, false, false));
					}
					if(!hadArmor && hasArmor(p))
						if(p.hasPotionEffect(type)) p.removePotionEffect(type);
				}
			}
		}, 1);
	}
	
	private boolean hasArmor(Player p)
	{
		for(ItemStack i : p.getInventory().getArmorContents())
			if(i.getType() != Material.AIR) return true;
		return false;
	}
	
}
