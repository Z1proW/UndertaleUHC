package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.tasks.DeathTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReviveCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public ReviveCMD(GameManager gameManager) {this.gameManager = gameManager;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || gameManager.isState(GameState.WAITING, GameState.STARTING, GameState.PLAYING, GameState.EP1) || args.length != 0 || DeathTask.revive == true) return true;
		
		Player p = (Player)sender;
		Role role = Utils.getRole(p);
		
		if(!role.equals(Role.KINDNESS)) return true;
		
		DeathTask.revive = true;
		return true;
	}
}
