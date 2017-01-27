package com.Akoot.daemons.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Akoot.daemons.Daemons;

public class CustomItem
{
	protected Daemons plugin;
	protected Material material;
	protected String displayName = "undefined";
	protected List<String> lore = new ArrayList<String>();
	protected LivingEntity wielder;
	protected Short durability = 0;
	
	public CustomItem() {}
	
	public void onLeftClick() {}
	
	public void onRightClick() {}
	
	public void onPickup(Event event) {}
	
	public ItemStack getItem()
	{
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		item.setDurability(durability);
		return item;
	}
}
