package com.Akoot.daemons.commands;

import static com.Akoot.daemons.util.ChatUtil.contains;
import static com.Akoot.daemons.util.ChatUtil.getArgFor;
import static com.Akoot.daemons.util.ChatUtil.hasArgFor;
import static com.Akoot.daemons.util.ChatUtil.replace;
import static com.Akoot.daemons.util.ChatUtil.toProperString;
import static com.Akoot.daemons.util.ChatUtil.toProperStringOr;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;
import com.Akoot.daemons.util.ChatUtil;

import mkremins.fanciful.FancyMessage;

public class CommandChat extends Command
{
	private ChatRoom room;

	public CommandChat()
	{
		this.name = "Chat";
		this.color = ChatColor.GOLD;
	}

	@Override
	public void sendUsage()
	{
		sendUsageKey();
		String command = "/" + name.toLowerCase() + " ";
		String suggest = color + "Suggest: " + ChatColor.RESET;
		message = new FancyMessage("");
		if(user.hasPermission("chat.create"))
		{
			message.then("[Create a chatroom]").color(ChatColor.DARK_GREEN)
			.then("\n")
			.then(command + "create ").suggest(command + "create ").tooltip(suggest + command + "create ").color(ChatColor.GREEN).then("<name>").color(ChatColor.GREEN).tooltip(color + "The name of your new chatroom").then("\n")
			.then("[-type] ").color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Set the type of chatroom", ChatColor.GRAY + "Default: PRIVATE", "" + ChatColor.AQUA + ChatColor.ITALIC + "Can also use -<type>").then("(type)").color(ChatColor.GRAY).style(ChatColor.ITALIC).tooltip(ChatColor.AQUA + "PRIVATE", ChatColor.GOLD + "PARTY", ChatColor.GREEN + "PUBLIC").then("\n")
			.then("[-password] ").color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Set a password.", ChatColor.GRAY + "Default: <none>").then("(password)").color(ChatColor.GRAY).style(ChatColor.ITALIC).tooltip(ChatColor.GRAY + "Any password will do").then("\n")
			.then("[-mods] ").color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Add players as moderators.", ChatColor.GRAY + "Default: <empty>").then("(mods...)").color(ChatColor.GRAY).style(ChatColor.ITALIC).tooltip(ChatColor.GRAY + "player1,player2,player3...").then("\n")
			.then("[-global]").color(ChatColor.GRAY).tooltip(ChatColor.GREEN + "Receive global chat messages?", ChatColor.GRAY + "Default: false");
		}
		if(user.hasPermission("chat.disband-other"))
		{
			message.then("\n")
			.then("[Disband a chatroom]").color(ChatColor.DARK_RED)
			.then("\n")
			.then(command + "disband ").suggest(command + "disband ").tooltip(suggest + command + "disband ").color(ChatColor.RED).then("<name>").color(ChatColor.RED).tooltip(color + "The name of the chatroom");
		}
		message.send(sender);
	}

	@Override
	public void onCommand()
	{
		room = (user != null ? user.getChatroom() : plugin.globalChatroom);
		if(args.length == 0 && sender instanceof Player)
		{
			sendMessage("[Current Chatroom]", ChatColor.LIGHT_PURPLE + "This is already displayed in the scoreboard.");
			showInfo(room, (user.hasPermission("chat.disband-other") || user.ownsChatroom()));
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("rooms") || args[0].equalsIgnoreCase("list"))
			{
				sendMessage("[Open Chatrooms]");
				for(ChatRoom room: plugin.chatrooms) if(room.getType() != ChatType.PRIVATE && !room.hasPassword()) showInfo(room, (user != null && (user.hasPermission("chat.disband-other") || user.ownsChatroom())));
				if(!isPlayer() || (user != null && user.hasPermission("chat.list-all")))
				{
					sendMessage("[Closed Chatrooms]");
					for(ChatRoom room: plugin.chatrooms) if(room.getType() == ChatType.PRIVATE || room.hasPassword()) showInfo(room, true);
				}
				return;
			}
			else if(args[0].equalsIgnoreCase("leave"))
			{
				if(isPlayer()) leave();
				else sendPlayerOnly();
			}
			else if(args[0].equalsIgnoreCase("disband"))
			{
				if(isPlayer())
				{
					if(user.ownsChatroom())
					{
						sendDisbandMessage("");
					}
					else noPermission("disband " + this.room.getName());
				}
				else sendMessage("Pleace specify which chatroom to disband.");
			}
			else sendUsage();
		}
		else if(args.length >= 2)
		{
			if(args[0].equalsIgnoreCase("join"))
			{
				if(isPlayer())
				{
					if((room = plugin.getChatRoom(args[1])) != null)
					{
						if(!room.hasPassword() || contains(room.getPassword(), args)) join(room);
						else sendError(room.getColor() + room.getName() + ChatColor.RED + (args.length == 2 ? " requires a password to enter." : " has a different password."));
					}
					else sendError("Chatroom " + ChatColor.YELLOW + args[1] + color + " does not exist.");
				}
				else sendPlayerOnly();
			}
			else if(args[0].equalsIgnoreCase("create"))
			{
				if(isPlayer() && user.ownsChatroom())
				{
					sendDisbandMessage("and create " + args[1], replace(args, 0, "-create"));
				}
				else create(args[1]);
			}
			else if(args[0].equalsIgnoreCase("disband"))
			{
				if(!isPlayer() || user.hasPermission("chat.disband-other") || user.ownsChatroom())
				{
					if(!contains("-y", args))
					{
						if(hasArgFor("-create", args)) sendDisbandMessage("and create " + getArgFor("-create", args));
						else sendDisbandMessage("");
					}
					else disband();
				}
				else noPermission("disband " + this.room.getName());
			}
			else if(args[0].equalsIgnoreCase("edit"))
			{
				if(!isPlayer()) room = plugin.getChatRoom(args[0]);
				if(room != null)
				{
					if(!isPlayer() || user.ownsChatroom())
					{
						if(contains("-private", args)) room.setType(ChatType.PRIVATE);
						if(contains("-public", args)) room.setType(ChatType.PUBLIC);
						if(contains("-party", args)) room.setType(ChatType.PARTY);

						if(hasArgFor("-name", args)) room.rename(getArgFor("-name", args));
						if(hasArgFor("-global", args)) room.receiveGlobal(Boolean.parseBoolean(getArgFor("-global", args)));
						if(hasArgFor("-pass", args)) room.setPassword(getArgFor("-pass", args));
						if(hasArgFor("-password", args)) room.setPassword(getArgFor("-password", args));
						if(hasArgFor("-type", args)) room.setType(ChatType.valueOf(getArgFor("-pass", args)));
					}
				}
				else sendError("Chatroom " + ChatColor.YELLOW + args[1] + color + " does not exist.");
			}
			else if(args[0].equalsIgnoreCase("kick"))
			{
				if(user.moderatesChatroom())
				{
					User u;
					for(int i = 0; i < args.length; i++)
					{
						if((u = plugin.getUser(args[i])) != null) this.room.remove(u);
					}
					sendMessage("Kicked " + toProperString(args));
				}
				else noPermission("kick " + toProperStringOr(args));
			}
			else if(args[0].equalsIgnoreCase("invite"))
			{
				message = new FancyMessage("You have been invited to join the chatroom ").color(ChatColor.GREEN)
						.then(room.getName()).color(room.getColor())
						.then("\n")
						.then("Click here to join!").color(ChatColor.DARK_AQUA).command("/" + this.name + " join " + room.getName() + " " + room.getPassword());
				User u;
				for(int i = 0; i < args.length; i++)
				{
					if((u = plugin.getUser(args[i])) != null)
					{
						message.send(u.getPlayer());
					}
					else sendPlayerNull(args[i]);
				}
				sendMessage("Invited " + toProperString(args) + " to the chatroom.");
			}
			else if(args[0].equalsIgnoreCase("promote"))
			{
				if(user.ownsChatroom())
				{
					User u;
					for(int i = 0; i < args.length; i++)
					{
						if((u = plugin.getUser(args[i])) != null)
						{
							this.room.addModerator(u);
							this.room.broadcast(user.getDisplayName() + ChatColor.LIGHT_PURPLE + " has left the chatroom!");
						}
						else sendPlayerNull(args[i]);
					}
					sendMessage("Promoting " + toProperString(args) + " to moderator.");
				}
				else noPermission("promote " + toProperStringOr(args));
			}
			else if(args[0].equalsIgnoreCase("demote"))
			{
				if(user.ownsChatroom())
				{
					User u;
					for(int i = 0; i < args.length; i++)
					{
						if((u = plugin.getUser(args[i])) != null) this.room.removeModerator(u);
						else sendPlayerNull(args[i]);
					}
					sendMessage("Demoting " + toProperString(args) + ".");
				}
				else noPermission("demote " + toProperStringOr(args));
			}
			else sendUsage();
		}
		else sendUsage();
	}

	public void join(ChatRoom room)
	{
		if(room != this.room)
		{
			if(!user.ownsChatroom())
			{
				this.room.getUsers().remove(this);
				user.setChatroom(room);
				room.broadcast(user.getDisplayName() + ChatColor.LIGHT_PURPLE + " has joined the chatroom!");
			}
			else sendDisbandMessage("and join " + room.getColor() + room.getName(), "-join", room.getName());
		}
		else sendMessage("You are already in " + room.getColor() + room.getName());
	}

	public void disband()
	{
		String roomName = this.room.getName();
		this.room.disband();
		sendMessage("Successfully disbanded " + roomName + ". RIP");
		if(hasArgFor("-join", args)) user.getPlayer().chat("/" + this.name + " join" + getArgFor("-join", args));
		if(hasArgFor("-create", args)) user.getPlayer().chat("/" + this.name + " create " + getArgFor("-create", args));
	}

	public void create(String name)
	{
		ChatType type = ChatType.PRIVATE;
		boolean receiveGlobal = false;
		String[] mods = null;
		String password = "";

		if(contains("-private", args)) type = ChatType.PRIVATE;
		if(contains("-public", args)) type = ChatType.PUBLIC;
		if(contains("-party", args)) type = ChatType.PARTY;
		if(contains("-global", args)) receiveGlobal = true;
		if(hasArgFor("-type", args)) type = ChatType.valueOf(getArgFor("-type", args).toUpperCase());
		if(hasArgFor("-mods", args)) mods = getArgFor("-mods", args).split(",");
		if(hasArgFor("-pass", args)) password = getArgFor("-pass", args);

		ChatRoom room = new ChatRoom(type, name, receiveGlobal);
		if(!password.isEmpty()) room.setPassword(password);
		if(mods != null)
		{
			for(String mod: mods)
			{
				User u;
				if((u = plugin.getUser(mod)) != null)
				{
					room.addModerator(u);
				}
				else sendPlayerNull(mod);
			}
		}
		plugin.registerChatRoom(room);
		sendMessage("Created " + room.getType() + " chatroom \"" + room.getName() + "\"!");
		if(isPlayer())
		{
			join(room);
			room.setOwner(user);
		}
	}

	public void leave()
	{
		if(user.ownsChatroom()) sendDisbandMessage("");
		else
		{
			ChatRoom room = this.room;
			room.remove(user);
			room.broadcast(user.getDisplayName() + ChatColor.LIGHT_PURPLE + " has left the chatroom!");
		}
	}

	public void sendDisbandMessage(String message, String... disbandArgs)
	{
		String s = message.isEmpty() ? "" : " ";
		sendConfirmMessage("disband " + this.room.getName() + s + message, "/" + this.name + " disband -y" + (disbandArgs != null ? " " + ChatUtil.toString(disbandArgs) : ""));
	}

	public void showInfo(ChatRoom room, boolean admin)
	{
		ChatColor color = room.getType() == ChatType.PARTY ? color = ChatColor.GOLD : ChatColor.GREEN;
		String owner = (room.hasOwner() ? room.getOwner().getDisplayName() : "@CONSOLE");
		String moderators = "";
		String users = "";

		int i = 0;
		for(User user: room.getUsers())
		{
			if(!user.ownsChatroom())
			{
				if(user.moderatesChatroom()) moderators += "  " + user.getDisplayName() + ChatColor.RESET + "\n";
				else
				{
					if(i <= 10) users += "  " + user.getDisplayName() + ChatColor.RESET + "\n";
					i++;
				}
			}
		}
		if(i > 10) users += "  and " + (i - 10) + " more...";
		FancyMessage msg = new FancyMessage(room.getName()).color(color)
				.suggest("/" + this.name + " join " + room.getName())
				.tooltip(
						ChatColor.LIGHT_PURPLE + "Owner: " + ChatColor.RESET + owner,
						ChatColor.DARK_PURPLE + "Moderators: \n" + ChatColor.RESET + (moderators.isEmpty() ? "  <none>" : moderators.substring(0, moderators.length() - 1)),
						ChatColor.GREEN + "Members: \n" + ChatColor.RESET + (users.isEmpty() ? "  <none>" : users.substring(0, users.length() - 1))
						);
		if(admin) msg.then(" [x]").color(ChatColor.RED).tooltip(ChatColor.DARK_RED + "Disband " + room.getName()).command("/" + this.name + " disband -y " + room.getName());
		msg.send(sender);
	}
}
