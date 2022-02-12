package fr.ziprow.undertaleuhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.enums.GameState;

public class StartCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public StartCMD(GameManager gameManager) {this.gameManager = gameManager;}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !gameManager.isState(GameState.WAITING) || !sender.isOp()) return true;
		
		gameManager.setState(GameState.STARTING);
		return true;
	}
}
