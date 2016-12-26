package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.util.ChatUtil;

public class CommandSetMOTD extends Command
{
	public CommandSetMOTD()
	{
		this.color = ChatColor.YELLOW;
		this.name = "SetMotd";
		this.permission = "daemons.command.setmotd";
	}

	@Override
	public void onCommand()
	{
		if(args.length > 0)
		{
			String msg = ChatUtil.toString(args);
			plugin.getConfigFile().set("MOTD", ChatUtil.color(msg));
			sendMessage("Server MOTD set to: " + ChatColor.WHITE + ChatUtil.color(msg));
		}
		else sendUsage("<motd>");
	}
}
