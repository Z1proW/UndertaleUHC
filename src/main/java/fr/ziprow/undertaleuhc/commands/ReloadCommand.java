package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.entity.Player;

public class ReloadCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		UndertaleUHC.reload();
		Utils.sendMessage(p, UndertaleUHC.PREFIX + "Settings Reloaded");
	}

	@Override
	public String getPermission()
	{
		return "undertaleuhc.admin";
	}

}
