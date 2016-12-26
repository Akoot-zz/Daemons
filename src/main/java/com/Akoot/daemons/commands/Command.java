package com.Akoot.daemons.commands;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
	protected FancyMessage message;
	protected User user;

	protected String[] args;

	protected Command(){}

	protected void onCommand(){}

	protected void sendUsage(String subcommand)
	{
		String subcommand2 = subcommand;
		if(subcommand2.contains("<name>|\\[name\\]")) subcommand2.replaceAll("<name>|\\[name\\]", plugin.getOfflineUsers().get(RandomUtils.nextInt(plugin.getOfflineUsers().size())).getName());
		message = new FancyMessage(color + "Usage: ");
		message.then("/" + name.toLowerCase() + " " + subcommand)
		.suggest("/" + name.toLowerCase() + " " + subcommand2)
		.tooltip(color + "Suggest: /" + name.toLowerCase() + ChatColor.WHITE + " " + subcommand)
		.send(sender);
	}

	protected void sendUsageKey()
	{
		message = new FancyMessage(color + "[Available Commands]")
				.tooltip(
						ChatColor.GOLD + "[Key/Legend]",
						ChatColor.RESET + "<text> " + ChatColor.GRAY + ChatColor.ITALIC + "Required subcommand.",
						ChatColor.RESET + "[text] " + ChatColor.GRAY + ChatColor.ITALIC + "Optional subcommand.",
						ChatColor.RESET + "\"<text>\" or \"[text]\" " + ChatColor.GRAY + ChatColor.ITALIC + "Subcommand that allows the use of spaces. MUST contain double quotes (\"\").",
						ChatColor.RESET + "<text...> or [text...] " + ChatColor.GRAY + ChatColor.ITALIC + "Subcommand that allows the use of a list (usually players). MUST contain commas (,) NO spaces.",
						"" + ChatColor.AQUA + ChatColor.ITALIC + "Make sure that you don't include <> or [] in your command!"
						);
		message.send(sender);
	}

	protected void noPermission(String use)
	{
		sendMessage(ChatColor.DARK_RED + "You do not have permission to " + use);
	}

	protected void sendError(String error)
	{
		sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + error);
	}

	protected void noPermission()
	{
		noPermission("use: " + ChatColor.RED + "/" + name.toLowerCase());
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

	protected void sendCommand(String msg, String hover, String command)
	{
		message = new FancyMessage(msg);
		message.tooltip(hover)
		.command(command)
		.send(sender);
	}

	protected boolean hasArgs()
	{
		return args.length > 0;
	}

	protected void sendPlayerOnly()
	{
		sendPlayerOnly(null);
	}

	protected void sendPlayerOnly(String arg)
	{
		sender.sendMessage("Sorry, " + (arg != null ? "subcommand " + arg : "/" + name.toLowerCase()) + " can only be used by players.");
	}

	protected boolean isPlayer()
	{
		return (sender instanceof Player);
	}

	protected String getName()
	{
		return name;
	}

	protected void sendConfirmMessage(String msg, String command)
	{
		sendMessage("Are you sure you want to " + msg + "?");
		message = new FancyMessage("[").color(ChatColor.DARK_GREEN)
				.then("Click here to confirm").color(ChatColor.GREEN).command(command).tooltip("Click to " + msg)
				.then("]").color(ChatColor.DARK_GREEN);
		message.send(sender);
	}
}
