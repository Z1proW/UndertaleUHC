package fr.ziprow.undertaleuhc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.enums.Team;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public final class Utils
{
	
	public static String line = "&m" + StringUtils.repeat(' ', 12);
	
	public static void clear(Player p)
	{
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setLevel(0);
		p.giveExpLevels(-Integer.MAX_VALUE);
		for(PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}
	
	public static String color(String str) {return ChatColor.translateAlternateColorCodes('&', str);}
	
	public static void sendMessage(Player p, String... msgs)
	{
		for(String msg : msgs) p.sendMessage(color(msg));
	}
	
	public static void informPlayer(Player p, String... msgs)
	{
		p.sendMessage("");
		for(String msg : msgs) p.sendMessage(ChatColor.YELLOW + msg);
	}
	
	public static void warnPlayer(Player p, String... msgs)
    {
        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
        p.sendMessage("");
        for(String msg : msgs) p.sendMessage(ChatColor.RED + "\u26A0 " + msg.toUpperCase() + " !");
    }
	
	public static void broadcast(String... msgs)
	{
		Bukkit.broadcastMessage("");
		for(String msg : msgs) Bukkit.broadcastMessage(color(msg));
	}
    
	public static ItemStack createItem(Material material, int amount, boolean glowing, boolean unbreakable, int durability, String name, String[] lore, Enchant... enchants)
    {
    	ItemStack item = new ItemStack(material, amount);
    	ItemMeta meta = item.getItemMeta();
    	if(name != null) meta.setDisplayName(name);
    	if(lore != null)
    	{
    		List<String> list = new ArrayList<>();
    		for(String s : lore) list.add(s);
    		meta.setLore(list);
    	}
    	if(glowing)
    	{
    		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    		meta.addEnchant(Enchantment.LUCK, 0, false);
    	}
    	if(unbreakable) meta.spigot().setUnbreakable(true);
    	if(durability != 0) item.setDurability((short)durability);
    	for(Enchant e : enchants) meta.addEnchant(e.getEnchantment(), e.getLevel(), true);
    	item.setItemMeta(meta);
    	return item;
    }
    
	public static ItemStack createPotionItem(String name, String[] lore, PotionEffect... effects)
    {
    	ItemStack item = new ItemStack(Material.POTION, 1);
		PotionMeta meta = (PotionMeta)item.getItemMeta();
		if(name != null) meta.setDisplayName(name);
    	if(lore != null)
    	{
    		List<String> list = new ArrayList<>();
    		for(String s : lore) list.add(s);
    		meta.setLore(list);
    	}
		for(PotionEffect effect : effects) meta.addCustomEffect(effect, true);
		item.setItemMeta(meta);
		return item;
    }
    
	public static ItemStack createEnchBook(String name, String[] lore, Enchant... enchants)
    {
    	ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
		if(name != null) meta.setDisplayName(name);
    	if(lore != null)
    	{
    		List<String> list = new ArrayList<>();
    		for(String s : lore) list.add(s);
    		meta.setLore(list);
    	}
		for(Enchant enchant : enchants) meta.addStoredEnchant(enchant.getEnchantment(), enchant.getLevel(), false);
		book.setItemMeta(meta);
		return book;
    }
    
	public static ItemStack nameItem(ItemStack item, String name, String... lore)
    {
    	ItemMeta meta = item.getItemMeta();
    	if(name != null) meta.setDisplayName(name);
    	if(lore != null)
    	{
    		List<String> list = new ArrayList<>();
    		for(String s : lore) list.add(s);
    		meta.setLore(list);
    	}
		return item;
    }
	
	public static String[] lore(String... lore) {return lore;}
	
	public static void info(Player p)
	{
		Role role = GameManager.rolesMap.get(p.getUniqueId());
		
		Player integrity = null;
		for(UUID u : GameManager.playing) if(GameManager.rolesMap.get(u).equals(Role.INTEGRITY)) integrity = Bukkit.getPlayer(u);
		if(GameManager.ally != null && GameManager.ally.equals(p.getUniqueId())) role.setTeam(Team.NEUTRAL);
		
		sendMessage(p,
		"",
		"&9" + line + "&9\u2764" + line,
		"&6Vous êtes &o" + role.getName() + "&r&6 !");
		
		switch(role.getTeam())
		{
			case HUMAN:
				sendMessage(p, "&6Vous devez gagner avec les humains !"); break;
			case MONSTER:
				sendMessage(p, "&6Vous devez gagner avec les monstres !"); break;
			case NEUTRAL:
				if(integrity != null && GameManager.ally != null && GameManager.ally.equals(p.getUniqueId()))
					sendMessage(p, "&6Vous devez gagner avec &o" + integrity.getName() + " &r&6 qui est " + Utils.getRole(integrity).getName() + " &r&6et votre vie est liee !");
				else if(role.equals(Role.INTEGRITY) && GameManager.ally != null)
				{
					Player allyP = Bukkit.getPlayer(GameManager.ally);
					sendMessage(p, "&6Vous devez gagner avec &o" + allyP.getName() + " &r&6 qui est " + Utils.getRole(allyP).getName() + " &r&6et votre vie est liee !");
				}
				else sendMessage(p, "&6Vous devez gagner tout seul !"); break;
		}
		
		switch(role)
		{
			case ALPHYS:
				sendMessage(p,
				"&6Deux fois dans la partie, pous pouvez 'ausculter' un joueur avec click droit + sneak",
				"&6Vous perdrez 1 coeur permanent en échange de la couleur de son Ame"); break;
			case ANGEL_OF_DEATH:
				sendMessage(p, "Vous pouvez avez maintenant résistance et vous pouvez provoquer des coups de foudre"); break;
			case ASGORE:
				sendMessage(p, "&6Vous pouvez crafter un trident avec une Ame"); break;
			case ASRIEL:
				sendMessage(p, "&6Quand vous serez le dernier monstre en vie vous vous transformerez en Ange de la mort"); break;
			case BRAVERY:
				sendMessage(p, "&6Vous devez éliminer un monstre avant l'épisode 5,", "&6sinon vous perdrez 2 coeurs permanents"); break;
			case CHARA:
				sendMessage(p, "&6Vous pouvez utiliser une épée tranchant IV"); break;
			case DETERMINATION:
				sendMessage(p, "&6Vous devez choisir un role entre &cChara &6et &cFrisk"); break;
			case FLOWEY:
				sendMessage(p, "&6Vous devez gagner avec les monstres"); break;
			case FRISK:
				sendMessage(p, "&6Vous devez choisir un joueur à  épargner,", "&6cependant si il meurt vous perdrez force"); break;
			case HATRED:
				sendMessage(p, "&6Vous perdrez Force I si &fAmour &6Meurt"); break;
			case INTEGRITY:
				sendMessage(p, "&6Vous partagez la même barre de vie que votre allié"); break;
			case JUSTICE:
				sendMessage(p,
				"&6Deux fois dans la partie, vous pouvez 'interroger' un joueur avec click droit + sneak",
				"&6Vous perdrez 1 coeur permanent en échange de la couleur de son Ame"); break;
			case KINDNESS:
				sendMessage(p,
				"&6Vous pourrez ressusciter un joueur lorsque il mourra, via le chat",
				"&6Cependant ce pouvoir est unique, et n'est pas utilisable sur vous-même"); break;
			case LOVE:
				sendMessage(p, "&6Vous perdrez Résistance I si &8Haine &6Meurt"); break;
			case METTATON:
				sendMessage(p, "&6A chaque épisode vous envoyez un quizz au joueurs proches,", "&6si personne ne trouve la réponse vous deviendrez Mettaton EX"); break;
			case METTATON_EX:
				sendMessage(p, "&6Vous prennez des dégâts quand vous êtes dans l'eau,", "&6si vous tuez un monstre vous vous transformez en &fMettaton NEO"); break;
			case METTATON_NEO:
				sendMessage(p, "&6Vous prennez toujours des dégâts dans l'eau,", "&6mais vous avez Résistance I"); break;
			case NAPSTABLOOK:
				sendMessage(p,
				"&6Deux fois dans la partie, vous pouvez enlever votre armure",
				"&6Vous obtiendrez Invisibilité I, Sauts améliorés II, Vitesse II et Faiblesse I"); break;
			case PAPYRUS:
				String sansPlayerName = null;
				for(UUID u : GameManager.playing)
					if(GameManager.rolesMap.get(u).equals(Role.SANS) || GameManager.rolesMap.get(u).equals(Role.SANS_GLOWING_EYE))
						sansPlayerName = Bukkit.getPlayer(u).getName();
				sendMessage(p, "&6Vous avez 3 coeurs d'absorption,", "&6et &fSans &6est &o" + sansPlayerName); break;
			case PATIENCE:
				sendMessage(p, "&6Gagne 1 coeur permanent à  l'épisode 4, et 7"); break;
			case PERSEVERANCE:
				sendMessage(p, "&6Vous possédez deux vies, sauf si votre meurtrier est un humain"); break;
			case PHOTOSHOP_FLOWEY:
				sendMessage(p, "&6Vous avez Force I et un coeur permanent de plus"); break;
			case SANS:
				String papyrusPlayerName = null;
				for(UUID u : GameManager.playing) if(GameManager.rolesMap.get(u).equals(Role.PAPYRUS)) papyrusPlayerName = Bukkit.getPlayer(u).getName();
				sendMessage(p, "&6Vous activez le mode 'Oeil brillant' quand &fPapyrus &6meurt,", "&6&fPapyrus &6est &o" + papyrusPlayerName); break;
			case SANS_GLOWING_EYE:
				sendMessage(p, "&6Vous avez maintenant le 'Gaster Blaster',", "&6vengez votre frère &fPapyrus"); break;
			case TORIEL:
				sendMessage(p, "&6Vous devez 'sympathiser' avec un joueur,", "&6si celui-ci meurt vous perdrez vos effets"); break;
			case UNDYNE:
				sendMessage(p, "&6Vous activez automatiquement le 'mode armure':", "&6Résistance II quand vous passez en dessous de 2 coeurs"); break;
		}
		sendMessage(p, "&9" + line + "&9\u2764" + line);
	}
	
	public static Role getRole(Player p)
	{
		return GameManager.rolesMap.get(p.getUniqueId());
	}
	
	public static Player getPlayer(Role r)
	{
		for(UUID u : GameManager.playing) if(GameManager.rolesMap.get(u).equals(r)) return Bukkit.getPlayer(u);
		return null;
	}
	
	@SuppressWarnings("rawtypes")
    public static void sendActionBar(Player player, String message)
    {
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + message + "\"}"));
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)ppoc);
    }
	
	public static boolean isSimilar(ItemStack i, ItemStack i2)
	{
		i.setDurability((short)0); i2.setDurability((short)0);
		return i.isSimilar(i2);
	}
	
}
