package com.Akoot.daemons;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.Akoot.daemons.commands.Commands;
import com.Akoot.daemons.events.EventListener;
import com.Akoot.daemons.events.ScheduledEvents;
import com.Akoot.util.CthFileConfiguration;
import com.massivecraft.factions.Factions;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Daemons extends JavaPlugin
{   
	private Commands commands;
	private EventListener eventHandler;
	private static Daemons instance;
	private List<User> onlineUsers;
	private Logger log;

	private File userDir;
	private File pluginDir;
	private CthFileConfiguration config;

	@Override
	public void onEnable()
	{
		instance = this;
		log = Bukkit.getLogger();
		commands = new Commands(instance);
		eventHandler = new EventListener(instance);
		onlineUsers = new ArrayList<User>();
		pluginDir = this.getDataFolder();
		userDir = new File(pluginDir, "users");
		config = new CthFileConfiguration(pluginDir, "config");
		new ScheduledEvents(instance);

		mkdirs();
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
			if(user.getUUID().equals(player.getUniqueId()))
				return user;
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

	public User getUser(UUID uuid)
	{
		for(User user: onlineUsers)
			if(user.getUUID().equals(uuid)) return user;
		return null;
	}

	public OfflineUser getOfflineUser(InetAddress IP)
	{
		for(OfflineUser u: getOfflineUsers())
			if(u.getIP().equalsIgnoreCase(IP.toString()))
				return u;
		return null;
	}

	public List<User> getOnlineUsers()
	{
		return onlineUsers;
	}

	public List<OfflineUser> getOfflineUsers()
	{
		List<OfflineUser> users = new ArrayList<OfflineUser>();
		for(File f: userDir.listFiles())
		{
			if(f.getName().endsWith(".cth"))
			{
				CthFileConfiguration config = new CthFileConfiguration(f.getAbsolutePath());
				users.add(new OfflineUser(UUID.fromString(config.getName().substring(0, config.getName().indexOf(".")))));
			}
		}
		return users;
	}

	public File getUserDir()
	{
		return userDir;
	}

	public File getPluginDir()
	{
		return pluginDir;
	}

	public CthFileConfiguration getConfigFile()
	{
		return config;
	}

	public Factions getFactions()
	{
		return (Factions) this.getServer().getPluginManager().getPlugin("Factions");
	}

	public Vault getVault()
	{
		return (Vault) this.getServer().getPluginManager().getPlugin("Vault");
	}

	public Economy getEconomy()
	{
		if(getVault() != null)
		{
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
			return rsp.getProvider();
		}
		return null;
	}

	public Permission getPermissions()
	{
		if(getVault() != null)
		{
			RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
			return rsp.getProvider();
		}
		return null;
	}

	@Override
	public void onDisable()
	{

	}
}
