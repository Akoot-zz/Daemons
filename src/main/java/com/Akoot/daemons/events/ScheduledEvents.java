package com.Akoot.daemons.events;

import org.bukkit.Bukkit;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;

public class ScheduledEvents
{
	private Daemons plugin;

	public ScheduledEvents(Daemons instance)
	{
		this.plugin = instance;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> updatePlaytime(), 0L, 1200L);
	}
	
	public void updatePlaytime()
	{
		if(!plugin.getOnlineUsers().isEmpty())
		{
			for(User user: plugin.getOnlineUsers())
				user.setPlaytime(user.getPlaytime() + 1);
		}
	}
}
