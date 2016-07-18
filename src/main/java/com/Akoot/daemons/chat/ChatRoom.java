package com.Akoot.daemons.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.Chats.ChatType;

public class ChatRoom
{
	private String displayname;
	private ChatType type;
	private List<User> users;
	private List<User> moderators;
	private User owner;
	private boolean receiveGlobal;
	private String password;
	private ChatColor color;

	public ChatRoom(ChatType type, String displayname, boolean receiveGlobal)
	{
		this.displayname = displayname;
		this.type = type;
		this.users = new ArrayList<User>();
		this.moderators = new ArrayList<User>();
		this.receiveGlobal = receiveGlobal;
		this.password = "";
		switch(type)
		{
		case PUBLIC: color = ChatColor.GREEN;
		case PRIVATE: color = ChatColor.AQUA;
		case PARTY: color = ChatColor.GOLD;
		}
	}

	public void receiveGlobal(boolean receive)
	{
		this.receiveGlobal = receive;
	}

	public boolean receivesGlobalChat()
	{
		return receiveGlobal;
	}

	public String getName()
	{
		return displayname;
	}

	public User getOwner()
	{
		return owner;
	}

	public boolean hasOwner()
	{
		return owner != null;
	}

	public void setOwner(User user)
	{
		this.owner = user;
	}

	public List<User> getModerators()
	{
		return moderators;
	}

	public void addModerator(User user)
	{
		this.moderators.add(user);
	}

	public void demote(User user)
	{
		this.moderators.remove(user);
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void add(User user)
	{
		if(!this.users.contains(user)) this.users.add(user);
	}

	public void remove(User user)
	{
		if(user.ownsChatroom()) disband();
		else if(user.moderatesChatroom()) this.moderators.remove(user);
		this.users.remove(user);

		user.setChatroom(Daemons.getInstance().globalChatroom);
	}

	public void disband()
	{
		for(User user: users)
		{
			if(!user.ownsChatroom()) user.sendMessage(ChatColor.LIGHT_PURPLE + "Your chatroom has been disbanded!");
			user.setChatroom(Daemons.getInstance().globalChatroom);
		}
		Daemons.getInstance().unregisterChatRoom(this);
	}

	public void setPassword(String newPassword)
	{
		this.password = newPassword;
	}

	public boolean isPassword(String pass)
	{
		return pass.equals(this.password);
	}

	public boolean hasUser(User user)
	{
		return this.users.contains(user);
	}

	public void rename(String newName)
	{
		this.displayname = newName;
	}

	public void removeModerator(User u)
	{
		this.moderators.remove(u);
	}

	public String getPassword()
	{
		return password;
	}

	public ChatType getType()
	{
		return type;
	}
	
	public void setType(ChatType type)
	{
		this.type = type;
	}
	
	public boolean hasPassword()
	{
		return password.isEmpty();
	}
	
	public ChatColor getColor()
	{
		return color;
	}

	public void broadcast(String msg)
	{
		for(User user: users)
		{
			user.sendMessage(ChatColor.GRAY + "[" + color + displayname + ChatColor.GRAY + "] " + ChatColor.RESET +  msg);
		}
	}
}
