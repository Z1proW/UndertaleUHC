package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public InfoCMD(GameManager gameManager) {this.gameManager = gameManager;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || gameManager.isState(GameState.WAITING, GameState.STARTING, GameState.PLAYING, GameState.EP1) || args.length != 0) return true;
		
		Player p = (Player)sender;
		
		Utils.info(p);
		return true;
	}
}
