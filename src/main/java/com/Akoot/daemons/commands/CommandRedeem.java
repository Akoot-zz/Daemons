package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CommandRedeem extends Command
{
	public CommandRedeem()
	{
		this.color = ChatColor.GOLD;
		this.name = "redeem";
		this.permission = "daemons.command.redeem";
	}

	@Override
	public void onCommand()
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			if(args.length == 0)
			{
				sendUsage("-<type> <prize>c");
			}
			else if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("-g"))
				{
					if(plugin.getPermissions() != null)
					{
						sendUsage("-g <group>");
					}
					else
					{
						sendError("-g is not a valid tag because it lacks a permissions plugin");
					}
				}
			}
			else if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("-g"))
				{
					if(plugin.getPermissions() != null)
					{
						for(String group: plugin.getPermissions().getGroups())
						{
							if(args[1].equalsIgnoreCase(group))
							{
								if(player.hasPermission(this.permission + ".group." + group))
								{
									plugin.getPermissions().playerAddGroup(player, group);
									sendMessage("Successfully redeemed " + group + " group!");
									return;
								}
								else
								{
									noPermission("redeem " + ChatColor.GOLD + group);
									return;
								}
							}
						}
						sendError("Group not found: " + ChatColor.GRAY + args[1]);
					}
					else
					{
						sendError("-g is not a valid tag because it lacks a permissions plugin");
					}
				}
			}
		}
	}
}