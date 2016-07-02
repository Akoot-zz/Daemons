package com.Akoot.daemons.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Akoot.daemons.User;
import com.Akoot.daemons.Daemons;

public class EventListener implements Listener
{
	private Daemons plugin;

	public EventListener(Daemons instance)
	{
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		String message = event.getMessage();
		Player player = event.getPlayer();
		plugin.getServer().broadcastMessage(player.getName() + ": " + message);
		
		/* Scoreboard */
		plugin.getChatScoreboard().update();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.onlineUsers.add(new User(player));
		for(User user: plugin.offlineUsers) if(user.getPlayer() == player) plugin.offlineUsers.remove(user);
		
		/* Scoreboard */
		plugin.getChatScoreboard().update();
		player.setScoreboard(plugin.getChatScoreboard().getBoard());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		plugin.offlineUsers.add(new User(player));
		for(User user: plugin.onlineUsers) if(user.getPlayer() == player) plugin.onlineUsers.remove(user);
	}
}
