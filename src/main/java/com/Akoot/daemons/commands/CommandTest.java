package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

public class CommandTest extends Command
{
	public CommandTest()
	{
		this.name = "Test";
		this.color = ChatColor.GREEN;
	}

	@Override
	public void onCommand()
	{
		sendUsage();
		sendMessage("Wew!", "Success mate!");
	}
}
