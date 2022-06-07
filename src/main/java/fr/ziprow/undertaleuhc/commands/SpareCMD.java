package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpareCMD implements CommandExecutor
{
	private GameManager gameManager;
	private boolean hasSpared = false;
	
	public SpareCMD(GameManager gameManager) {this.gameManager = gameManager;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !gameManager.isState(GameState.EP2) || args.length != 1 || hasSpared) return true;
		
		Player p = (Player)sender;
		
		if(!Utils.getRole(p).equals(Role.FRISK)) return true;
		
		Player spared = Bukkit.getPlayer(args[0]);
		
		if(spared == null) return true;
		
		GameManager.spared = spared.getUniqueId();
		Utils.informPlayer(p, "Vous épargnez " + spared.getName());
		hasSpared = true;
		return true;
	}
}
