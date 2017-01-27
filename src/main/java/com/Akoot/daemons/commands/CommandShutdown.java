package com.Akoot.daemons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandShutdown extends Command
{
	public CommandShutdown()
	{
		this.name = "shutdown";
		this.color = ChatColor.RED;
		this.permission = "daemons.command.shutdown";
	}
	
	@Override
	public void onCommand()
	{
		if(args.length == 0)
		{
			shutdown("The server was shut down");
		}
		else if(args.length > 1)
		{
			if(args[0].equals("-t"))
			{
				String message = "The server was shut down";
				double time  = Double.valueOf(args[1]);
				plugin.getServer().broadcastMessage(color + "The server will be shutting down in " + time + " seconds!");
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> shutdown(message), (long) (time * 20.0));
			}
		}
		else sendUsage("[-t] [time in seconds]");
	}
	
	private void shutdown(String message)
	{
		for(Player p: plugin.getServer().getOnlinePlayers()) p.kickPlayer(message);
		plugin.getServer().shutdown();
	}
}
