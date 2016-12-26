package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.util.ChatUtil;

import mkremins.fanciful.FancyMessage;

public class CommandRaw extends Command
{
	public CommandRaw()
	{
		this.name = "Raw";
		this.color = ChatColor.BLUE;
		this.permission = "bukkit.command.say";
	}

	protected void sendUsage()
	{
		message = new FancyMessage(color + "Usage: ");
		message.then("/" + name)
		.then(ChatUtil.color("&f<&d-color ChatColor&f|&a-hover \"Hover message\"&f|&b-url url&f|&6-command \"/command subcommand\"&f|&c-suggest \"/command subcommand\"&f>"))
		.suggest("/" + name + "-color GREEN Hello World!")
		.tooltip(color + "Suggest: /" + name + " -color GREEN Hello World!")
		.send(sender);
	}

	@Override
	public void onCommand()
	{
		if(args.length > 0)
		{
			plugin.getServer().broadcastMessage(ChatUtil.color(ChatUtil.toString(args)));
//			List<CommandSender> recipients = new ArrayList<CommandSender>();
//			for(CommandSender player: plugin.getServer().getOnlinePlayers())
//				recipients.add(player);
//			FancyMessage msg = new FancyMessage("");
//			for(int i  = 0; i < args.length; i++)
//			{
//				if(i > 1 && args[i - 1].matches("-(chatcolor|color|cc|col|c|url|link|tip|tooltip|command|cmd|suggest|sug)"))
//				{
//					if(args[i - 2].toLowerCase().matches("-(chatcolor|color|cc)")) msg.color(ChatColor.valueOf(args[i].toUpperCase()));
//					else if(args[i - 1].toLowerCase().matches("-(col|c)")) msg.color(ChatColor.getByChar(args[i]));
//					else if(args[i - 1].toLowerCase().matches("-(url|link)")) msg.link(args[i]);
//					else if(args[i - 2].toLowerCase().matches("-(to|send)"))
//					{
//						String[] playerNames = StringUtil.getArgFor(args[i], StringUtil.substr(args, i)).split(", ");
//						for(CommandSender player: recipients)
//							for(String name: playerNames)
//								if(!player.getName().toLowerCase().matches(name)) recipients.remove(player);
//					}
//				}
//				else msg.then(args[i] + " ");
//			}
//			msg.send(recipients);
		}
		else sendUsage();
	}
}
