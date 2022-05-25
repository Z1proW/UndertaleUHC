package fr.ziprow.undertaleuhc;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class Enchant
{
	
	private Enchantment enchant;
	private int lvl;
	
	public Enchant(Enchantment enchant) {this.enchant = enchant; this.lvl = 1;}
	public Enchant(Enchantment enchant, int lvl) {this.enchant = enchant; this.lvl = lvl;}
	
	public Enchantment getEnchantment() {return enchant;}
	public int getLevel() {return lvl;}
	
	public static Enchant[] list(Enchant... enchants) {return enchants;}
	
	public static void addEnchant(ItemMeta meta, Enchant e) {meta.addEnchant(e.getEnchantment(), e.getLevel(), true);}
	
}
