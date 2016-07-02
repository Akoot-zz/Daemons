package com.Akoot.daemons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;
import com.Akoot.daemons.commands.Commands;
import com.Akoot.daemons.events.EventListener;

public class Daemons extends JavaPlugin
{   
	private Commands commands;
	private EventListener eventHandler;
	private static Daemons instance;
	public List<ChatRoom> chatrooms;
	private List<User> onlineUsers;

	@Override
	public void onEnable()
	{
		instance = this;
		commands = new Commands(instance);
		eventHandler = new EventListener(instance);
		chatrooms = new ArrayList<ChatRoom>();
		onlineUsers = new ArrayList<User>();
		registerChatRooms();
	}

	public Commands getCommands()
	{
		return commands;
	}

	private void registerChatRooms()
	{
		chatrooms.add(new ChatRoom(ChatType.PUBLIC, "Global"));
		chatrooms.add(new ChatRoom(ChatType.PARTY, "Help"));
	}

	public void registerChatRoom(ChatRoom chatroom)
	{
		chatrooms.add(chatroom);
	}

	public ChatRoom getChatRoom(String search)
	{
		for(ChatRoom chatroom: chatrooms)
			if(chatroom.getName().toLowerCase().contains(search.toLowerCase())) return chatroom;
		return null;
	}

	public EventListener getEventHandler()
	{
		return eventHandler;
	}

	public static Daemons getInstance()
	{
		return instance;
	}

	public User getUser(Player player)
	{
		for(User user: onlineUsers)
		{
			if(user.getPlayer() == player)
			{
				return user;
			}
		}
		return null;
	}

	public User getUser(String search)
	{
		for(User user: onlineUsers)
		{
			if(user.getPlayer().getName().toLowerCase().contains(search.toLowerCase()) ||
					user.getPlayer().getDisplayName().toLowerCase().toLowerCase().contains(search.toLowerCase()) ||
					user.getPlayer().getUniqueId().toString().contains(search.toLowerCase()))
			{
				return user;
			}
		}
		return null;
	}

	public List<User> getOnlineUsers()
	{
		return onlineUsers;
	}

	@Override
	public void onDisable()
	{

	}
}
