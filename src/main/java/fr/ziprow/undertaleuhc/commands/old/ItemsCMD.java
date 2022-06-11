package fr.ziprow.undertaleuhc.commands.old;

import fr.ziprow.undertaleuhc.enums.Item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsCMD implements CommandExecutor
{
	ItemStack[] items = {Item.SOUL_ORANGE.getItem(), Item.SOUL_RED.getItem(), Item.SOUL_BLACK.getItem(), Item.SOUL_BLUE.getItem(), Item.SOUL_YELLOW.getItem(), Item.SOUL_GREEN.getItem(), Item.SOUL_WHITE.getItem(), Item.SOUL_CYAN.getItem(), Item.SOUL_MAGENTA.getItem(), Item.TRIDENT.getItem(), Item.CHAOS_BUSTER.getItem(), Item.CUPID_BOW.getItem(), Item.DARK_BOW.getItem(), Item.RAPID_BOW.getItem()};
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !sender.isOp()) return true;
		
		Player p = (Player)sender;
		
		for(int i = 0; i < items.length; i++) p.getInventory().setItem(i + 9, items[i]);
		p.updateInventory();
		p.sendMessage("Gave you the items");
		return true;
	}
}
