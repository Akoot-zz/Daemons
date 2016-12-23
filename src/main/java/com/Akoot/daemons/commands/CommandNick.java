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
	}

	@Override
	public void onCommand()
	{
		if(sender instanceof Player)
		{
			if(args.length == 0)
				sendMessage("Your current nickname: " + ChatColor.RESET + user.getPlayer().getCustomName());
			else
			{
				User target = plugin.getUser(args[0]);
				if(target == null)
				{
					String newName = ChatUtil.toString(args);
					user.setDisplayName(ChatUtil.toString(args));
					sendMessage("Changed your nickname to: " + ChatColor.RESET + newName);
				}
				else
				{
					if(args.length >= 2)
					{
						String newName = ChatUtil.toString(ChatUtil.removeFirst(args));
						target.setDisplayName(newName);
						sendMessage("Changed " + target.getName() + "'s nickname to: " + ChatColor.RESET + newName);
					}
					else
						this.sendUsage();
				}
			}
		}
		else sendPlayerOnly();
	}
}
