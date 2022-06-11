package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.entity.Player;

public class StartCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		UndertaleUHC.start();
	}

	@Override
	public String getPermission()
	{
		return "undertaleuhc.admin";
	}

}
