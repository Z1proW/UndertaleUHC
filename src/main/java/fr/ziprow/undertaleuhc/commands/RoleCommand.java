package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RoleCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		if(!(gameManager.isState(GameState.WAITING, GameState.WAITING, GameState.PLAYING))) return;

		if(args.length != 2) return;

		Role role = null;

		for(Role r : Role.values()) if(ChatColor.stripColor(r.getName()).equalsIgnoreCase(args[0])) role = r;
		if(role == null) {p.sendMessage("Wrong role name"); return;}
		GameManager.rolesMap.replace(p.getUniqueId(), role);
		Utils.inform(p, "Vous êtes maintenant : " + role.getName());
	}

	@Override
	public String getPermission()
	{
		return "undertaleuhc.admin";
	}

}
