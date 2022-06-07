package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player)) return true;

		Player player = (Player)sender;

		if(!sender.hasPermission("template.user"))
		{
			Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions"));
			return true;
		}

		switch(args[0].toLowerCase())
		{
			case "help":
				help(player);
				break;

			case "reload":
				if(sender.hasPermission("template.admin"))
					reload();
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "start":
				if(sender.hasPermission("template.admin"))
					start();
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "info":
				if(sender.hasPermission("template.admin"))
					info(player);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "role":
				if(sender.hasPermission("template.admin"))
					role(player, args);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "items":
				if(sender.hasPermission("template.admin"))
					items(player);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "choosechara":
				if(sender.hasPermission("template.admin"))
					chooseChara(player);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "choosefrisk":
				if(sender.hasPermission("template.admin"))
					chooseFrisk(player);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case spare:
				if(sender.hasPermission("template.admin"))
					spare(player, args);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case revive:
				if(sender.hasPermission("template.admin"))
					revive(player, args);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			case "sympa":
				if(sender.hasPermission("template.admin"))
					sympa(player, args);
				else Utils.warnPlayer(player, UndertaleUHC.getPhrase("no-permissions-message"));
				break;

			default:
				Utils.warnPlayer(player, UndertaleUHC.getPhrase("not-a-command-message"));
				help(player);
				break;
		}

		return true;
	}

	private void help(Player player)
	{
		Utils.sendMessage(player, "Commands",
				ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/template help",
				ChatColor.DARK_PURPLE + "- " + ChatColor.GRAY + "/template reload",
				ChatColor.DARK_PURPLE + "------------------------------");
	}

	private void reload()
	{
		UndertaleUHC.reload();
	}

	private void start()
	{
		UndertaleUHC.start();
	}

	private void info(Player player)
	{
		if(gameManager.isState(GameState.WAITING, GameState.STARTING, GameState.PLAYING, GameState.EP1) && args.length == 0)
			Utils.info(p);
	}

	private void role(Player player, String[] args)
	{
		if(!(gameManager.isState(GameState.WAITING, GameState.WAITING, GameState.PLAYING))) return;

		if(args.length != 1) return;

		Role role = null;

		for(Role r : Role.values()) if(ChatColor.stripColor(r.getName()).equalsIgnoreCase(args[0])) role = r;
		if(role == null) {p.sendMessage("Wrong role name"); return;}
		GameManager.rolesMap.replace(p.getUniqueId(), role);
		Utils.informPlayer(p, "Vous êtes maintenant : " + role.getName());
	}

	private void items(Player player)
	{

	}

	private void chooseChara(Player player)
	{

	}

	private void chooseFrisk(Player player)
	{

	}

	private void spare(Player player, String[] args)
	{

	}

	private void revive(Player player, String[] args)
	{

	}

	private void sympa(Player player, String[] args)
	{

	}

}
