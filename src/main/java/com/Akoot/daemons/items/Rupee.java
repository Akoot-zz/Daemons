package com.Akoot.daemons.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;

public class Rupee extends CustomItem
{
	public static enum Type
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

	public Rupee()
	{
		this(Type.GREEN);
	}

	public Rupee(Type type)
	{
		this.material = Material.EMERALD;
		this.displayName = type.color + "Rupee";
		this.type = type;
	}

	@Override
	public void onRightClick()
	{
		if(wielder.getType() == EntityType.PLAYER)
		{
			User user = plugin.getUser(wielder.getUniqueId());
			Player player = user.getPlayer();
			int amount = player.getInventory().getItemInMainHand().getAmount();
			player.getInventory().setItemInMainHand(null);
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F), 5L);
			if(plugin.getEconomy() != null) user.pay(type.value * amount);
		}
	}
}
