package com.Akoot.daemons.chat;

import java.util.ArrayList;
import java.util.List;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.Chats.ChatType;

import net.md_5.bungee.api.ChatColor;

public class ChatRoom
{
	private String displayname;
	public ChatType type;
	private List<User> users;
	private List<User> moderators;
	private User owner;
	private boolean receiveGlobal;
	private String password;

	public ChatRoom(ChatType type, String displayname, boolean receiveGlobal)
	{
		this.displayname = displayname;
		this.type = type;
		this.users = new ArrayList<User>();
		this.moderators = new ArrayList<User>();
		this.receiveGlobal = receiveGlobal;
		this.password = "";
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
}
