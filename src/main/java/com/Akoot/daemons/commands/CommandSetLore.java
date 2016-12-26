package com.Akoot.daemons.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Akoot.daemons.util.ChatUtil;

public class CommandSetLore extends Command
{
	public CommandSetLore()
	{
		this.name = "Setlore";
		this.color = ChatColor.LIGHT_PURPLE;
		this.permission = "daemons.command.rename";
	}

	@Override
	public void onCommand()
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(args.length > 0)
			{
				if((player.getInventory().getItemInMainHand() != null) && (player.getInventory().getItemInMainHand().getType() != Material.AIR))
				{
					ItemStack item = player.getInventory().getItemInMainHand();
					ItemMeta meta = item.getItemMeta();
					
					if(args.length == 1 && args[0].equalsIgnoreCase("none"))
						meta.setLore(null);
					else
					{
						List<String> lore = new ArrayList<String>();
						String msg = ChatUtil.toString(args);
						for(String s: msg.split(", "))
						{
							lore.add(ChatUtil.color("&f" + s).trim());
						}
						meta.setLore(lore);
						item.setItemMeta(meta);
						sendMessage("Added lore:");
						for(String s: lore)
							sendMessage(ChatUtil.color(s));
						meta = player.getInventory().getItemInMainHand().getItemMeta();
					}
				}
				else
				{
					sendError("You need to hold an item first!");
				}
			}
			else
			{
				sendMessage("Usage: " + ChatColor.RESET + "/setlore <lore>");
				sendMessage("Example: " + ChatColor.RESET + "/setlore line1, line2, line3");
			}
		}
	}
}