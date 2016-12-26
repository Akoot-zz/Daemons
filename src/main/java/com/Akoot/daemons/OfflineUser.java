package com.Akoot.daemons;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.CthFileConfiguration;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class OfflineUser
{
	protected Daemons instance;
	protected CthFileConfiguration config;
	protected Permission permission;
	protected Economy economy;
	
	public OfflineUser() {}
	
	public OfflineUser(UUID uniqueID)
	{
		this.instance = Daemons.getInstance();
		if(Daemons.getInstance().getPermissions() != null)
			this.permission = Daemons.getInstance().getPermissions();
		if(Daemons.getInstance().getEconomy() != null)
			this.economy = Daemons.getInstance().getEconomy();
		this.config = new CthFileConfiguration(Daemons.getInstance().getUserDir(), uniqueID.toString());
	}
	
	public String getName()
	{
		return config.getString("username");
	}
	
	public String getDisplayName()
	{
		return ChatUtil.color(config.getString("displayname"));
	}
	
	public void setPlaytime(int playtime)
	{
		this.config.set("playtime", playtime);
	}
	
	public int getPlaytime()
	{
		return this.config.getInt("playtime");
	}
	
	public String getPlaytimeString()
	{
		return ChatUtil.getTime(getPlaytime());
	}
	
	public UUID getUUID()
	{
		return UUID.fromString(config.getName().substring(0, config.getName().indexOf(".")));
	}
	
	public boolean equals(OfflineUser user)
	{
		if(user.getUUID() == this.getUUID()) return true;
		return false;
	}
	
	public boolean isOnline()
	{
		return Daemons.getInstance().getUser(getUUID()) != null;
	}
	
	public User getOnlineUser()
	{
		return Daemons.getInstance().getUser(getUUID());
	}
	
	public CthFileConfiguration getConfig()
	{
		return config;
	}
	
	public String getGroup()
	{
		if(permission != null)
			return permission.getPrimaryGroup("world", getOfflinePlayer());
		else return "";
	}
	
	public OfflinePlayer getOfflinePlayer()
	{
		return Daemons.getInstance().getServer().getOfflinePlayer(getUUID());
	}
	
	public String getIP()
	{
		return config.getString("IP");
	}
}
