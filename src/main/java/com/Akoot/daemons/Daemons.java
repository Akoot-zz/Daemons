package com.Akoot.daemons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.Akoot.cthulhu.util.CthFile;
import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;
import com.Akoot.daemons.commands.Commands;
import com.Akoot.daemons.events.EventListener;
import com.Akoot.daemons.events.ScheduledEvents;

public class Daemons extends JavaPlugin
{   
	private Commands commands;
	private EventListener eventHandler;
	private static Daemons instance;
	public List<ChatRoom> chatrooms;
	private List<User> onlineUsers;
	private Logger log;
	
	public ChatRoom globalChatroom;
	public ChatRoom helpChatroom;
	
	private File userDir;
	private File pluginDir;
	private CthFile config;

	@Override
	public void onEnable()
	{
		instance = this;
		log = Bukkit.getLogger();
		commands = new Commands(instance);
		eventHandler = new EventListener(instance);
		chatrooms = new ArrayList<ChatRoom>();
		onlineUsers = new ArrayList<User>();
		pluginDir = this.getDataFolder();
		userDir = new File(pluginDir, "users");
		config = new CthFile(pluginDir, "config.cth");
		new ScheduledEvents(instance);
		
		mkdirs();
		registerChatRooms();
	}

	private void mkdirs()
	{
		pluginDir.mkdir();
		userDir.mkdir();
		if(!config.exists()) config.create();
	}

	public Commands getCommands()
	{
		return commands;
	}
	
	public void log(String msg)
	{
		log.log(Level.INFO, msg);
	}
	
	public void warn(String msg)
	{
		log.log(Level.WARNING, msg);
	}
	
	public void severe(String msg)
	{
		log.log(Level.SEVERE, msg);
	}

	private void registerChatRooms()
	{
		chatrooms.add(globalChatroom = new ChatRoom(ChatType.PUBLIC, "Global", true));
		chatrooms.add(helpChatroom = new ChatRoom(ChatType.PARTY, "Help", true));
	}

	public void registerChatRoom(ChatRoom chatroom)
	{
		chatrooms.add(chatroom);
	}
	
	public void unregisterChatRoom(ChatRoom chatroom)
	{
		if(chatrooms.contains(chatroom)) chatrooms.remove(chatroom);
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
	
	public File getUserDir()
	{
		return userDir;
	}
	
	public File getPluginDir()
	{
		return pluginDir;
	}
	
	public CthFile getConfigFile()
	{
		return config;
	}

	@Override
	public void onDisable()
	{

	}
}
