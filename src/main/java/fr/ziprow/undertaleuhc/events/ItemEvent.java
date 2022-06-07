package fr.ziprow.undertaleuhc.events;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.Item;
import fr.ziprow.undertaleuhc.tasks.LightningArrowTask;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class ItemEvent implements Listener
{
	private static UndertaleUHC main;
	
	Enchantment fire = Enchantment.FIRE_ASPECT;
	
	public ItemEvent(UndertaleUHC main) {ItemEvent.main = main;}
	
	@EventHandler
	public static void onRightClick(PlayerInteractEvent event)
	{
		ItemStack item = event.getItem();
		
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK || item == null) return;
		
		if(item.equals(Item.SOUL_RED.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_RED.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*3, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_CYAN.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_CYAN.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*5, 0, false, false));
			if(!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*60*5, 0, false, false));
			if(!player.hasPotionEffect(PotionEffectType.SLOW)) player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*60*5, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_ORANGE.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_ORANGE.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*60*5, 0, false, false));
			if(!player.hasPotionEffect(PotionEffectType.SPEED)) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*3, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_BLUE.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_BLUE.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*3, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_MAGENTA.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_MAGENTA.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 20*60*5, 0, false, false));
			if(!player.hasPotionEffect(PotionEffectType.HEAL)) player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_GREEN.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_GREEN.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.REGENERATION)) player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*5, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_YELLOW.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_YELLOW.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			player.setVelocity(player.getEyeLocation().getDirection().multiply(20).setY(2));
			if(!player.hasPotionEffect(PotionEffectType.WEAKNESS)) player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*60, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_WHITE.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_WHITE.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*5, 0, false, false));
			return;
		}
		if(item.equals(Item.SOUL_BLACK.getItem()))
		{
			event.setCancelled(true);
			Player player = event.getPlayer();
			player.getWorld().playEffect(player.getLocation().add(0, 3, 0), Effect.HEART, 0);
			player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.getInventory().removeItem(Item.SOUL_BLACK.getItem());
			Utils.informPlayer(player, "Vous avez absorbé l'âme");
			if(!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*5, 0, false, false));
			return;
		}
	}
	
	@EventHandler
	public static void onShoot(EntityShootBowEvent event)
	{
		if(!(event.getProjectile() instanceof Arrow)) return;
		
		ItemStack bow = event.getBow();
		Arrow arrow = (Arrow)event.getProjectile();
		
		if(bow.getItemMeta().equals(Item.CHAOS_BUSTER.getItem().getItemMeta()))
		{
			new LightningArrowTask(arrow).runTaskTimer(main, 0, 1);
			return;
		}
		
		if(bow.getItemMeta().equals(Item.CUPID_BOW.getItem().getItemMeta()))
		{
			Vector v = arrow.getVelocity();
			event.setProjectile(null);
			Potion potion = new Potion(PotionType.INSTANT_HEAL, 2);
			potion.setSplash(true);
			ItemStack itemStack = new ItemStack(Material.POTION);
			potion.apply(itemStack);
			ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
			thrownPotion.setItem(itemStack);
			thrownPotion.setVelocity(v);
			return;
		}
		
		if(bow.getItemMeta().equals(Item.DARK_BOW.getItem().getItemMeta()))
		{
			Vector v = arrow.getVelocity();
			event.setProjectile(null);
			Potion potion = new Potion(PotionType.INSTANT_DAMAGE, 2);
			potion.setSplash(true);
			ItemStack itemStack = new ItemStack(Material.POTION);
			potion.apply(itemStack);
			ThrownPotion thrownPotion = arrow.getShooter().launchProjectile(ThrownPotion.class);
			thrownPotion.setItem(itemStack);
			thrownPotion.setVelocity(v);
			return;
		}
		
		if(bow.getItemMeta().equals(Item.RAPID_BOW.getItem().getItemMeta()))
		{
			arrow.setVelocity(arrow.getVelocity().multiply(1.6));
			return;
		}
	}
	
	@EventHandler
	public void TridentFireAspect(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player) || ((Player)event.getDamager()).getInventory().getItemInHand() == null || !((Player)event.getDamager()).getInventory().getItemInHand().containsEnchantment(fire) || !((Player)event.getDamager()).getInventory().getItemInHand().getItemMeta().equals(Item.TRIDENT.getItem().getItemMeta())) return;
		
		if(Math.random() * 100.0D < 10) event.getEntity().setFireTicks(20*3);
	}
}
