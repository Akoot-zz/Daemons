package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;
import com.Akoot.daemons.util.ChatUtil;

public class CommandNick extends Command
{
	public CommandNick()
	{
		this.name = "Nick";
		this.color = ChatColor.AQUA;
		this.permission = "daemons.command.nick";
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0 && sender instanceof Player)
			sendMessage("Your current nickname: " + ChatColor.RESET + user.getDisplayName());
		else
		{
			User target = plugin.getUser(args[0]);
			if(target == null)
			{
				if(user != null)
				{
					String newName = ChatUtil.toString(args);
					user.setDisplayName(ChatUtil.toString(args));
					sendMessage("Changed your nickname to: " + ChatColor.RESET + ChatUtil.color(newName));
				}
				else sendPlayerOnly();
			}
			else
			{
				if(args.length >= 2)
				{
					String newName = ChatUtil.toString(ChatUtil.removeFirst(args));
					target.setDisplayName(newName);
					sendMessage("Changed " + target.getName() + "'s nickname to: " + ChatColor.RESET + ChatUtil.color(newName));
				}
				else this.sendUsage("[player] <nickname>");
			}
		}
	}
}
