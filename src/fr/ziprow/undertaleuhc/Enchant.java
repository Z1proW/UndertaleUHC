package fr.ziprow.undertaleuhc;

import org.bukkit.enchantments.Enchantment;

public class Enchant
{
	private Enchantment enchant;
	private int lvl;
	
	public Enchant(Enchantment enchant, int lvl) {this.enchant = enchant; this.lvl = lvl;}
	
	public Enchantment getEnchantment() {return enchant;}
	public int getLevel() {return lvl;}
}
