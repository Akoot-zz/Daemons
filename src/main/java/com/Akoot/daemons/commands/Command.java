package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;

import mkremins.fanciful.FancyMessage;

public class Command
{
	protected Daemons plugin;
	protected String name;
	protected ChatColor color;
	protected String permission = "";
	protected CommandSender sender;
	protected boolean playerOnly = false;
	FancyMessage message = new FancyMessage();
	protected User user;

	public String[] args;

	protected Command(){}

	protected void onCommand(){}

	protected void sendUsage()
	{
		message = new FancyMessage(color + "Usage: ");
		message.then("/" + name)
		.suggest("/" + name)
		.tooltip(color + "Suggest: /" + name)
		.send(sender);
	}

	protected void noPermission(String use)
	{
		sendMessage(ChatColor.DARK_RED + "You do not have permission to " + use);
	}

	protected void sendError(String error)
	{
		sendMessage("&4Error: &c" + error);
	}

	protected void noPermission()
	{
		noPermission("use: " + ChatColor.RED + "/" + name);
	}

	protected void sendPlayerNull(String arg)
	{
		sendMessage("Can't find player: " + ChatColor.GRAY + ChatColor.ITALIC + arg);
	}

	protected void sendMessage(String msg)
	{
		if(sender != null)
		{
			sender.sendMessage(color + msg);
		}
	}

	protected void sendMessage(String msg, String hover)
	{
		message = new FancyMessage(msg);
		message.tooltip(hover)
		.send(sender);
	}

	protected void sendUsage(String msg)
	{
	}

	public String getName()
	{
		return name;
	}
}
