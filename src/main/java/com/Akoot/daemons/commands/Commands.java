package com.Akoot.daemons.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Akoot.daemons.Daemons;
import com.Akoot.util.StringUtil;

public class Commands implements CommandExecutor
{
	private Daemons plugin;

	private List<Command> commands;

	public Commands(Daemons instance)
	{
		this.plugin = instance;
		this.commands = new ArrayList<Command>();
		registerCommands();
	}

	public void registerCommands()
	{
		commands.add(new CommandTest());
		commands.add(new CommandNick());
		commands.add(new CommandMake());
		commands.add(new CommandPlaytime());
		commands.add(new CommandRaw());
		commands.add(new CommandSetLore());
		commands.add(new CommandRename());
		commands.add(new CommandSetMOTD());
		commands.add(new CommandSay());
		commands.add(new CommandProfile());
		commands.add(new CommandRedeem());
		commands.add(new CommandShutdown());
		commands.add(new CommandAddMOTD());
		commands.add(new CommandGameMode());

		for(Command cmd: commands)
			plugin.getCommand(cmd.getName()).setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args)
	{
		for(Command command: commands)
		{
			if(cmd.getName().equalsIgnoreCase(command.name))
			{
				command.plugin = plugin;
				command.sender = sender;
				command.args = args;

				if(sender instanceof Player)
				{
					Player player = (Player) sender;
					command.user = plugin.getUser(player);
					if(!command.permission.isEmpty())
					{
						if(!player.hasPermission(command.permission))
						{
							command.noPermission();
							return false;
						}
					}
				}
				else if(sender instanceof BlockCommandSender)
				{
					if(command.playerOnly)
					{
						command.sendPlayerOnly();
						return false;
					}
					BlockCommandSender block  = (BlockCommandSender) sender;
					System.out.println(String.format("Command block at %s,%s,%s issued server command: /%s", block.getBlock().getX(), block.getBlock().getY(), block.getBlock().getZ(), command.name + " " + StringUtil.toString(args)));
				}
				else
				{
					if(command.playerOnly)
					{
						command.sendPlayerOnly();
						return false;
					}
				}
				command.onCommand();
			}
		}
		return false;
	}

	public List<Command> getCommands()
	{
		return commands;
	}
}
