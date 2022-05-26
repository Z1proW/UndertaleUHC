package fr.ziprow.undertaleuhc.enums;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public enum Item
{
	SOUL_RED("soul.red"),
	SOUL_CYAN("soul.cyan"),
	SOUL_ORANGE("soul.orange"),
	SOUL_BLUE("soul.blue"),
	SOUL_MAGENTA("soul.magenta"),
	SOUL_GREEN("soul.green"),
	SOUL_YELLOW("soul.yellow"),
	SOUL_WHITE("soul.white"),
	SOUL_BLACK("soul.black"),
	
	// Determination
	
	// Chara
	REAL_KNIFE("book.realKnife"),
	HEART_LOCKET("item.heartLocket"),
	
	// Frisk
	STICK("book.stick"),
	BANDAGE("item.bandage"),
	
	// Patience
	TOY_KNIFE("book.toyKnife"),
	FADED_RIBBON("item.fadedRibbon"),
	
	// Bravery
	TOUGH_GLOVE("book.toughGlove"),
	MANLY_BANDANNA("item.manlyBandanna"),
	ADRENALINE_RUSH("potion.adrenalineRush"),
	
	// Integrity
	BALLET_SHOES("book.balletShoes"),
	OLD_TUTU("item.oldTutu"),
	
	// Perseverance
	TORN_NOTEBOOK("book.tornNotebook"),
	CLOUDY_GLASSES("item.cloudyGlasses"),
	
	// Kindness
	BURNT_PAN("book.burntPan"),
	STAINED_APRON("item.stainedApron"),
	
	// Justice
	EMPTY_GUN("book.emptyGun"),
	COWBOY_HAT("item.cowboyHat"),
	
	// Love
	CUPID_BOW("item.cupidBow"),
	REVIGORATING_STRENGTH("book.revigoratingStrength"),
	
	// Hatred
	DARK_BOW("item.darkBow"),
	EVIL_ESSENCE("book.evilEssence"),
	
	// Flowey
	FRIENDLINESS_PELLETS("item.friendlinessPellets"),
	
	// Photoshop Flowey
	
	// Asriel
	
	// Angel of Death
	CHAOS_BUSTER("item.chaosBuster"),
	
	// Toriel
	HORNS("book.horns"),
	STRONG_FIRE("book.strongFire"),
	
	// Undyne
	WARRIOR_HELMET("book.warriorHelmet"),
	ARROWS("item.arrows"),
	RAPID_BOW("item.rapidBow"),
	
	// Sans
	BONE("book.bone"),
	
	// Sans Glowing Eye
	GASTER_BLASTER("item.gasterBlaster"),
	
	// Papyrus
	// BONE
	SCARF("item.scarf"),
	
	// Alphys
	BLOUSE("book.blouse"),
	
	// Mettaton
	IRON_BODY("book.ironBody"),
	
	// Mettaton EX
	
	// Mettaton NEO
	
	// ASGORE
	// HORNS
	CAPE("book.cape"),
	TRIDENT("item.trident"),
	
	// NAPSTABLOOK
	TEARS("item.tears"),
	DAPPER_BLOOK("item.dapperBlook");

	private final String path;

	Item(String path) {this.path = path;}
	
	public String getPath() {return path;}
	
	public ItemStack getItem()
	{
		ConfigurationSection sct = UndertaleUHC.get().getItemsConfig().getConfigurationSection(path);
		
		if(path.startsWith("soul"))
		{
			ItemStack soul = new ItemStack(Material.INK_SACK);
	    	ItemMeta meta = soul.getItemMeta();
	    	String name = sct.getString("name");
	    	List<String> lore = sct.getStringList("lore");
	    	
	    	if(name != null) meta.setDisplayName(ChatColor.RESET + name);
			lore.add(0, "En absorbant cette âme vous obtiendrez:");
			lore.add("");
			lore.add(ChatColor.RED + "Attention une explosion alertera les joueurs à proximité !");
	    	meta.setLore(lore);
	    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    	meta.addEnchant(Enchantment.LUCK, 0, false);
	    	soul.setDurability((short)sct.getInt("dura"));
	    	soul.setItemMeta(meta);
	    	return soul;
		}
		else if(path.startsWith("book"))
		{
			ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
	    	EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
	    	String name = sct.getString("name");
	    	List<String> enchants = sct.getStringList("enchants");
	    	List<Integer> lvls = sct.getIntegerList("levels");
	    	
	    	if(name != null) meta.setDisplayName(ChatColor.RESET + name);
	    	for(int i = 0; i < enchants.size(); i++)
	    		meta.addStoredEnchant(Enchantment.getByName(enchants.get(i)), lvls.get(i), false);
	    	book.setItemMeta(meta);
	    	return book;
		}
		else if(path.startsWith("potion"))
		{
			ItemStack potion = new ItemStack(Material.POTION);
			PotionMeta meta = (PotionMeta)potion.getItemMeta();
			String name = sct.getString("name");
			List<String> effects = sct.getStringList("effects");
			List<Integer> durations = sct.getIntegerList("durations");
			List<Integer> amplifiers = sct.getIntegerList("amplifiers");
			List<Boolean> ambients = sct.getBooleanList("ambients");
			List<Boolean> particles = sct.getBooleanList("particles");
			
			if(name != null) meta.setDisplayName(ChatColor.RESET + name);
			for(int i = 0; i < effects.size(); i++)
				meta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(effects.get(i)), durations.get(i), amplifiers.get(i), ambients.get(i), particles.get(i)), true);
			potion.setItemMeta(meta);
			return potion;
		}
		else if(path.startsWith("item"))
		{
			ItemStack item = new ItemStack(Material.matchMaterial(sct.getString("material")), sct.getInt("amount", 1));
		    ItemMeta meta = item.getItemMeta();
		    String name = sct.getString("name");
		    List<String> lore = sct.getStringList("lore");
		    int dura = sct.getInt("dura");
		    List<String> enchants = sct.getStringList("enchants");
		    List<Integer> lvls = sct.getIntegerList("levels");
		    
		    if(name != null) meta.setDisplayName(ChatColor.RESET + name);
		    if(!lore.isEmpty()) meta.setLore(lore);
		    item.setDurability((short)dura);
		    for(int i = 0; i < enchants.size(); i++)
		    	meta.addEnchant(Enchantment.getByName(enchants.get(i)), lvls.get(i), true);
		    item.setItemMeta(meta);
		    return item;
		}
		return null;
	}
}
