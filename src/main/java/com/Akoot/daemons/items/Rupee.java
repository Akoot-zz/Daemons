package com.Akoot.daemons.items;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import com.Akoot.daemons.User;

public class Rupee extends CustomItem
{
	public int value = 1;

	public Rupee()
	{
		this.displayName = ChatColor.GREEN + "Rupee";
	}

	@Override
	public void onPickup(Event event)
	{
		if(wielder.getType() == EntityType.PLAYER)
		{
			User user = plugin.getUser(wielder.getUniqueId());
			if(plugin.getEconomy() != null)
			{
				user.pay(value);
			}
		}
	}
}
