package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;

public class CommandPlaytime extends Command
{
	public CommandPlaytime()
	{
		this.name = "playtime";
		this.color = ChatColor.LIGHT_PURPLE;
		this.permission = "daemons.command.playtime";
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0 && sender instanceof Player)
			sendPlaytime(user);
		else if(args.length == 1)
		{
			User target = plugin.getUser(args[0]);
			if(target != null)
				sendPlaytime(target);
			else
				sendPlayerNull(args[0]);
		}
		else sendUsage("[player]");
	}

	public void sendPlaytime(User user)
	{
		boolean self = user == this.user;
		int playTime = user.getPlaytime();
		sendMessage((self ? "" : user.getName() + "'s ") + "Playtime: " + ChatColor.WHITE + user.getPlaytimeString());
		if(plugin.getPermissions() != null)
		{
			if(self && !user.isBuilder())
			{
				int time = playTime;
				String msg = color + "Click " + ChatColor.AQUA + "here " + ChatColor.LIGHT_PURPLE + "to become a ";
				if(time >= 10 && time < 720 && !user.getGroup().equalsIgnoreCase("member"))
				{
					if(!user.getPlayer().hasPermission("daemons.command.redeem.group.member")) plugin.getPermissions().playerAdd(user.getPlayer(), "daemons.command.redeem.group.member");
					sendCommand(msg + ChatColor.YELLOW + "Member", ChatColor.GREEN + "Redeem!", "/redeem -g member");
				}
				else if(time >= 720 && time < 10080 && !user.getGroup().equalsIgnoreCase("member+"))
				{
					if(!user.getPlayer().hasPermission("daemons.command.redeem.group.member+")) plugin.getPermissions().playerAdd(user.getPlayer(), "daemons.command.redeem.group.member+");
					sendCommand(msg + ChatColor.GOLD + "Member+", ChatColor.GREEN + "Redeem!", "/redeem -g member+");
				}
				else if(time >= 10080 && !user.getGroup().equalsIgnoreCase("loyalist"))
				{
					if(!user.getPlayer().hasPermission("daemons.command.redeem.group.loyalist"))plugin.getPermissions().playerAdd(user.getPlayer(), "daemons.command.redeem.group.loyalist");
					sendCommand(msg + ChatColor.GREEN + "Loyalist", ChatColor.GREEN + "Redeem!", "/redeem -g loyalist");
				}
			}
		}
	}
}