package fr.ziprow.undertaleuhc;

import fr.ziprow.undertaleuhc.commands.*;
import fr.ziprow.undertaleuhc.enums.Item;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.helpers.UtilsListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UndertaleUHC extends JavaPlugin
{

	public static final String MAIN_COLOR = "&c";
	public static final String PREFIX = Utils.color("&8[" + MAIN_COLOR + "UndertaleUHC&8] &r");

	private static UndertaleUHC instance;

	private static final Map<String, String> PHRASES = new HashMap<>();

	@Override
	public void onEnable()
	{
		instance = this;
		new GameManager();
		saveConfigs();
		getCommands();
		getRecipes();

		new UtilsListener();
	}
	
	private void saveConfigs()
	{
		// config.yml
		saveDefaultConfig();

		// lang.yml
		saveResource("english.yml", true);

		String lang = getConfig().getString("language") + ".yml";
		YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), lang));

		for(String phrase : langConfig.getKeys(false))
			PHRASES.put(phrase, langConfig.getString(phrase));
		
		// items.yml
		File file = new File(this.getDataFolder(), "items.yml");
		if (!file.exists()) this.saveResource("items.yml", false);
	}
	
	private void getCommands()
	{
		getCommand("undertaleuhc").setExecutor(new MainCommand());
		getCommand("undertaleuhc").setTabCompleter(new TabComplete());
		/*
		getCommand("start").setExecutor(new StartCMD(game));
		getCommand("info").setExecutor(new InfoCMD(game));
		getCommand("role").setExecutor(new RoleCMD(game));
		getCommand("items").setExecutor(new ItemsCMD());
		getCommand("choosechara").setExecutor(new ChooseCharaCMD(game));
		getCommand("choosefrisk").setExecutor(new ChooseFriskCMD(this, game));
		getCommand("spare").setExecutor(new SpareCMD(game));
		getCommand("revive").setExecutor(new ReviveCMD(game));
		getCommand("sympa").setExecutor(new SympaCMD(game));*/
	}
	
	private void getRecipes()
	{
		// Trident recipe
		ShapelessRecipe recipe = new ShapelessRecipe(Objects.requireNonNull(Item.TRIDENT.getItem()));
		recipe.addIngredient(Material.DIAMOND_SWORD);
		
		MaterialData[] datas = {Objects.requireNonNull(Item.SOUL_RED.getItem()).getData(), Objects.requireNonNull(Item.SOUL_CYAN.getItem()).getData(), Objects.requireNonNull(Item.SOUL_ORANGE.getItem()).getData(), Objects.requireNonNull(Item.SOUL_BLUE.getItem()).getData(), Objects.requireNonNull(Item.SOUL_MAGENTA.getItem()).getData(), Objects.requireNonNull(Item.SOUL_GREEN.getItem()).getData(), Objects.requireNonNull(Item.SOUL_YELLOW.getItem()).getData(), Objects.requireNonNull(Item.SOUL_WHITE.getItem()).getData(), Objects.requireNonNull(Item.SOUL_BLACK.getItem()).getData()};
		
		for(MaterialData d : datas)
		{
			recipe.addIngredient(d);
			Bukkit.getServer().addRecipe(recipe);
			recipe.removeIngredient(d);
		}
	}

	public static void start()
	{
		// TODO
	}

	public static void reload()
	{
		instance.reloadConfig();
		// TODO

		Bukkit.getLogger().info(ChatColor.stripColor(PREFIX) + "Settings Reloaded");
	}

	public static UndertaleUHC getInstance()
	{
		return instance;
	}

	public static String getPhrase(String key)
	{
		return PHRASES.get(key);
	}

}