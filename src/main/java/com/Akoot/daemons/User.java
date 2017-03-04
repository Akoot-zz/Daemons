package com.Akoot.daemons;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Akoot.daemons.util.ChatUtil;
import com.massivecraft.factions.entity.Faction;
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
			config.set("IP", player.getAddress().getAddress().toString());
			config.set("birthday", "undefined");
			config.set("chat-color", "WHITE");
			config.set("chat-filter", true);
			config.set("list-refreshes", 1);
			config.set("times-joined", 1);
			config.set("playtime", 0);
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

	public String getFactionName()
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
	
	public Faction getFaction()
	{
		if(mplayer != null)
		{
			return mplayer.getFaction();
		}
		else return null;
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
	
	public String getPrefix()
	{
		if(chat != null)
			return chat.getPlayerPrefix(player);
		else return "";
	}
	
	public String getSuffix()
	{
		if(chat != null)
			return chat.getPlayerSuffix(player);
		else return "";
	}

	public boolean isOP()
	{
		return player.isOp();
	}

	public boolean isAdmin()
	{
		return player.hasPermission("deamons.group.admin") || isOP();
	}

	public boolean isModerator()
	{
		return player.hasPermission("deamons.group.moderator") || isAdmin();
	}
	
	public boolean isBuilder()
	{
		return player.hasPermission("daemons.group.builder") || isModerator();
	}

	public void setDisplayName(String newName)
	{
		config.set("displayname", newName);
	}

	public boolean give(ItemStack item, double chance)
	{
		if(Math.random() <= chance)
		{
			player.getInventory().addItem(item);
			return true;
		}
		return false;
	}

	public void give(ItemStack item)
	{
		player.getInventory().addItem(item);
	}
}
