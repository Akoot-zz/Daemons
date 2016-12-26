package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.User;
import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.StringUtil;

public class CommandMake extends Command
{
	public CommandMake()
	{
		this.name = "Make";
		this.color = ChatColor.GREEN;
		this.permission = "daemons.command.make";
	}
	
	@Override
	public void onCommand()
	{		
		if(args.length == 0)
			sendUsage("<player> " + ChatUtil.color("<&csay&f|&6do&f> &f<&cmessage&f|&6command&f>"));
		else if(args.length >= 3)
		{
			User target = plugin.getUser(args[0]);
			if(target != null)
			{
				String msg = "";
				for(int i = 2; i < args.length; i++)
					msg += args[i] + " ";
				msg = msg.trim();
				
				if(args[1].equalsIgnoreCase("say"))
					target.getPlayer().chat(msg);
				else if(args[1].equalsIgnoreCase("do"))
				{
					sendMessage("Forcing " + ChatColor.GOLD + target.getName() + this.color + " to execute: " + ChatColor.RESET + "/" + msg);
					target.getPlayer().chat("/" + msg);
				}
				else
					sendError("Can't make " + ChatColor.GRAY + target.getName() + " " + this.color +  args[1] + " " + ChatUtil.toString(StringUtil.substr(args, 2)).trim());
			}
			else
			{
				sendPlayerNull(args[0]);
			}
		}
		else
		{
			sendUsage("<player> " + ChatUtil.color("<&csay&f|&6do&f> &f<&cmessage&f|&6command&f>"));
		}
	}
}
