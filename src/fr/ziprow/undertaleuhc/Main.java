package fr.ziprow.undertaleuhc;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ziprow.undertaleuhc.commands.ChooseCharaCMD;
import fr.ziprow.undertaleuhc.commands.ChooseFriskCMD;
import fr.ziprow.undertaleuhc.commands.InfoCMD;
import fr.ziprow.undertaleuhc.commands.ItemsCMD;
import fr.ziprow.undertaleuhc.commands.ReviveCMD;
import fr.ziprow.undertaleuhc.commands.RoleCMD;
import fr.ziprow.undertaleuhc.commands.SpareCMD;
import fr.ziprow.undertaleuhc.commands.StartCMD;
import fr.ziprow.undertaleuhc.commands.SympaCMD;
import fr.ziprow.undertaleuhc.enums.Item;

public class Main extends JavaPlugin
{
	private static Main instance;
	private GameManager game;
	private YamlConfiguration itemsConfig;
	
	public static Main get() {return instance;}
	
	public YamlConfiguration getItemsConfig() {return itemsConfig;}
	
	@Override
	public void onEnable()
	{
		instance = this;
		this.game = new GameManager(this);
		saveConfigs();
		getCommands();
		getRecipes();
	}
	
	private void saveConfigs()
	{
		saveDefaultConfig(); // config.yml
		
		// items.yml
		File file = new File(this.getDataFolder(), "items.yml");
		if (!file.exists()) this.saveResource("items.yml", false);
		
		itemsConfig = YamlConfiguration.loadConfiguration(file);
	}
	
	private void getCommands()
	{
		getCommand("start"		).setExecutor(new StartCMD		(game));
		getCommand("info"		).setExecutor(new InfoCMD		(game));
		getCommand("role"		).setExecutor(new RoleCMD		(game));
		getCommand("items"		).setExecutor(new ItemsCMD		(    ));
		getCommand("choosechara").setExecutor(new ChooseCharaCMD(game));
		getCommand("choosefrisk").setExecutor(new ChooseFriskCMD(this, game));
		getCommand("spare"		).setExecutor(new SpareCMD		(game));
		getCommand("revive"		).setExecutor(new ReviveCMD		(game));
		getCommand("sympa"		).setExecutor(new SympaCMD		(game));
	}
	
	private void getRecipes()
	{
		// Trident recipe
		ShapelessRecipe recipe = new ShapelessRecipe(Item.TRIDENT.getItem());
		recipe.addIngredient(Material.DIAMOND_SWORD);
		
		MaterialData[] datas = {Item.SOUL_RED.getItem().getData(), Item.SOUL_CYAN.getItem().getData(), Item.SOUL_ORANGE.getItem().getData(), Item.SOUL_BLUE.getItem().getData(), Item.SOUL_MAGENTA.getItem().getData(), Item.SOUL_GREEN.getItem().getData(), Item.SOUL_YELLOW.getItem().getData(), Item.SOUL_WHITE.getItem().getData(), Item.SOUL_BLACK.getItem().getData()};
		
		for(MaterialData d : datas)
		{
			recipe.addIngredient(d);
			Bukkit.getServer().addRecipe(recipe);
			recipe.removeIngredient(d);
		}
	}
}