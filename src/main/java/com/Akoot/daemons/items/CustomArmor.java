package com.Akoot.daemons.items;

import org.bukkit.Color;
import org.bukkit.Material;

public class CustomArmor extends CustomItem
{
	protected Color color;
	
	public CustomArmor() {}
	
	public boolean onAttacked()
	{
		return false;
	}
	
	public boolean onHurt()
	{
		return false;
	}
	
	protected boolean isLeather()
	{
		return (material == Material.LEATHER_BOOTS || material == Material.LEATHER_CHESTPLATE || material == Material.LEATHER_HELMET || material == Material.LEATHER_LEGGINGS);
	}
}
