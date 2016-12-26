package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.OfflineUser;
import com.Akoot.util.StringUtil;

public class CommandProfile extends Command
{
	public CommandProfile()
	{
		this.name = "profile";
		this.color = ChatColor.GREEN;
		this.permission = "daemons.command.profile";
	}

	public void sendProfile(OfflineUser user)
	{
		sendMessage(user.getName() + "'s profile");
		for(String s: user.getConfig().listKeys())
			sendMessage(s + ": " + ChatColor.WHITE + user.getConfig().getString(s));
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0)
			if(user != null) sendProfile(user);
			else
			{
				sendPlayerOnly();
				return;
			}
		else
		{
			OfflineUser target = plugin.getOfflineUser(args[0]);
			int index = 0;
			if(target != null)
				index = 1;
			else
			{
				if(user != null) target = user;
				else
				{
					sendPlayerOnly();
					return;
				}
			}

			if(args.length == 1) sendProfile(target);
			else
			{
				if(args[index].toLowerCase().matches("edit|set|change"))
				{
					if(args.length > index + 1)
					{
						String key = args[index + 1].toLowerCase();
						if(key.matches("displayname|age|gender|censor-chat|chat-color"))
						{
							String value = StringUtil.toString(StringUtil.substr(args, index + 2));
							target.getConfig().set(key, value);
							sendMessage("Successfully changed " + ChatColor.LIGHT_PURPLE + key + color + " to: " + ChatColor.WHITE + value + "!");
						}
						else sendError("\"" + args[index + 1] + "\" is not an editable profile key.");
					}
				}
				else if(args[index].toLowerCase().matches(StringUtil.toString(target.getConfig().listKeys()).replaceAll(" ", "|")))
					sendMessage(target.getName() + "'s " + args[index].toLowerCase() + ": " + ChatColor.WHITE + target.getConfig().getString(args[index].toLowerCase()));
				else sendUsage("[user] [edit] <key> <new value>");
			}
		}
	}
}
