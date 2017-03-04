package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.OfflineUser;
import com.Akoot.util.StringUtil;

import mkremins.fanciful.FancyMessage;

public class CommandProfile extends Command
{
	private static final String FORBIDDEN_DISPLAY = "IP|list-refreshes|chat-filter";
	private static final String FORBIDDEN_EDIT = "IP|times-joined|list-refreshes|playtime";

	public CommandProfile()
	{
		this.name = "profile";
		this.color = ChatColor.DARK_GREEN;
		this.permission = "daemons.command.profile";
	}

	public void sendProfile(OfflineUser u)
	{
		sendMessage(u.getName() + "'s profile");
		for(String s: u.getConfig().listKeys())
			if(!s.matches(FORBIDDEN_DISPLAY) || user.isOP())
				sendMessage(s + ": " + ChatColor.WHITE + u.getConfig().getString(s));
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0)
		{
			if(user != null) sendProfile(user);
			else sendPlayerOnly();
		}
		else if(args.length == 1)
		{
			if(user != null)
			{
				FancyMessage fm = new FancyMessage("(Click to edit)")
						.style(ChatColor.ITALIC)
						.then("\n")

						.then("Birthday").color(ChatColor.LIGHT_PURPLE)
						.tooltip(ChatColor.GRAY + "Please use this format: mm/dd/yyyy or mm/dd", "  example: 4/20/1893", "  example: 4/20")
						.suggest("/profile set birthday ")
						.then("\n")

						.then("Chat-Filter").color(ChatColor.RED)
						.tooltip(ChatColor.GRAY + "true/false")
						.suggest("/profile set chat-filter ")
						.then("\n")

						.then("Chat-Color").color(ChatColor.AQUA)
						.tooltip(ChatColor.GRAY + "Must be full color name", "  example: GOLD", "  example: LIGHT_PURPLE", "  example: AQUA")
						.suggest("/profile set chat-color ")
						.then("\n")
						;
				fm.send(sender);
			}
			else sendPlayerOnly();
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
						if(!key.matches(FORBIDDEN_EDIT) || !(sender instanceof Player) || user.isOP())
						{
							String value = StringUtil.toString(StringUtil.substr(args, index + 2));
							if(target.getConfig().listKeys().contains(key))
							{
								target.getConfig().set(key, value);
								sendMessage("Changed " + ChatColor.GRAY + key + color + " to: " + ChatColor.WHITE + value);
							}
							else sendError("\"" + args[index + 1] + "\" is not a valid profile key.");
						}
						else sendError("\"" + args[index + 1] + "\" is not an editable profile key.");
					}
				}
				else if(args[index].toLowerCase().matches(StringUtil.toString(target.getConfig().listKeys()).replaceAll(" ", "|")))
					sendMessage(target.getName() + "'s " + args[index].toLowerCase() + ": " + ChatColor.WHITE + target.getConfig().getString(args[index].toLowerCase()));
				else sendUsage("[player] [edit] <key> <new value>");
			}
		}
	}
}
