package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

class HelpCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		Utils.sendMessage(p, UndertaleUHC.PREFIX + " Commands",
				ChatColor.DARK_PURPLE + Utils.LINE_SEPARATOR);

		for(Map.Entry<String, SubCommand> subCommand : MainCommand.SUB_COMMANDS.entrySet())
			Utils.sendMessage(p, ChatColor.DARK_PURPLE + "- " + UndertaleUHC.MAIN_COLOR + "/undertaleuhc " + ChatColor.GRAY + subCommand.getKey());

		Utils.sendMessage(p, ChatColor.DARK_PURPLE + Utils.LINE_SEPARATOR);
	}

	@Override
	public String getPermission()
	{
		return "undertaleuhc.user";
	}

}
