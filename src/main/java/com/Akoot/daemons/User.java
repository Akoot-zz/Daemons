package com.Akoot.daemons;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.util.ChatUtil;
import com.massivecraft.factions.entity.MPlayer;

public class User extends OfflineUser
{
	private Player player;
	private MPlayer mplayer;
	
	public User(Player player)
	{
		super(player.getUniqueId());
		this.player = player;
		if(Daemons.getInstance().getFactions() != null)
			mplayer = MPlayer.get(player);
		if(!config.exists())
		{
			config.create();
			config.set("username", player.getName());
			config.set("displayname", player.getDisplayName());
			config.set("playtime", 0);
			config.set("IP", player.getAddress().getAddress().toString());
			config.set("age", "undefined");
			config.set("gender", "undefined");
			config.set("chat-color", "white");
			config.set("censor-chat", true);
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public void sendMessage(String message)
	{
		player.sendMessage(ChatUtil.color(message));
	}

	public boolean hasPermission(String permission)
	{
		return (player.hasPermission(permission) || player.isOp());
	}
	
	public MPlayer getMPlayer()
	{
		return mplayer;
	}
	
	public String getFaction()
	{
		if(mplayer != null)
			return mplayer.getFactionName();
		else return "";
	}
	
	public String getFactionPrefix()
	{
		if(mplayer != null)
			return mplayer.getRole().getPrefix();
		else return "";
	}
	
	public ChatColor getRelationTo(UUID uuid)
	{
		if(mplayer != null)
			return mplayer.getRelationTo(MPlayer.get(uuid)).getColor();
		else return ChatColor.WHITE;
	}
	
	@Override
	public String getGroup()
	{
		if(permission != null)
			return permission.getPrimaryGroup(player);
		else return "";
	}

	public void setDisplayName(String newName)
	{
		config.set("displayname", newName);
	}
}
