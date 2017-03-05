package com.Akoot.daemons.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.StringUtil;

import mkremins.fanciful.FancyMessage;

public class CommandPalette extends Command
{
	public CommandPalette()
	{
		this.name = "commandpalette";
		this.color = ChatColor.DARK_PURPLE;
		this.permission = "daemons.command.comandpalette";
		this.playerOnly = true;
	}

	private void displayPalette()
	{
		List<Command> commands = new ArrayList<Command>();
		for(String s: user.getConfig().listKeys())
		{
			if(s.startsWith("command."))
			{
				commands.add(new Command(s.substring(s.indexOf(".") + 1).replaceAll("-", " "), user.getConfig().getString(s)));
			}
		}

		if(!commands.isEmpty())
		{
			FancyMessage message = new FancyMessage("  [Your Palette]  ").color(ChatColor.WHITE).then("\n---------------").color(ChatColor.GRAY);
			int i = 0;
			int r = plugin.random.nextInt(ChatUtil.rainbowseq.length);
			for(Command command: commands)
			{
				if(r >= ChatUtil.rainbowseq.length) r = 0;
				message.then("\n§" + ChatUtil.rainbowseq[r] + (i + 1) + ". " + command.displayname)
				.tooltip(ChatColor.LIGHT_PURPLE + "Click to execute: " + ChatColor.WHITE + command.command)
				.command(command.command);
				i++;
				r++;
			}
			message.send(sender);
		}
		else sendError("Your command palette is empty!\n Add commands by typing\n /palette add <\"command name\"> as \"/command to execute\"");
	}

	@Override
	public void onCommand()
	{
		if(user != null)
		{
			if(args.length == 0) displayPalette();
			else if(args.length >= 2)
			{
				if(args[0].toLowerCase().matches("add|set|create") && args.length >= 4)
				{
					String displayname = StringUtil.getArgFor(args[0], args);
					String command = StringUtil.getArgFor("as", args);
					user.getConfig().set("command." + displayname.replaceAll(" ", "-"), command);
					sendMessage("Command " + ChatColor.WHITE + command + color + " was set to " + ChatColor.GRAY + displayname + color + "!");
				}
				else if(args[0].toLowerCase().matches("rem(ove)?|del(ete)?"))
				{
					String displayname = StringUtil.getArgFor(args[0], args);
					user.getConfig().remove("command." + displayname.replaceAll(" ", "-"));
					sendMessage("Removed " + ChatColor.GRAY + displayname + color + " from the command palette");
				}
				else sendUsage("set <\"Display name\"> as <\"/command to execute\"");
			}
		}
		else sendPlayerOnly();
	}

	private class Command
	{
		public String displayname, command;

		public Command(String displayname, String command)
		{
			this.displayname = displayname;
			this.command = command;
		}
	}
}
