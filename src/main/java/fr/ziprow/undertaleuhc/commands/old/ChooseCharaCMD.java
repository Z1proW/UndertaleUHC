package fr.ziprow.undertaleuhc.commands.old;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChooseCharaCMD implements CommandExecutor
{
	private GameManager gameManager;
	
	public ChooseCharaCMD(GameManager gameManager) {this.gameManager = gameManager;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !gameManager.isState(GameState.EP2) || args.length != 0) return true;
		
		Player p = (Player)sender;
		Role role = Utils.getRole(p);
		
		if(!role.equals(Role.DETERMINATION)) return true;
		
		GameManager.rolesMap.replace(p.getUniqueId(), Role.CHARA);
		Role new_role = Utils.getRole(p);
		Utils.info(p);
		new_role.giveStuff(p);
		return true;
	}
}
