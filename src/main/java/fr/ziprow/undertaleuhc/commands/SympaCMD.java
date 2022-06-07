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

public class SympaCMD implements CommandExecutor
{
	private GameManager gameManager;
	private static boolean hasSympa = false;
	
	public SympaCMD(GameManager gameManager) {this.gameManager = gameManager;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !gameManager.isState(GameState.EP2) || args.length != 1 || hasSympa) return true;
		
		Player p = (Player)sender;
		Role role = Utils.getRole(p);
		
		if(!role.equals(Role.TORIEL)) return true;
		
		Player sympa = Bukkit.getPlayer(args[0]);
		
		if(sympa == null) return true;
		
		GameManager.sympathized = sympa.getUniqueId();
		Utils.informPlayer(p, "Vous sympatisez avec " + sympa.getName());
		hasSympa = true;
		return true;
	}
}
