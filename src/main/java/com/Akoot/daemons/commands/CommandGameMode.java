package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CommandGameMode extends Command
{
	public CommandGameMode()
	{
		this.name = "gamemode";
		this.color = ChatColor.GOLD;
		this.permission = "bukkit.command.gamemode";
	}

	@Override
	public void onCommand()
	{
		if(isPlayer() && args.length <= 1)
		{
			Player player = user.getPlayer();
			if(args.length == 0)
			{
				if(player.getGameMode() == GameMode.CREATIVE) setGameMode(player, GameMode.SURVIVAL);
				else setGameMode(player, GameMode.CREATIVE);
			}
			else if(args.length == 1)
			{
				String arg = args[0].toLowerCase();
				if(arg.matches("0|s(urvival)?")) setGameMode(player, GameMode.SURVIVAL);
				else if(arg.matches("1|c(reative)?")) setGameMode(player, GameMode.CREATIVE);
				else if(arg.matches("2|a(dventure)?")) setGameMode(player, GameMode.ADVENTURE);
				else if(arg.matches("3|sp(ectator)?")) setGameMode(player, GameMode.SPECTATOR);
				else sendError("\"" + arg + "\" is not a valid gamemode");
			}
		}
		else if(args.length >= 1)
		{
			Player player = plugin.getUser(args[0]).getPlayer();
			if(player != null)
			{
				
			}
		}
	}
	
	private void setGameMode(Player player, GameMode gameMode)
	{
		String newGameMode = gameMode.name().toLowerCase();
		String oldGameMode = player.getGameMode().name().toLowerCase();
		player.setGameMode(gameMode);
		sendMessage("Changed " + ChatColor.LIGHT_PURPLE + (player == sender ? "your" : player.getName() + "'s") + color + " gamemode from " + ChatColor.RED + oldGameMode + color + " to " + ChatColor.GREEN + newGameMode);
		if(player.isOp() && player != sender) player.sendMessage(ChatColor.LIGHT_PURPLE + sender.getName() + color + " changed your gamemode from " + ChatColor.RED + oldGameMode + " to " + ChatColor.GREEN + newGameMode);
	}
}
