package com.Akoot.daemons.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.Akoot.daemons.Daemons;

public class CustomItems
{
	private List<CustomItem> items;
	private Daemons plugin;

	public CustomItems(Daemons instance)
	{
		this.plugin = instance;
		this.items = new ArrayList<CustomItem>();
		registerItems();
	}

	public void registerItems()
	{
		/* Weapons */
		items.add(new FinnSword());
		items.add(new MasterSword());

		/* Items */
		for(Rupee.Type type: Rupee.Type.values())
			items.add(new Rupee(type));

		/* Armor */
		items.add(new Uggs());
		
		for(CustomItem item: items)
			item.plugin = plugin;
	}

	public boolean isCustomItem(ItemStack item)
	{
		for(CustomItem i: items)
			if(i.getItem().equals(item)) return true;
		return false;
	}

	public List<CustomArmor> getCustomArmor(ItemStack[] items, LivingEntity wearer)
	{
		List<CustomArmor> customArmors = new ArrayList<CustomArmor>();
		for(ItemStack item: items)
		{
			for(CustomItem i: this.items)
			{
				if(i.getItem().equals(item))
				{
					i.setWeilder(wearer);
					customArmors.add((CustomArmor) i);
				}
			}
		}
		return customArmors;
	}

	public CustomWeapon getCustomWeapon(ItemStack item, LivingEntity weilder)
	{
		for(CustomItem i: items)
		{
			if(i instanceof CustomWeapon)
			{
				if(i.getItem().equals(item))
				{
					i.setWeilder(weilder);
					return (CustomWeapon) i;
				}
			}
		}
		return null;
	}

	public CustomItem getCustomItem(ItemStack item, LivingEntity weilder)
	{
		for(CustomItem i: items)
		{
			if(i.getItem().equals(item))
			{
				i.setWeilder(weilder);
				return i;
			}
		}
		return null;
	}

	public ItemStack getCustomItem(String displayName)
	{
		for(CustomItem i: items)
			if(ChatColor.stripColor(i.displayName).equalsIgnoreCase(displayName))
				return i.getItem();
		return null;
	}
}
