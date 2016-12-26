package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.util.ChatUtil;

public class CommandSay extends Command
{
	public CommandSay()
	{
		this.name = "Say";
		this.color = ChatColor.AQUA;
		this.permission = "bukkit.command.say";
	}

	@Override
	public void onCommand()
	{
		if(args.length > 0)
			plugin.getServer().broadcastMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "Server" + ChatColor.WHITE + "] " + ChatUtil.color(ChatUtil.toString(args)));
		else sendUsage("<message>");
	}
}
