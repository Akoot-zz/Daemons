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
				int time = user.getPlaytime();
				user.updatePlaytime();
				if(time == 10 || time == 720 || time == 10080)
				{
					user.sendMessage(ChatColor.LIGHT_PURPLE + "You have unlocked a new rank!");
					user.sendMessage("Type " + ChatColor.LIGHT_PURPLE + "/playtime" + ChatColor.WHITE + " to redeem it");
					if(time == 10)
						plugin.getPermissions().playerAdd(user.getPlayer(), "deamons.command.redeem.group.member");
					else if(time == 720)
						plugin.getPermissions().playerAdd(user.getPlayer(), "deamons.command.redeem.redeem.group.member+");
					else if(time == 10080)
						plugin.getPermissions().playerAdd(user.getPlayer(), "deamons.command.redeem.redeem.group.loyalist");
				}
			}
		}
	}
}
