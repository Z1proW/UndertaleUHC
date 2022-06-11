package fr.ziprow.undertaleuhc.commands;

import org.bukkit.entity.Player;

interface SubCommand
{

	// label: args[0]
	// arg1 = args[1] ...
	void onCommand(Player p, String[] args);

	String getPermission();

}
