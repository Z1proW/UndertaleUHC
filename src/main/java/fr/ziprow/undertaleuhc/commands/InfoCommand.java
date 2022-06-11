package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.entity.Player;

public class InfoCommand implements SubCommand
{

	@Override
	public void onCommand(Player p, String[] args)
	{
		if(gameManager.isState(GameState.WAITING, GameState.STARTING, GameState.PLAYING, GameState.EP1))
			Utils.info(p);
	}

	@Override
	public String getPermission()
	{
		return "undertaleuhc.user";
	}

}
