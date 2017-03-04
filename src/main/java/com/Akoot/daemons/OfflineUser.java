package com.Akoot.daemons;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.CthFileConfiguration;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class OfflineUser
{
	protected Daemons instance;
	protected CthFileConfiguration config;
	protected Permission permission;
	protected Chat chat;
	protected Economy economy;
	
	public OfflineUser() {}
	
	public OfflineUser(UUID uniqueID)
	{
		this.instance = Daemons.getInstance();
		if(Daemons.getInstance().getPermissions() != null)
			this.permission = Daemons.getInstance().getPermissions();
		if(Daemons.getInstance().getChat() != null)
			this.chat = Daemons.getInstance().getChat();
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
	
	public void updatePlaytime()
	{
		this.config.set("playtime", getPlaytime() + 1);
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
	
	public boolean isBirthday()
	{
		return ChatUtil.getCurrentDate("%d/%d/%d").startsWith(getBirthday());
	}
	
	public String getBirthday()
	{
		return config.getString("birthday");
	}
	
	public void updateRefreshCounter()
	{
		config.set("list-refreshes", refreshCounter() + 1);
	}
	
	public int refreshCounter()
	{
		return config.getInt("list-refreshes");
	}
	
	public void updateJoinCounter()
	{
		config.set("times-joined", refreshCounter() + 1);
	}
	
	public int joinCounter()
	{
		return config.getInt("times-joined");
	}
	
	public void pay(double money)
	{
		economy.depositPlayer(getOfflinePlayer(), money);
	}
}
