package com.Akoot.daemons.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.util.StringUtil;

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
			stop("The server was shut down");
		}
		else if(args.length > 1)
		{	
			String message = StringUtil.getArgFor("-m", args);
			String t = StringUtil.getArgFor("-t", args);
			double time = t.isEmpty() ? 0 : Double.valueOf(t);
			if(message.isEmpty()) message = "The server was shut down";
			if(time > 0)
			{
				String msg = message;
				plugin.getServer().broadcastMessage(color + "The server will be shutting down in " + time + " seconds!");
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> stop(msg), (long) (time * 20.0));
			}
			else stop(message);
			if(StringUtil.hasArgFor("-s", args)) shutdown();
		}
		else sendUsage("[-m <message>] [-t <time in seconds>] [-s]");
	}
	
	private void shutdown()
	{
		sendMessage("at this point, the computer would shut down in 30 seconds...");
	}

	private void stop(String message)
	{
		for(Player p: plugin.getServer().getOnlinePlayers()) p.kickPlayer(message);
		plugin.getServer().shutdown();
	}
}
