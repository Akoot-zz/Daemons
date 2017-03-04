package com.Akoot.daemons.events;

import org.bukkit.Bukkit;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;

import net.md_5.bungee.api.ChatColor;

public class ScheduledEvents
{
	private Daemons plugin;

	public ScheduledEvents(Daemons instance)
	{
		this.plugin = instance;

		/* Sechedule playtime updates for all online players every minute*/
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> updatePlaytime(), 0L, 1200L);
	}

	public void updatePlaytime()
	{
		if(!plugin.getOnlineUsers().isEmpty())
		{
			for(User user: plugin.getOnlineUsers())
			{
				if(plugin.getEssentials() == null || (plugin.getEssentials() != null && !plugin.getEssentials().getUser(user.getPlayer()).isAfk()))
				{
					int time = user.getPlaytime();
					user.updatePlaytime();
					if(plugin.getPermissions() != null)
					{
						if(time == 10 || time == 720 || time == 10080)
						{
							user.sendMessage(ChatColor.LIGHT_PURPLE + "You have unlocked a new rank!");
							user.sendMessage("Type " + ChatColor.LIGHT_PURPLE + "/playtime" + ChatColor.WHITE + " to redeem it");
						}
					}
				}
			}
		}
	}
}
