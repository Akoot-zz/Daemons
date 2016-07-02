package com.Akoot.daemons.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;

import net.md_5.bungee.api.ChatColor;

public class ChatScoreboard
{
	private Scoreboard board;
	private Objective objective;
	private HashMap<User, Score> scores;

	public ChatScoreboard()
	{
		this.board = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = board.registerNewObjective("chat", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		scores = new HashMap<User, Score>();
	}

	public void update()
	{
		for(User user: Daemons.getInstance().onlineUsers)
		{
			objective.setDisplayName(user.getChatroom().getName());
			if(scores.get(user) == null) scores.put(user, objective.getScore(ChatColor.GREEN + "Members: "));
			scores.
			get(user)
			.setScore(
					user
					.getChatroom()
					.getUsers()
					.size());
		}
	}

	public Scoreboard getBoard()
	{
		return board;
	}
}
