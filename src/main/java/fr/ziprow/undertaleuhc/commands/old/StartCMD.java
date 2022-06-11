package fr.ziprow.undertaleuhc.commands.old;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.enums.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public StartCMD(GameManager gameManager) {this.gameManager = gameManager;}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !sender.isOp()) return true;
		
		if(!gameManager.isState(GameState.WAITING)) {((Player)sender).sendMessage("The game already started"); return true;}
		
		gameManager.setState(GameState.STARTING);
		return true;
	}
}
