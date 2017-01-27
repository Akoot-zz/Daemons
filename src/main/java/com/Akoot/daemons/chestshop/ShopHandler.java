package com.Akoot.daemons.chestshop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.CthFileConfiguration;

public class ShopHandler implements Listener
{
	public Daemons plugin;
	public File shopsDir;
	
	public enum ShopType
	{
		BUY, SELL, BOTH, BARTER
	}
	
	public ShopHandler(Daemons instance)
	{
		this.plugin = instance;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.shopsDir = new File(plugin.getDataFolder(), "shops");
		this.shopsDir.mkdirs();
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(event.getClickedBlock().getType() == Material.WALL_SIGN)
			{
				//TODO: if(plugin.shopHandler.getShopAt(pos).isOwner(player)) edit it
				//TODO: else try to buy
			}
		}
	}

	@EventHandler
	public void signDetachCheck(BlockPhysicsEvent event)
	{
		Block b = event.getBlock();
		if (b.getType() == Material.WALL_SIGN)
		{
			org.bukkit.material.Sign s = (org.bukkit.material.Sign) b.getState().getData();
			Sign sign = (Sign) b.getState();
			String[] lines = sign.getLines();
			Block attachedBlock = b.getRelative(s.getAttachedFace());
			if (attachedBlock.getType() == Material.AIR) // or maybe any non-solid material, but AIR is the normal case
			{  
				if(isShop(sign))
					event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onExplosion(EntityExplodeEvent event)
	{
		for(int i = 0; i < event.blockList().size(); i++)
		{
			if(event.blockList().get(i).getType() == Material.WALL_SIGN)
			{
				if(((Sign) (event.blockList().get(i).getState())).getLines()[0].equalsIgnoreCase(ChatUtil.color(plugin.getConfigFile().getString("shop-header")))) //TODO: plugin.shopHandler.isShop(sign)
				{
					event.blockList().remove(i);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(event.getBlock().getType() == Material.WALL_SIGN)
		{
			Sign sign = (Sign) event.getBlock().getState();
			String[] lines = sign.getLines();
			if(lines[0].equalsIgnoreCase(ChatUtil.color(plugin.getConfigFile().getString("shop-header")))) //TODO: plugin.shopHandler.isShop(sign)
			{
				event.getPlayer().sendMessage(ChatColor.RED + "Hey there... That there is vandalism...");
				event.getPlayer().sendMessage(ChatColor.GRAY + "Imagine how " + ChatColor.LIGHT_PURPLE + lines[3] + ChatColor.GRAY + " would feel...");
				event.setCancelled(true);
			}
		}
	}
	
	public List<CthFileConfiguration> getShops()
	{
		List<CthFileConfiguration> shops = new ArrayList<CthFileConfiguration>();
		for(File f: shopsDir.listFiles())
				shops.add(new CthFileConfiguration(f.getAbsolutePath()));
		return shops;
	}
	
	public CthFileConfiguration getShop(UUID id)
	{
		for(File f: shopsDir.listFiles())
		{
			if(f.getName().startsWith(id.toString()))
				return new CthFileConfiguration(f.getAbsolutePath());
		}
		return null;
	}
	
	public CthFileConfiguration getShop(Location location)
	{
		for(CthFileConfiguration shop: getShops())
			if(shop.getString("location").equals(location.toString())) return shop;
		return null;
	}
	
	public boolean isShop(Sign sign)
	{
		return getShop(sign.getLocation()) != null;
	}
}
