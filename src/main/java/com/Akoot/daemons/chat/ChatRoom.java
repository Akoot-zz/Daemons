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
	
	public ChatRoom(ChatType type, String displayname)
	{
		this.displayname = displayname;
		this.type = type;
		this.users = new ArrayList<User>();
	}
	
	public String getName()
	{
		return displayname;
	}
	
	public List<User> getUsers()
	{
		return users;
	}
	
	public void add(User user)
	{
		this.users.add(user);
	}
	
	public void remove(User user)
	{
		this.users.remove(user);
	}
}
