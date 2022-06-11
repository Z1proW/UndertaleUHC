package fr.ziprow.undertaleuhc.tasks;

import fr.ziprow.undertaleuhc.GameManager;
import fr.ziprow.undertaleuhc.helpers.Utils;
import fr.ziprow.undertaleuhc.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class EpisodesTask extends BukkitRunnable
{
	private final GameManager game;
	public static int defaultTimer = 20;
	
	private int episode = 0;
	private int eptimer = 0;
	
	public EpisodesTask(GameManager gameManager)
	{
		this.game = gameManager;
	}

	@Override
	public void run()
	{
		if(game.isState(GameState.END)) cancel();
		
		if(eptimer == 0)
		{
			episode++;
			eptimer = defaultTimer;
			Utils.broadcast("&5" + Utils.LINE_SEPARATOR + "&5Episode " + episode + Utils.LINE_SEPARATOR);
			for(Player p : Bukkit.getOnlinePlayers()) p.getWorld().playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
			switch(episode)
			{
				case 1: game.setState(GameState.EP1); break;
				case 2: game.setState(GameState.EP2); break;
				case 3: game.setState(GameState.EP3); break;
				case 4: game.setState(GameState.EP4); break;
				case 5: game.setState(GameState.EP5); break;
				case 6: game.setState(GameState.EP6); break;
				case 7: game.setState(GameState.EP7); break;
				case 8: game.setState(GameState.EP8); break;
				case 9: game.setState(GameState.EP9); break;
			}
		}
		eptimer--;
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			String roleS = "?";
			if(episode > 1 && GameManager.rolesMap.containsKey(p.getUniqueId()))
				roleS = ChatColor.stripColor(GameManager.rolesMap.get(p.getUniqueId()).getName());
			int playerSize = GameManager.playing.size();
			int borderSize = (int) p.getWorld().getWorldBorder().getSize();
			
			createBoard(p, episode, playerSize, roleS, getGroupSize(playerSize), eptimer, borderSize);
		}
	}
	
	private void createBoard(Player p, int episode, int playerSize, String roleS, int groupSize, int timer, int borderSize)
	{
		int min = timer / 60;
		int sec = timer % 60;
		String Smin = (min < 10 ? "0" : "") + min;
		String Ssec = (sec < 10 ? "0" : "") + sec;
		String time = Smin + ":" + Ssec;
		
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective("UNDERTALEUHC", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		obj.setDisplayName		("UNDERTALE UHC");
		Score l7 = obj.getScore	(""); l7.setScore(7);
		Score l6 = obj.getScore	(Utils.color("&8>> &cEpisode: &r" + episode		)); l6.setScore(6);
		Score l5 = obj.getScore	(Utils.color("&8>> &eJoueurs: &r" + playerSize	)); l5.setScore(5);
		Score l4 = obj.getScore (Utils.color("&8>> &aRole: &r"	  + roleS		)); l4.setScore(4);
		Score l3 = obj.getScore	(Utils.color("&8>> &bGroupes: &r" + groupSize	)); l3.setScore(3);
		Score l2 = obj.getScore	(Utils.color("&8>> &9Temps: &r"   + time		)); l2.setScore(2);
		Score l1 = obj.getScore	(Utils.color("&8>> &dBordure: &r" + borderSize	)); l1.setScore(1);
		
		p.setScoreboard(board);
	}
	
	private int getGroupSize(int pSize)
	{
		if(pSize >= 15) return 5;
		if(pSize >= 10) return 4;
		return 3;
	}
}
