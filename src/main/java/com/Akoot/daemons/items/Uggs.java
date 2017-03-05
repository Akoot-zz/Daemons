package com.Akoot.daemons.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Uggs extends CustomArmor
{
	public Uggs()
	{
		this.material = Material.LEATHER_BOOTS;
		this.displayName = ChatColor.BLUE + "Uggs";
		this.lore.add(ChatColor.AQUA + "" + ChatColor.ITALIC + "The boots of battle");
		this.color = Color.fromRGB(0x85abff);
	}

	@Override
	public boolean onAttacked()
	{
		return true;
	}

	@Override
	public boolean onHurt()
	{
		wielder.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5, 1));
		if(!(wielder.getLocation().getBlock().getType() == Material.LAVA || wielder.getLocation().getBlock().getType() == Material.STATIONARY_LAVA))
		{
			wielder.getWorld().playSound(wielder.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.5F, 1.0F);
			wielder.getWorld().playSound(wielder.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1.0F);
		}
		return true;
	}
}
