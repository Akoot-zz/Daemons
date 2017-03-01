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
			if(args.length == 1 && args[1].toLowerCase().matches("(-)?(en|dis)able(d)?"))
			{
				boolean enabled = args[1].toLowerCase().startsWith("e");
				if(enabled)
					plugin.getConfigFile().set("multiple-motd", true);
				else
					plugin.getConfigFile().set("multiple-motd", false);
				sendMessage((enabled ? "En" : "Dis") + "abled multiple MOTDs!");
			}
			else
			{
				String msg = ChatUtil.toString(args);
				plugin.setMOTD(msg);
				sendMessage("Server MOTD set to: " + ChatColor.WHITE + ChatUtil.color(msg));
			}
		}
		else sendUsage("<motd>");
	}
}
