package com.Akoot.daemons.items;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.Akoot.daemons.User;

public class Rupee extends CustomItem
{
	public enum Type
	{
		GREEN(ChatColor.GREEN, 1),
		BLUE(ChatColor.BLUE, 5),
		YELLOW(ChatColor.YELLOW, 10),
		RED(ChatColor.DARK_RED, 20),
		PURPLE(ChatColor.DARK_PURPLE, 50),
		ORANGE(ChatColor.GOLD, 100),
		SILVER(ChatColor.GRAY, 200);

		int value;
		ChatColor color;

		Type(ChatColor color, int value)
		{
			this.value = value;
			this.color = color;
		}
	}

	private Type type = Type.GREEN;

	public Rupee(Type type)
	{
		this.displayName = type.color + "" + "Rupee";
		this.type = type;
	}

	@Override
	public void onRightClick()
	{
		if(wielder.getType() == EntityType.PLAYER)
		{
			User user = plugin.getUser(wielder.getUniqueId());
			if(plugin.getEconomy() != null)
				user.pay(type.value);
		}
	}

	@Override
	public void onPickup(PlayerPickupItemEvent event)
	{
		for(Sound sound: getSoundsFor(type.value))
		{
			event.getPlayer().playSound(event.getPlayer().getLocation(), sound, 1.0F, 1.0F);
		}
	}

	public Sound[] getSoundsFor(int value)
	{
		Sound[] sounds = {Sound.BLOCK_NOTE_PLING};
		return sounds;
	}
}
