package fr.ziprow.undertaleuhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;

public class RoleCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public RoleCMD(GameManager gameManager) {this.gameManager = gameManager;}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || gameManager.isState(GameState.WAITING, GameState.WAITING, GameState.PLAYING)) return true;
		
		if(args.length != 1) return false;
		
		Player p = (Player)sender;
		Role role = null;
		
		for(Role r : Role.values()) if(ChatColor.stripColor(r.getName()).equalsIgnoreCase(args[0])) role = r;
		if(role == null) {p.sendMessage("Wrong role name"); return true;}
		GameManager.rolesMap.replace(p.getUniqueId(), role);
		Utils.informPlayer(p, "Vous êtes maintenant : " + role.getName());
		return true;
	}
}
