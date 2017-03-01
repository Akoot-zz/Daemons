package com.Akoot.daemons;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import com.Akoot.daemons.items.CustomItems;
import com.Akoot.daemons.util.ChatUtil;
import com.Akoot.util.CthFileConfiguration;
import com.massivecraft.factions.Factions;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Daemons extends JavaPlugin
{   
	private Commands commands;
	private CustomItems items;
	private ScheduledEvents scheduler;
	private EventListener eventHandler;
	private static Daemons instance;
	private List<User> onlineUsers;
	private Logger log;

	private File userDir;
	private File pluginDir;
	private CthFileConfiguration config;
	
	public Random random;

	@Override
	public void onEnable()
	{
		instance = this;
		random = new Random();
		log = Bukkit.getLogger();
		commands = new Commands(instance);
		items = new CustomItems(instance);
		eventHandler = new EventListener(instance);
		onlineUsers = new ArrayList<User>();
		pluginDir = this.getDataFolder();
		userDir = new File(pluginDir, "users");
		config = new CthFileConfiguration(pluginDir, "config");
		scheduler = new ScheduledEvents(instance);

		mkdirs();
	}

	private void mkdirs()
	{
		pluginDir.mkdir();
		userDir.mkdir();
		if(!config.exists())
		{
			config.create();
			config.set("multiple-motd", true);
			config.set("motd", "&2A Minecraft server", "&aA Minecraft server");
			config.set("shop-header", "&lShop");
			config.set("swears", "poop", "pee", "darn");
		}
	}

	public Commands getCommands()
	{
		return commands;
	}
	
	public String getMOTD()
	{
		List<String> MOTDs = config.getList("motd");
		if(MOTDs.isEmpty()) MOTDs.add("A Minecraft Server");
		String MOTD = MOTDs.get(MOTDs.size() - 1);
		if(config.getBoolean("multiple-motd"))
			MOTD = MOTDs.get(random.nextInt(MOTDs.size()));
		return ChatUtil.color(MOTD);
	}
	
	public void addMOTD(String... MOTDs)
	{
		List<String> motd = config.getList("motd");
		for(String s: MOTDs) motd.add(s);
		config.set("motd", motd);
		config.set("multiple-motd", true);
	}
	
	public void setMOTD(List<String> MOTD)
	{
		config.setList("motd", MOTD);
		config.set("multiple-motd", true);
	}
	
	public void setMOTD(String MOTD)
	{
		List<String> motd = config.getList("motd");
		motd.add(MOTD);
		config.setList("motd", motd);
		config.set("multiple-motd", false);
	}
	
	public CustomItems getCustomItems()
	{
		return items;
	}
	
	public ScheduledEvents getScheduler()
	{
		return scheduler;
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
			if(user.getName().toLowerCase().contains(search.toLowerCase()) ||
					user.getDisplayName().toLowerCase().toLowerCase().contains(search.toLowerCase()) ||
					user.getUUID().toString().equals(search.toLowerCase()))
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

	public OfflineUser getOfflineUser(String search)
	{
		for(OfflineUser u: getOfflineUsers())
			if(u.getName().toLowerCase().contains(search.toLowerCase()) ||
					u.getDisplayName().toLowerCase().contains(search.toLowerCase()) ||
					u.getUUID().toString().toLowerCase().equals(search.toLowerCase()))
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
