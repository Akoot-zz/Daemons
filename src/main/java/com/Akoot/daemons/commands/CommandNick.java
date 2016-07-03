package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandNick extends Command
{
	public CommandNick()
	{
		this.name = "Nick";
		this.color = ChatColor.AQUA;
	}

	@Override
	public void onCommand()
	{
		if(!hasArgs() && sender instanceof Player)
		{
			sendMessage("Your current nickname: " + ChatColor.RESET + user.getPlayer().getCustomName());
		}
		else sendPlayerOnly();
	}
}
