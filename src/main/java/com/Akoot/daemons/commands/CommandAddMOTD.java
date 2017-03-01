package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.util.ChatUtil;

public class CommandAddMOTD extends Command
{
	public CommandAddMOTD()
	{
		this.color = ChatColor.YELLOW;
		this.name = "AddMOTD";
		this.permission = "daemons.command.setmotd";
	}

	@Override
	public void onCommand()
	{
		if(args.length > 0)
		{
			String[] msg = ChatUtil.toString(args).split("\\|");
			plugin.addMOTD(msg);
			sendMessage("The following messages: ");
			for(String s: msg) sendMessage(ChatColor.RESET + "- " + s);
			sendMessage("were added!");
		}
		else sendUsage("<motd>|[motd2]|[motd3]...");
	}
}
