package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.Akoot.daemons.util.ChatUtil;

public class CommandRename extends Command
{
	public CommandRename()
	{
		this.name = "Rename";
		this.color = ChatColor.GOLD;
		this.permission = "daemons.command.rename";
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0)
			sendUsage("[player] <item name>");
		else if(args.length >= 1)
		{
			if(user != null)
			{
				Player player = user.getPlayer();
				if((player.getInventory().getItemInMainHand() != null) && (player.getInventory().getItemInMainHand().getType() != Material.AIR))
				{
					String newName = ChatUtil.color("&r" + ChatUtil.toString(args));
					ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
					meta.setDisplayName(newName);
					player.getInventory().getItemInMainHand().setItemMeta(meta);
					sendMessage("Renamed " + ChatColor.WHITE + ChatUtil.itemName(player.getInventory().getItemInMainHand()) + color + " to: " + ChatColor.WHITE + newName);
				}
				else sendError("You need to hold an item first!");
			}
			else sendPlayerOnly();
		}
	}
}
