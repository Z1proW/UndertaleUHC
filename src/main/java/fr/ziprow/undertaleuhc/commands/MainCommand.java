package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.UndertaleUHC;
import fr.ziprow.undertaleuhc.helpers.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MainCommand implements CommandExecutor
{

	static final Map<String, SubCommand> SUB_COMMANDS = new HashMap<String, SubCommand>()
	{{
		put("help", new HelpCommand());
		put("reload", new ReloadCommand());
		put("start", new StartCommand());
		put("info", new InfoCommand());
		put("role", new RoleCommand());
		put("items", new ItemsCommand());
		put("choose", new ChooseCommand());
		put("spare", new SpareCommand());
		put("revive", new ReviveCommand());
		put("sympa", new SympaCommand());
	}};

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;

			String subCommand = args.length > 0 ? args[0].toLowerCase() : "";

			if(SUB_COMMANDS.containsKey(subCommand))
			{
				SubCommand sub = SUB_COMMANDS.get(subCommand);

				if(p.hasPermission(sub.getPermission()))
					sub.onCommand(p, args);
				else Utils.warn(p, UndertaleUHC.getPhrase("no-permission-message"));
			}
			else Utils.warn(p, UndertaleUHC.getPhrase("not-a-command-message"));
		}
		return true;
	}

}
