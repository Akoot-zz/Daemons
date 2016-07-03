package com.Akoot.daemons.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.Chats.ChatType;

import mkremins.fanciful.FancyMessage;

public class EventListener implements Listener
{
	private Daemons plugin;

	public EventListener(Daemons instance)
	{
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public FancyMessage formattedMessage(Player player, String message)
	{
		String name = player.getDisplayName();
		String fRole = "**";
		String fRoleColor = "§a";
		String fName = "416";
		String group = "§aLoyalist";
		return new FancyMessage("")
				.then(fRoleColor + fRole + fName)
					.tooltip(ChatColor.DARK_GREEN + "Click to show the faction...")
					.command("/f f " + fName)
				.then(" ")
				.then("[").color(ChatColor.BLACK)
				.then(group)
				.then("] [").color(ChatColor.BLACK)
				.then(name)
					.tooltip(ChatColor.GREEN + (player.getDisplayName() != player.getName() ? "Real name: " + player.getName() + "\n ": "") + ChatColor.GOLD + "Click to send a message...")
					.suggest("/msg " + player.getName() + " ")
				.then("]").color(ChatColor.BLACK)
				.then(" ")
				.then(message);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		FancyMessage message = formattedMessage(event.getPlayer(), event.getMessage());
		Player player = event.getPlayer();
		User user = plugin.getUser(player);

		plugin.log("[" + player.getName() + ":" + user.getChatroom().getName() + "] " + event.getMessage());
		message.send(player);

		if(user.getChatroom().type != ChatType.PUBLIC)
		{
			for(Player recipent: event.getRecipients())
			{
				if(!plugin.getUser(recipent).getChatroom().receivesGlobalChat())
				{
					if(!user.getChatroom().hasUser(plugin.getUser(recipent))) return;
				}
				message.send(recipent);
			}
		}
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
