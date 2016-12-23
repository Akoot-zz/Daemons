package com.Akoot.daemons.events;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;

public class ScheduledEvents
{
	private Daemons plugin;

	public ScheduledEvents(Daemons instance)
	{
		this.plugin = instance;
	}
	
	public void test()
	{
		plugin.getServer().broadcastMessage("10s has passed.");
	}

	public void updatePlaytime()
	{
		if(plugin.getOnlineUsers().isEmpty())
		{
			for(User user: plugin.getOnlineUsers())
			{
				int playtime = 0;
				playtime = user.getConfig().getInt("playtime");
				user.getConfig().set("playtime", playtime + 1);
			}
		}
	}
}
