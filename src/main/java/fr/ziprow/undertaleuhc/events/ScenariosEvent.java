package fr.ziprow.undertaleuhc.events;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Item;
import fr.ziprow.undertaleuhc.enums.Role;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.HashMap;
import java.util.Map;

public class ScenariosEvent implements Listener
{
	private UndertaleUHC main;
	private GameManager gameManager;
	
	Map<Player, Integer> limitPerPlayer = new HashMap<>();
	Enchantment prot = Enchantment.PROTECTION_ENVIRONMENTAL;
	Enchantment sharp = Enchantment.DAMAGE_ALL;
	Enchantment fire = Enchantment.FIRE_ASPECT;
	Enchantment power = Enchantment.ARROW_DAMAGE;
	
	public ScenariosEvent(UndertaleUHC main, GameManager gameManager) {this.main = main; this.gameManager = gameManager;}

	@EventHandler
	public void NoHorses(EntityMountEvent event) {event.setCancelled(true);}
	
	@EventHandler
	public void NoDogs(EntityTameEvent event) {event.setCancelled(true);}
	
	@EventHandler
	public void MoreApples(LeavesDecayEvent event)
	{
		event.getBlock().setType(Material.AIR);
		
		if(Math.random() * 100. < 4)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
	}
	
	@EventHandler
	public void DisableCrafts(PrepareItemCraftEvent event)
	{
		if(event.getInventory() == null) return;
		ItemStack item = event.getInventory().getResult();
		if(item.getType() == Material.FISHING_ROD
		|| item.getType() == Material.GOLDEN_APPLE && item.getDurability() == 1
		|| item.getType() == Material.FLINT_AND_STEEL
		|| item.getType() == Material.BED
		|| item.getType() == Material.BREWING_STAND_ITEM
		|| item.equals(Item.TRIDENT.getItem()) && (!GameManager.rolesMap.get(event.getView().getPlayer().getUniqueId()).equals(Role.ASGORE)))
			event.getInventory().setResult(new ItemStack(Material.AIR));
	}

	@EventHandler
	public void NoMilk(PlayerInteractEvent event)
	{
		Action a = event.getAction();
		    
		if((a == Action.RIGHT_CLICK_AIR
		|| a == Action.RIGHT_CLICK_BLOCK)
		&& event.getPlayer().getInventory().getItemInHand().getType() == Material.MILK_BUCKET)
			event.setCancelled(true);
	}
	
	@EventHandler
	public void NoSnowballNoEgg(ProjectileLaunchEvent event)
	{
		if(event.getEntity() instanceof org.bukkit.entity.Snowball
		|| event.getEntity() instanceof org.bukkit.entity.Egg)
			event.setCancelled(true); 
	}

	@EventHandler
	public void NoChat(AsyncPlayerChatEvent event)
	{
		((Cancellable) event).setCancelled(true);
		Utils.informPlayer(event.getPlayer(), "Le chat est désactivé");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void XPBoost(BlockExpEvent event) {event.setExpToDrop((int)(event.getExpToDrop() * 2));}
	
	@EventHandler
	public void FastSmelting(FurnaceBurnEvent event)
	{
		final Furnace block = (Furnace)event.getBlock().getState();
			
		(new BukkitRunnable()
		{
			public void run()
			{
				if(block.getCookTime() > 0 || block.getBurnTime() > 0)
				{
					block.setCookTime((short)(block.getCookTime() + 8));
					block.update();
				}
				else cancel();
			}
		}).runTaskTimer((Plugin)this.main, 1L, 1L);
	}
	
	@EventHandler
	private void FireLess(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player)) return;
		
		if(event.getCause().equals(DamageCause.LAVA)
		|| event.getCause().equals(DamageCause.FIRE))
			event.setCancelled(true);
	}
	
	@EventHandler
	private void NoNether(PlayerTeleportEvent event)
	{
		if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
		|| event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void NoNotchApple(PlayerItemConsumeEvent event)
	{
		if(event.getItem().getType() == Material.GOLDEN_APPLE)
		{
			Player p = event.getPlayer();
			
			if(event.getItem().getDurability() == 1)
			{
				event.setCancelled(true);
				p.getInventory().remove(event.getItem());
				p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, event.getItem().getAmount()));
				return;
			}
			
			Player papyrus = Utils.getPlayer(Role.PAPYRUS);
			if(papyrus != null && papyrus.equals(p))
			{
				p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 2, 0), true);
				EntityPlayer ep = ((CraftPlayer)p).getHandle();
				ep.setAbsorptionHearts(6);
				Bukkit.getScheduler().runTaskLater(main, new Runnable()
				{
					@Override
					public void run()
					{
						if(!p.hasPotionEffect(PotionEffectType.ABSORPTION))
							ep.setAbsorptionHearts(0);
					}
				}, 20 * 60 * 2);
			}
		}
	}
	
	@EventHandler
	public void EnchantLimits(EnchantItemEvent event)
	{
		Map<Enchantment, Integer> enchants = event.getEnchantsToAdd();
		
		if(enchants.containsKey(sharp) && enchants.get(sharp) > 3
		&& (!Utils.getRole(event.getEnchanter()).equals(Role.CHARA)
		|| enchants.containsKey(sharp) && enchants.get(sharp) > 4)
		|| enchants.containsKey(prot) && enchants.get(prot) > 2
		|| enchants.containsKey(fire)
		|| enchants.containsKey(power) && enchants.get(power) > 2)
			event.setCancelled(true);
	}

	@EventHandler
	public void AnvilLimits(InventoryClickEvent event)
	{
		if(!(event.getClickedInventory() instanceof AnvilInventory) || event.getSlot() != 2) return;
	
		Inventory inv = event.getClickedInventory();
		ItemStack item = inv.getItem(2);
		
		if(item == null) return;
		
		if(item.getType() == Material.ENCHANTED_BOOK
		&& item.getItemMeta() instanceof EnchantmentStorageMeta)
		{
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
			Map<Enchantment, Integer> enchants = meta.getStoredEnchants();
			
			if((enchants.containsKey(sharp) && enchants.get(sharp) > 3
			&& (!Utils.getRole((Player)event.getWhoClicked()).equals(Role.CHARA)
			|| enchants.containsKey(sharp) && enchants.get(sharp) > 4))
			|| (enchants.containsKey(prot) && enchants.get(prot) > 2
			|| enchants.containsKey(fire)
			|| enchants.containsKey(power) && enchants.get(power) > 2)
			|| (item.containsEnchantment(sharp) && item.getEnchantmentLevel(sharp) > 3
			&& (!Utils.getRole((Player)event.getWhoClicked()).equals(Role.CHARA)
			|| item.containsEnchantment(sharp) && item.getEnchantmentLevel(sharp) > 4)
			|| item.containsEnchantment(prot) && item.getEnchantmentLevel(prot) > 2
			|| item.containsEnchantment(fire)
			|| item.containsEnchantment(power) && item.getEnchantmentLevel(power) > 2))
				inv.setItem(2, new ItemStack(Material.AIR));
		}
		
		ItemMeta meta = item.getItemMeta();
		Map<Enchantment, Integer> enchants = meta.getEnchants();
		
		if((enchants.containsKey(sharp) && enchants.get(sharp) > 3
		&& (!Utils.getRole((Player)event.getWhoClicked()).equals(Role.CHARA)
		|| enchants.containsKey(sharp) && enchants.get(sharp) > 4))
		|| (enchants.containsKey(prot) && enchants.get(prot) > 2
		|| enchants.containsKey(fire)
		|| enchants.containsKey(power) && enchants.get(power) > 2)
		|| (item.containsEnchantment(sharp) && item.getEnchantmentLevel(sharp) > 3
		&& (!Utils.getRole((Player)event.getWhoClicked()).equals(Role.CHARA)
		|| item.containsEnchantment(sharp) && item.getEnchantmentLevel(sharp) > 4)
		|| item.containsEnchantment(prot) && item.getEnchantmentLevel(prot) > 2
		|| item.containsEnchantment(fire)
		|| item.containsEnchantment(power) && item.getEnchantmentLevel(power) > 2))
			inv.setItem(2, new ItemStack(Material.AIR));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		ItemStack helmet = player.getInventory().getHelmet();
		ItemStack chestplate = player.getInventory().getChestplate();
		ItemStack leggings = player.getInventory().getLeggings();
		ItemStack boots = player.getInventory().getBoots();
		
		if((chestplate != null && chestplate.getType().equals(Material.DIAMOND_CHESTPLATE)) || (boots != null && boots.getType().equals(Material.DIAMOND_BOOTS)))
		{
			if(helmet != null)
			{
				helmet.setType(Material.IRON_HELMET);
				player.getInventory().setHelmet(helmet);
			}
			if(leggings != null)
			{
				leggings.setType(Material.IRON_LEGGINGS);
				player.getInventory().setLeggings(leggings);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(gameManager.isState(GameState.STARTING, GameState.PLAYING)) return;
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();
		Location loc = block.getLocation().add(.5, .5, .5);
		
		if((material == Material.DIAMOND_ORE
		|| material == Material.GOLD_ORE
		|| material == Material.IRON_ORE)
		&& !gameManager.isState(GameState.EP1, GameState.EP2, GameState.EP3))
		{
			player.sendTitle("&cMEETUP", "Remontez à la surface");
			((ExperienceOrb)block.getWorld().spawn(loc, ExperienceOrb.class)).setExperience(event.getExpToDrop());
			block.setType(Material.AIR);
			return;
		}
		
		switch(material)
		{
			case STONE:
				if(block.getData() != 0)
					block.setData((byte)0);
				break;
				
			case GRAVEL:
				block.setType(Material.AIR);
				Material newMaterial = Material.FLINT;
				if (Math.random() * 100. < 50)
					newMaterial = Material.GRAVEL;
				event.getBlock().getWorld().dropItemNaturally(loc, new ItemStack(newMaterial));
				break;
				
			case DIAMOND_ORE:
				Material hand = player.getItemInHand().getType();
				if(hand != Material.DIAMOND_PICKAXE && hand != Material.IRON_PICKAXE)
					break;
				if(limitPerPlayer.getOrDefault(player, 0).intValue() >= 17)
				{
					block.getWorld().dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 1));
					((ExperienceOrb)block.getWorld().spawn(loc, ExperienceOrb.class)).setExperience(event.getExpToDrop());
					block.setType(Material.AIR);
				}
				limitPerPlayer.put(player, limitPerPlayer.getOrDefault(player, 0).intValue() + 1);
				break;
				
			default:
				break;
		}
	}
	
	@EventHandler
	public void noFall(EntityDamageEvent event)
	{
		if(gameManager.isState(GameState.EP1)
		&& event.getEntity() instanceof Player
		&& event.getCause().equals(DamageCause.FALL))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void StrengthNerf(EntityDamageByEntityEvent event)
	{
		if(!(event.getDamager() instanceof Player)) return;
		
		if(((Player)event.getDamager()).hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
			event.setDamage(.6 * event.getDamage());
	}
	
}
