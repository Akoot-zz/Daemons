package com.Akoot.daemons.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.OfflineUser;
import com.Akoot.daemons.User;
import com.Akoot.daemons.util.ChatUtil;

import mkremins.fanciful.FancyMessage;

public class EventListener implements Listener
{
	private Daemons plugin;

	public EventListener(Daemons instance)
	{
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public FancyMessage formattedMessage(User user, String message, Player recipient)
	{
		FancyMessage fm = new FancyMessage("");
		String name = user.getDisplayName();
		String fRole = user.getFactionPrefix();
		String fRelColor = user.getRelationTo(recipient.getUniqueId()) + "";
		String fName = user.getFaction();
		String group = user.getGroup();

		if(plugin.getUser(recipient).getConfig().getBoolean("censor-chat"))
			for(String swear: plugin.getConfigFile().getList("swears"))
				if(message.contains(swear)) message = message.replace(swear, ChatUtil.censor(swear));

		/* Faction */
		fm.then(fRelColor + fRole + fName)
		.tooltip(ChatColor.DARK_GREEN + "Click to show the faction...")
		.command("/f f " + fName)
		.then((fName.isEmpty() ? "" : " "));

		/* Group */
		if(!group.isEmpty())
		{
			fm.then("[").color(ChatColor.BLACK)
			.then(group)
			.tooltip(ChatColor.GOLD + "Time on server: " + ChatColor.YELLOW + user.getPlaytimeString())
			.then("]").color(ChatColor.BLACK);
		}

		/* Name */
		fm.then("[").color(ChatColor.BLACK)
		.then(name)
		.tooltip(ChatColor.GREEN + (user.getDisplayName() != user.getName() ? "Real name: " + user.getName() + "\n ": "") + ChatColor.GOLD + "Click to send a message...")
		.suggest("/msg " + user.getName() + " ")
		.then("]").color(ChatColor.BLACK)
		.then(" ")	
		.then(ChatColor.valueOf(user.getConfig().getString("chat-color").toUpperCase()) + ChatUtil.color(message));

		return fm;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		Player player = event.getPlayer();
		plugin.log("[" + player.getName() + "] " + event.getMessage());
		User user = plugin.getUser(player);
		for(Player recipient: event.getRecipients())
			formattedMessage(user, event.getMessage(), recipient).send(recipient);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		User user = new User(event.getPlayer());
		user.getConfig().set("IP", user.getPlayer().getAddress().getAddress().toString());
		user.getConfig().set("username", user.getPlayer().getName());
		plugin.getOnlineUsers().add(user);
	}

	@EventHandler
	public void onPlayerList(ServerListPingEvent event)
	{
		event.setMotd(ChatUtil.color(plugin.getConfigFile().getString("MOTD")));
		OfflineUser user = plugin.getOfflineUser(event.getAddress());
		if(user == null) plugin.getServer().broadcastMessage("Someone new is joining");
		else plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + user.getName() + " is joining");
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		User user = plugin.getUser(event.getPlayer());
		plugin.getOnlineUsers().remove(user);
	}
}
