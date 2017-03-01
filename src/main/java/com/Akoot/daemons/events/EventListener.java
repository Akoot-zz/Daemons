package com.Akoot.daemons.events;

import java.net.InetAddress;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.Akoot.daemons.Daemons;
import com.Akoot.daemons.OfflineUser;
import com.Akoot.daemons.User;
import com.Akoot.daemons.items.CustomItem;
import com.Akoot.daemons.items.CustomWeapon;
import com.Akoot.daemons.items.Rupee;
import com.Akoot.daemons.util.ChatUtil;

import mkremins.fanciful.FancyMessage;

public class EventListener implements Listener
{
	private Daemons plugin;
	private InetAddress lastIP;

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
		ChatColor color = ChatColor.valueOf(user.getConfig().getString("chat-color").toUpperCase());

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
			.command("/playtime " + user.getName())
			.then("] ").color(ChatColor.BLACK);
		}

		/* Name */
		fm.then("[").color(ChatColor.BLACK)
		.then(name)
		.tooltip(ChatColor.GREEN + (!user.getDisplayName().equals(user.getName()) ? "Real name: " + ChatColor.WHITE + user.getName() + "\n ": "") + ChatColor.GOLD + "Click to send a message...")
		.suggest("/msg " + user.getName() + " ")
		.then("]").color(ChatColor.BLACK)
		.then(" ");

		/* Message */
		if(plugin.getUser(recipient).getConfig().getBoolean("chat-filter"))
		{
			boolean swore;
			for(String s: message.split(" "))
			{
				swore = false;
				for(String swear: plugin.getConfigFile().getList("swears"))
				{
					if(s.toLowerCase().contains(swear))
					{
						swore = true;
						fm.then(ChatUtil.censor(s) + " ")
						.tooltip(ChatColor.STRIKETHROUGH + s)
						.color(ChatColor.RED);
					}
				}
				if(!swore) fm.then(color + s + " ");
			}
		}
		else
		{
			fm.then(color + ChatUtil.color(message));
		}

		return fm;
	}

	public void giveRandomKit(User user)
	{
		user.give(new Rupee(Rupee.Type.YELLOW).getItem());
		ItemMeta meta;
		ItemStack rareSword = new ItemStack(Material.IRON_SWORD);
		rareSword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
		meta = rareSword.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "");
		rareSword.setItemMeta(meta);

		ItemStack epicSword = new ItemStack(Material.GOLD_SWORD);
		epicSword.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		meta = epicSword.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "");
		epicSword.setItemMeta(meta);

		ItemStack legendarySword = new ItemStack(Material.DIAMOND_SWORD);
		legendarySword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		meta = legendarySword.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "");
		legendarySword.setItemMeta(meta);

		/* Food */
		user.give(new ItemStack(Material.BREAD, 4 + RandomUtils.nextInt(4)), 0.95);
		user.give(new ItemStack(Material.APPLE, 2 + RandomUtils.nextInt(6)), 0.90);
		user.give(new ItemStack(Material.GOLDEN_APPLE), 0.10);
		user.give(new ItemStack(Material.CAKE), 0.25);

		/* Rare Items */
		if(user.give(rareSword, 0.10)) plugin.getServer().broadcastMessage("");
		if(user.give(epicSword, 0.5)) plugin.getServer().broadcastMessage("");
		if(user.give(legendarySword, 0.01)) plugin.getServer().broadcastMessage("");
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
		if(event.getPlayer().hasPlayedBefore())
		{
			giveRandomKit(user);
		}
		if(!user.getName().equals("CrateOfBoxes"))user.getConfig().set("IP", user.getPlayer().getAddress().getAddress().toString());
		user.getConfig().set("username", user.getPlayer().getName());
		plugin.getOnlineUsers().add(user);
	}

	@EventHandler
	public void onPlayerList(ServerListPingEvent event)
	{
		event.setMotd(plugin.getMOTD());
		InetAddress IP = event.getAddress();
		OfflineUser user = plugin.getOfflineUser(IP);
		if(user != null)
		{	
			if(user.isBirthday()) event.setMotd(ChatColor.LIGHT_PURPLE + "Happy Birthday!");
			user.updateRefreshCounter();
		}

		if(!IP.equals(lastIP))
		{
			for(User u: plugin.getOnlineUsers())
				if(u.isModerator())
					u.sendMessage(ChatColor.LIGHT_PURPLE + (user != null ? user.getName() : "Someone new") + " is joining");
			plugin.log((user != null ? user.getName() : "Someone new") + " is joining");
		}
		lastIP = IP;
	}

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event)
	{
		Entity attacker = event.getDamager();
		Entity attacked = event.getEntity();
		if(attacker.getType() == EntityType.PLAYER && attacked instanceof LivingEntity)
		{
			ItemStack item = ((Player)attacker).getInventory().getItemInMainHand();
			CustomWeapon customWeapon = plugin.getCustomItems().getCustomWeapon(item, (LivingEntity) attacker);
			if(customWeapon != null) customWeapon.onAttack((LivingEntity) attacked);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		CustomItem customItem = plugin.getCustomItems().getCustomItem(item, player);
		if(customItem != null)
		{
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
				customItem.onLeftClick();
			else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				customItem.onRightClick();
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		if(entity instanceof LivingEntity)
		{
			LivingEntity killed = (LivingEntity) entity;
			Player killer = killed.getKiller();
			if(killer != null)
			{
				ItemStack item = killer.getInventory().getItemInMainHand();
				CustomWeapon customWeapon = plugin.getCustomItems().getCustomWeapon(item, killer);
				if(customWeapon != null)
					customWeapon.onEntityDeath(event);
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		User user = plugin.getUser(event.getPlayer());
		plugin.getOnlineUsers().remove(user);
	}
}
