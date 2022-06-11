package fr.ziprow.undertaleuhc.enums;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Role
{
	DETERMINATION	("Détermination", 		ChatColor.RED, 			Team.HUMAN,     Collections.emptyList(), 																		Item.SOUL_RED.getItem()),
	CHARA			("Chara", 				ChatColor.RED, 			Team.NEUTRAL,   Collections.singletonList(PotionEffectType.INCREASE_DAMAGE), 										Item.SOUL_RED.getItem(), 		Item.REAL_KNIFE.getItem(), 		Item.HEART_LOCKET.getItem()),
	FRISK			("Frisk", 				ChatColor.RED, 			Team.HUMAN,     Collections.singletonList(PotionEffectType.INCREASE_DAMAGE), 										Item.SOUL_RED.getItem(), 		Item.STICK.getItem(), 			Item.BANDAGE.getItem()),
	PATIENCE		("Patience", 			ChatColor.AQUA, 		Team.HUMAN,     Collections.emptyList(), 																		Item.SOUL_CYAN.getItem(), 		Item.TOY_KNIFE.getItem(), 		Item.FADED_RIBBON.getItem()),
	BRAVERY			("Bravoure", 			ChatColor.GOLD, 		Team.HUMAN,     Collections.singletonList(PotionEffectType.FIRE_RESISTANCE), 										Item.SOUL_ORANGE.getItem(), 	Item.TOUGH_GLOVE.getItem(), 	Item.MANLY_BANDANNA.getItem()),
	INTEGRITY		("Intégrité", 			ChatColor.BLUE, 		Team.NEUTRAL,   Collections.singletonList(PotionEffectType.DAMAGE_RESISTANCE), 										Item.SOUL_BLUE.getItem(), 		Item.BALLET_SHOES.getItem(), 	Item.OLD_TUTU.getItem()),
	PERSEVERANCE	("Persévérance", 		ChatColor.LIGHT_PURPLE, Team.HUMAN,     Collections.emptyList(), 																		Item.SOUL_MAGENTA.getItem(), 	Item.TORN_NOTEBOOK.getItem(), 	Item.CLOUDY_GLASSES.getItem()),
	KINDNESS		("Gentillesse", 		ChatColor.GREEN, 		Team.HUMAN,     Collections.singletonList(PotionEffectType.DAMAGE_RESISTANCE), 										Item.SOUL_GREEN.getItem(), 		Item.BURNT_PAN.getItem(), 		Item.STAINED_APRON.getItem()),
	JUSTICE			("Justice", 			ChatColor.YELLOW, 		Team.HUMAN,     Collections.emptyList(), 																		Item.SOUL_YELLOW.getItem(), 	Item.EMPTY_GUN.getItem(), 		Item.COWBOY_HAT.getItem()),
	LOVE			("Amour", 				ChatColor.WHITE, 		Team.HUMAN,     Collections.singletonList(PotionEffectType.DAMAGE_RESISTANCE), 										Item.SOUL_WHITE.getItem(), 		Item.CUPID_BOW.getItem(), 		Item.REVIGORATING_STRENGTH.getItem()),
	HATRED			("Haine", 				ChatColor.DARK_GRAY, 	Team.NEUTRAL,   Collections.singletonList(PotionEffectType.INCREASE_DAMAGE), 										Item.SOUL_BLACK.getItem(), 		Item.DARK_BOW.getItem(), 		Item.EVIL_ESSENCE.getItem()),

	FLOWEY			("Flowey", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.FRIENDLINESS_PELLETS.getItem()),
	PHOTOSHOP_FLOWEY("Photoshop Flowey", 	ChatColor.WHITE, 		Team.NEUTRAL,   Collections.emptyList(), 																		null),
	ASRIEL			("Asriel", 			ChatColor.WHITE, 		Team.NEUTRAL,   Collections.emptyList(), 																		null),
	ANGEL_OF_DEATH	("Ange de la Mort", 	ChatColor.WHITE, 		Team.NEUTRAL,   Collections.singletonList(PotionEffectType.DAMAGE_RESISTANCE), 										null, 	Item.CHAOS_BUSTER.getItem()),
	TORIEL			("Toriel", 			ChatColor.WHITE, 		Team.MONSTER, 	Arrays.asList(PotionEffectType.FIRE_RESISTANCE, PotionEffectType.DAMAGE_RESISTANCE), 	null, 	Item.HORNS.getItem(), 			Item.STRONG_FIRE.getItem()),
	UNDYNE			("Undyne", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.WARRIOR_HELMET.getItem(), 	Item.ARROWS.getItem(), Item.RAPID_BOW.getItem()),
	SANS			("Sans", 				ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.BONE.getItem()),
	SANS_GLOWING_EYE("Sans Oeil Brillant", ChatColor.WHITE, 		Team.MONSTER,   Collections.singletonList(PotionEffectType.INCREASE_DAMAGE), 										null, 	Item.GASTER_BLASTER.getItem()),
	PAPYRUS			("Papyrus", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.singletonList(PotionEffectType.FIRE_RESISTANCE), 										null, 	Item.BONE.getItem(), 			Item.SCARF.getItem()),
	ALPHYS			("Alphys", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.BLOUSE.getItem()),
	METTATON		("Mettaton", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.IRON_BODY.getItem()),
	METTATON_EX		("Mettaton EX", 		ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null),
	METTATON_NEO	("Mettaton NEO", 		ChatColor.WHITE, 		Team.NEUTRAL,   Collections.singletonList(PotionEffectType.DAMAGE_RESISTANCE), 										null),
	ASGORE			("Asgore", 			ChatColor.WHITE, 		Team.MONSTER,   Collections.singletonList(PotionEffectType.FIRE_RESISTANCE), 										null, 	Item.HORNS.getItem(), Item.CAPE.getItem()),
	NAPSTABLOOK		("Napstablook", 		ChatColor.WHITE, 		Team.MONSTER,   Collections.emptyList(), 																		null, 	Item.TEARS.getItem(), Item.DAPPER_BLOOK.getItem());

	private final String name;

	private final ChatColor color;

	private Team team;

	private final ItemStack[] items;

	private final List<PotionEffectType> effects;

	private final ItemStack soul;

	Role(String rawName, ChatColor color, Team team, List<PotionEffectType> effects, ItemStack soul, ItemStack... items)
	{
		name = color + rawName;
		this.color = color;
		this.team = team;
		this.items = items;
		this.effects = effects;
		this.soul = soul;
	}

	public String getName() {return name;}
	public ChatColor getColor() {return color;}
	public Team getTeam() {return team;}
	public ItemStack[] getItems() {return items;}
	public List<PotionEffectType> getPotionEffects() {return effects;}
	public ItemStack getSoulItem() {return soul;}

	public void setTeam(Team team) {this.team = team;}

	public void giveStuff(Player p)
	{
		for(PotionEffectType type : getPotionEffects())
		{
			if(p.hasPotionEffect(type)) p.removePotionEffect(type);
			p.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, 0, false, false));
		}

		for(ItemStack i : getItems())
		{
			if(p.getInventory().firstEmpty() != -1) p.getInventory().addItem(i);
			else p.getWorld().dropItem(p.getLocation(), i);
		}
	}
}
