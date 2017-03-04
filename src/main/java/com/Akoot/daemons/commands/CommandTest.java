package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.util.StringUtil;

public class CommandTest extends Command
{
	public CommandTest()
	{
		this.name = "Test";
		this.color = ChatColor.GREEN;
		this.permission = "daemons.command.test";
	}

	@Override
	public void onCommand()
	{
		sendMessage("Wew!", "Success mate!");
		if(user != null)
		{
			user.getPlayer().getInventory().addItem(plugin.getCustomItems().getCustomItem(StringUtil.toString(args)));
		}
	}
}
