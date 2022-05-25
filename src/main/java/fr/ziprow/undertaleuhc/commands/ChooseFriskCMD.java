package fr.ziprow.undertaleuhc.commands;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.Main;
import fr.ziprow.undertaleuhc.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import fr.ziprow.undertaleuhc.enums.Role;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class ChooseFriskCMD implements CommandExecutor
{
	private Main main;
	private GameManager gameManager;
	
	public ChooseFriskCMD(Main main, GameManager gameManager)
	{
		this.main = main;
		this.gameManager = gameManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!(sender instanceof Player) || !gameManager.isState(GameState.EP2) || args.length != 0) return true;
		
		Player p = (Player)sender;
		Role role = Utils.getRole(p);
		
		if(!role.equals(Role.DETERMINATION)) return true;
		
		GameManager.rolesMap.replace(p.getUniqueId(), Role.FRISK);
		Role new_role = Utils.getRole(p);
		Utils.info(p);
		new_role.giveStuff(p);
		
		// Envoyer msg de épargner
		p.sendMessage("");
		for(UUID u : GameManager.playing)
		{
			Player player = Bukkit.getPlayer(u);
			if(!GameManager.rolesMap.get(u).equals(Role.FRISK))
			{
				TextComponent spareMsg = new TextComponent(Utils.color("&6Epargner &2" + player.getName()));
				spareMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&2" + player.getName())).create()));
				spareMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spare " + player.getName()));
				p.spigot().sendMessage(spareMsg);
			}
		}
		
		Bukkit.getScheduler().runTaskLater(main, new Runnable()
		{
			@Override
		    public void run()
		    {
				if(GameManager.spared != null) return;
		    	Utils.informPlayer(p, "Vous avez pris trop longtemps pour épargner un joueur, il a été choisi aléatoirement");
		    	Player spared;
		    	do spared = Bukkit.getPlayer(GameManager.playing.get(new Random().nextInt(GameManager.playing.size())));
		    	while(spared.equals(p));
		    	p.performCommand("spare " + spared.getName());
			}
		}, 20 * 30);
		
		return true;
	}
}
