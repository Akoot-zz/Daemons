package com.Akoot.daemons.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.Chats.ChatType;

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
		String msg = event.getFormat().replace("%1$s", player.getDisplayName()).replace("%2$s", message);
		User user = plugin.getUser(player);
		if(user.getChatroom().type != ChatType.PUBLIC)
		{
			for(Player recipent: event.getRecipients())
			{
				if(recipent.isOp() && recipent != player)
				{
					recipent.sendMessage("[" + user.getChatroom().getName() + "]" + msg);
				}
				else
				{
					if(user.getChatroom() == plugin.getUser(recipent).getChatroom())
						recipent.sendMessage(msg);
				}
			}
		}
		System.out.println("[" + user.getChatroom().getName() + "]" + msg);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		plugin.getOnlineUsers().add(new User(player));
		
		plugin.getUser(player).setBoard();
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		User user = plugin.getUser(event.getPlayer());
		
		user.getChatroom().remove(user);
		plugin.getOnlineUsers().remove(user);
	}
}
