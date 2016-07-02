package com.Akoot.daemons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;
import com.Akoot.daemons.commands.Commands;
import com.Akoot.daemons.events.EventListener;
import com.Akoot.daemons.scoreboard.ChatScoreboard;

public class Daemons extends JavaPlugin
{   
	private Commands commands;
	private EventListener eventHandler;
	private ChatScoreboard chatScoreboard;
	public List<User> onlineUsers;
	public List<User> offlineUsers;
	private static Daemons instance;
	public List<ChatRoom> chatrooms;

	@Override
	public void onEnable()
	{
		instance = this;
		commands = new Commands(instance);
		eventHandler = new EventListener(instance);
		chatScoreboard = new ChatScoreboard();
		onlineUsers = new ArrayList<User>();
		offlineUsers = new ArrayList<User>();
		chatrooms = new ArrayList<ChatRoom>();
		registerChatRooms();
	}

	public Commands getCommands()
	{
		return commands;
	}

	private void registerChatRooms()
	{
		chatrooms.add(new ChatRoom(ChatType.GLOBAL, "Global"));
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

	public ChatScoreboard getChatScoreboard()
	{
		return chatScoreboard;
	}

	public EventListener getEventHandler()
	{
		return eventHandler;
	}

	public static Daemons getInstance()
	{
		return instance;
	}

	public User getOnlineUser(String search)
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

	@Override
	public void onDisable()
	{

	}
}
