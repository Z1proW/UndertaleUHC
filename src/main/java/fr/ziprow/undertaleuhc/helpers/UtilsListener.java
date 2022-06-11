package fr.ziprow.undertaleuhc.helpers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UtilsListener implements Listener
{

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();

		if(Utils.isMuted(p))
			e.setCancelled(true);

		Rank rank = Utils.getRank(p);

		if(rank != Rank.NONE)
			e.setFormat(Utils.color("&8[" + rank.getColor() + rank + "&8] &r%s: %s"));
	}

	@EventHandler
	public void onFallDamage(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player
		&& e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
		&& Utils.hasNoFallDamage((Player)e.getEntity()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		e.setJoinMessage(Utils.color("&8[&c+&8] &r&o" + e.getPlayer().getName()));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		e.setQuitMessage(Utils.color("&8[&c-&8] &r&o" + e.getPlayer().getName()));
	}

}
