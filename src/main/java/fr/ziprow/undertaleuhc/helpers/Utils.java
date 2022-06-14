package fr.ziprow.undertaleuhc.helpers;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.enums.Team;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public final class Utils
{

	public static final String LINE_SEPARATOR = StringUtils.repeat('-', 12);
	private static final Set<Material> TRANSPARENT = EnumSet.of(Material.AIR, Material.CARPET);
	private static final org.bukkit.ChatColor INFORM_COLOR = org.bukkit.ChatColor.YELLOW;
	private static final org.bukkit.ChatColor WARN_COLOR = org.bukkit.ChatColor.RED;

	public static String color(String s)
	{
		return org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
	}

	public static void broadcastMessages(String... msgs)
	{
		Bukkit.broadcastMessage("");
		for(String msg : msgs) Bukkit.broadcastMessage(color(msg));
	}

	public static Location stringToLocation(String string)
	{
		String[] parts = string.split(",");
		return new Location(
				Bukkit.getWorld(parts[0].substring(15)),
				Double.parseDouble(parts[0].substring(2)),
				Double.parseDouble(parts[1].substring(2)),
				Double.parseDouble(parts[2].substring(2)),
				Float.parseFloat(parts[3].substring(6)),
				Float.parseFloat(parts[4].substring(4)
						.replace('}', ' '))
		);
	}

	public static void nameItem(ItemStack item, String name)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
	}

	public static void loreItem(ItemStack item, String... lore)
	{
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
	}

	public static void setUnbreakable(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
	}

	public static void setGlowing(ItemStack item)
	{
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.LUCK, 0, false);
		item.setItemMeta(meta);
	}

	public static void setDurability(ItemStack item, double durabilityPercent)
	{
		item.setDurability((short)(durabilityPercent * item.getType().getMaxDurability()));
	}

	public static String convertToInvisibleString(String s)
	{
		StringBuilder hidden = new StringBuilder();
		for(char c : s.toCharArray())
			hidden.append(org.bukkit.ChatColor.COLOR_CHAR + "").append(c);
		return hidden.toString();
	}

	public static String convertToVisibleString(String s)
	{
		return s.replaceAll(org.bukkit.ChatColor.COLOR_CHAR + "", "");
	}

	public static boolean isSimilar(ItemStack i, ItemStack i2)
	{
		if(i == i2) return true;

		ItemStack ii = i.clone();

		switch(ii.getType())
		{
			case WOOD:
			case WOOL:
			case CARPET:
			case INK_SACK:
			case DOUBLE_STONE_SLAB2:
			case STONE_SLAB2:
				break;

			default:
				ii.setDurability(i2.getDurability());
		}
		return ii == i2;
	}

	public static ItemStack createPotionItem(PotionEffect... effects)
	{
		ItemStack item = new ItemStack(Material.POTION, 1);
		PotionMeta meta = (PotionMeta)item.getItemMeta();
		for(PotionEffect effect : effects)
			meta.addCustomEffect(effect, true);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createEnchBook(Map<Enchantment, Integer> enchants)
	{
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
		for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
			meta.addStoredEnchant(entry.getKey(), entry.getValue(), false);
		book.setItemMeta(meta);
		return book;
	}

	public static ItemStack createEnchBook(Object... enchants)
	{
		return createEnchBook(map(enchants));
	}

	public static <K, V> Map<K, V> map(Object... inputs)
	{
		if((inputs.length & 1) != 0) // + null check
			throw new InternalError("length is odd");

		Map<K, V> map = new HashMap<>();

		for(int i = 0; i < inputs.length; i += 2)
		{
			@SuppressWarnings("unchecked")
			K k = Objects.requireNonNull((K)inputs[i]);
			@SuppressWarnings("unchecked")
			V v = Objects.requireNonNull((V)inputs[i+1]);
			map.put(k, v);
		}

		return map;
	}

	/* Player */

	public static void sendMessage(Player p, String... msgs)
	{
		for(String msg : msgs)
			p.sendMessage(color(msg));
	}

	public static void inform(Player p, String... msgs)
	{
		sendMessage(p, "", UndertaleUHC.PREFIX);
		for(String msg : msgs)
			sendMessage(p, INFORM_COLOR + msg);
	}

	public static void warn(Player p, String... msgs)
	{
		playSound(p, ActionSound.PING);
		sendMessage(p, "", UndertaleUHC.PREFIX);
		for(String msg : msgs)
			sendMessage(p, WARN_COLOR + "\u26A0 " + msg.toUpperCase() + " !");
	}

	public static void broadcast(String... msgs)
	{
		Bukkit.broadcastMessage(UndertaleUHC.PREFIX);
		for(String msg : msgs)
			Bukkit.broadcastMessage(color(msg));
	}

	public static Block getBlockLookingAt(Player p)
	{
		return getBlockLookingAt(p, Integer.MAX_VALUE);
	}

	public static Block getBlockLookingAt(Player p, int maxDistance)
	{
		return p.getTargetBlock(TRANSPARENT, maxDistance);
	}

	public static void playSound(Player p, ActionSound actionSound)
	{
		playSound(p, actionSound.getSound());
	}

	public static void playSound(Player p, Sound sound)
	{
		p.playSound(p.getLocation(), sound, 1f, 1f);
	}

	public static void clear(Player p)
	{
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
		p.setFoodLevel(20);
		p.setTotalExperience(0);
		for(PotionEffect effect : p.getActivePotionEffects())
			p.removePotionEffect(effect.getType());
	}

	public static void heal(Player p)
	{
		p.setHealth(p.getMaxHealth());
	}

	public static void sendActionBar(Player p, String message)
	{
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
	}

	public static void showScoreBoard(Player p, String title, String... lines)
	{
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("scoreboard", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(color(title));

		int i = lines.length - 1;
		for(String line : lines)
		{
			Score score = obj.getScore(color(line));
			score.setScore(i);
			i--;
		}

		p.setScoreboard(board);
	}

	public static void loadBook(Player p, String author, String title, String... pages)
	{
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();

		meta.setAuthor(author);
		meta.setTitle(title);
		for(int i = 0; i < pages.length; i++)
			pages[i] = color(pages[i]);
		meta.setPages(pages);
		book.setItemMeta(meta);
		((CraftPlayer)p).getHandle().openBook(CraftItemStack.asNMSCopy(book));
	}

	public static void setMetaData(Player p, String key, boolean value)
	{
		p.setMetadata(key, new FixedMetadataValue(UndertaleUHC.getInstance(), value));
	}

	public static boolean getMetaData(Player p, String key)
	{
		return p.hasMetadata(key) && p.getMetadata(key).get(0).asBoolean();
	}

	public static void setMuted(Player p, boolean muted)
	{
		setMetaData(p, "muted", muted);
	}

	public static boolean isMuted(Player p)
	{
		return getMetaData(p, "muted");
	}

	public static void setNoFallDamage(Player p, boolean noFall)
	{
		setMetaData(p, "noFall", noFall);
	}

	public static boolean hasNoFallDamage(Player p)
	{
		return getMetaData(p, "noFall");
	}

	public static void setRank(Player p, Rank r)
	{
		p.setMetadata("rank", new FixedMetadataValue(UndertaleUHC.getInstance(), r.toString()));
	}

	public static Rank getRank(Player p)
	{
		if(p.hasMetadata("rank"))
			return Rank.valueOf(p.getMetadata("rank").get(0).asString());
		return Rank.NONE;
	}

	/******/
	
	public static void info(Player p)
	{
		Role role = GameManager.rolesMap.get(p.getUniqueId());
		
		Player integrity = null;
		for(UUID u : GameManager.playing) if(GameManager.rolesMap.get(u).equals(Role.INTEGRITY)) integrity = Bukkit.getPlayer(u);
		if(GameManager.ally != null && GameManager.ally.equals(p.getUniqueId())) role.setTeam(Team.NEUTRAL);
		
		sendMessage(p,
		"",
		"&9" + LINE_SEPARATOR + "&9\u2764" + LINE_SEPARATOR,
		"&6Vous êtes &o" + role.getName() + "&r&6 !");
		
		switch(role.getTeam())
		{
			case HUMAN:
				sendMessage(p, "&6Vous devez gagner avec les humains !"); break;
			case MONSTER:
				sendMessage(p, "&6Vous devez gagner avec les monstres !"); break;
			case NEUTRAL:
				if(integrity != null && GameManager.ally != null && GameManager.ally.equals(p.getUniqueId()))
					sendMessage(p, "&6Vous devez gagner avec &o" + integrity.getName() + " &r&6 qui est " + getRole(integrity).getName() + " &r&6et votre vie est liee !");
				else if(role.equals(Role.INTEGRITY) && GameManager.ally != null)
				{
					Player allyP = Bukkit.getPlayer(GameManager.ally);
					sendMessage(p, "&6Vous devez gagner avec &o" + allyP.getName() + " &r&6 qui est " + getRole(allyP).getName() + " &r&6et votre vie est liee !");
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
				sendMessage(p, "&6Vous devez choisir un joueur à épargner,", "&6cependant si il meurt vous perdrez force"); break;
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
				sendMessage(p, "&6Vous prennez des dégâts quand vous �tes dans l'eau,", "&6si vous tuez un monstre vous vous transformez en &fMettaton NEO"); break;
			case METTATON_NEO:
				sendMessage(p, "&6Vous prennez toujours des d�g�ts dans l'eau,", "&6mais vous avez R�sistance I"); break;
			case NAPSTABLOOK:
				sendMessage(p,
				"&6Deux fois dans la partie, vous pouvez enlever votre armure",
				"&6Vous obtiendrez Invisibilit� I, Sauts am�lior�s II, Vitesse II et Faiblesse I"); break;
			case PAPYRUS:
				String sansPlayerName = null;
				for(UUID u : GameManager.playing)
					if(GameManager.rolesMap.get(u).equals(Role.SANS) || GameManager.rolesMap.get(u).equals(Role.SANS_GLOWING_EYE))
						sansPlayerName = Bukkit.getPlayer(u).getName();
				sendMessage(p, "&6Vous avez 3 coeurs d'absorption,", "&6et &fSans &6est &o" + sansPlayerName); break;
			case PATIENCE:
				sendMessage(p, "&6Gagne 1 coeur permanent � l'�pisode 4, et 7"); break;
			case PERSEVERANCE:
				sendMessage(p, "&6Vous poss�dez deux vies, sauf si votre meurtrier est un humain"); break;
			case PHOTOSHOP_FLOWEY:
				sendMessage(p, "&6Vous avez Force I et un coeur permanent de plus"); break;
			case SANS:
				String papyrusPlayerName = null;
				for(UUID u : GameManager.playing) if(GameManager.rolesMap.get(u).equals(Role.PAPYRUS)) papyrusPlayerName = Bukkit.getPlayer(u).getName();
				sendMessage(p, "&6Vous activez le mode 'Oeil brillant' quand &fPapyrus &6meurt,", "&6&fPapyrus &6est &o" + papyrusPlayerName); break;
			case SANS_GLOWING_EYE:
				sendMessage(p, "&6Vous avez maintenant le 'Gaster Blaster',", "&6vengez votre fr�re &fPapyrus"); break;
			case TORIEL:
				sendMessage(p, "&6Vous devez 'sympathiser' avec un joueur,", "&6si celui-ci meurt vous perdrez vos effets"); break;
			case UNDYNE:
				sendMessage(p, "&6Vous activez automatiquement le 'mode armure':", "&6R�sistance II quand vous passez en dessous de 2 coeurs"); break;
		}
		sendMessage(p, "&9" + LINE_SEPARATOR + "&9\u2764" + LINE_SEPARATOR);
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

	public static void randomTeleport(Player p, int bounds, int height)
	{
		Random random = new Random();
		int x = random.nextInt(bounds + 1) - bounds/2;
		int z = random.nextInt(bounds + 1) - bounds/2;
		Location loc = new Location(p.getWorld(), x, p.getWorld().getHighestBlockYAt(x, z) + height, z);
		p.teleport(loc);
	}
	
}
