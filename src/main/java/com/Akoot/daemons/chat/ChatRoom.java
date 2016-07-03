package com.Akoot.daemons.chat;

import java.util.ArrayList;
import java.util.List;

import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.Chats.ChatType;

public class ChatRoom
{
	private String displayname;
	public ChatType type;
	private List<User> users;
	private List<User> moderators;
	private User owner;
	private boolean receiveGlobal;
	
	public ChatRoom(ChatType type, String displayname, boolean receiveGlobal)
	{
		this.displayname = displayname;
		this.type = type;
		this.users = new ArrayList<User>();
		this.moderators = new ArrayList<User>();
		this.receiveGlobal = receiveGlobal;
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
	}
	
	public boolean hasUser(User user)
	{
		return this.users.contains(user);
	}
}
