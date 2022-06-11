package fr.ziprow.undertaleuhc.helpers;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

public enum Rank
{
	NONE(WHITE),
	VIP(AQUA),
	HOST(GREEN),
	FOUNDER(RED);

	private final ChatColor color;

	Rank(ChatColor color)
	{
		this.color = color;
	}
	
	public ChatColor getColor()
	{
		return color;
	}
	
}
