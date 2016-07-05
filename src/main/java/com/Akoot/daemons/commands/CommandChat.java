package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;
import com.Akoot.daemons.util.ChatUtil;

import mkremins.fanciful.FancyMessage;

public class CommandChat extends Command
{
	public CommandChat()
	{
		this.name = "Chat";
		this.color = ChatColor.GOLD;
	}

	public void join(ChatRoom room)
	{
		if(room != user.getChatroom())
		{
			if(!user.ownsChatroom())
			{
				user.getChatroom().getUsers().remove(this);
				user.setChatroom(room);
				sendMessage("You joined " + room.getName());
			}
			else sendDisbandMessage("and join " + room.getName(), "-join", room.getName());
		}
		else sendMessage("You are already in that room!");
	}

	public void disband(String... disbandArgs)
	{
		String roomName = user.getChatroom().getName();
		user.getChatroom().disband();
		sendMessage("Successfully disbanded " + roomName + ". RIP");
		for(int i = 0; i < disbandArgs.length; i++)
		{
			if(i + 1 < disbandArgs.length)
			{
				if(disbandArgs[i].equalsIgnoreCase("-join")) user.getPlayer().chat("/chat " + disbandArgs[i + 1]);
				if(disbandArgs[i].equalsIgnoreCase("-create")) user.getPlayer().chat("/chat create" + disbandArgs[i + 1]);
			}
		}
	}

	public void create(String name, String... createArgs)
	{
		ChatType type = ChatType.PRIVATE;
		boolean receiveGlobal = false;
		String[] mods = null;
		String password = "";

		for(int i = 0; i < createArgs.length; i++)
		{
			if(createArgs[i].equalsIgnoreCase("-global")) receiveGlobal = true;
			if(i + 1 < createArgs.length)
			{
				if(createArgs[i].equalsIgnoreCase("-type")) type = ChatType.valueOf(createArgs[i + 1].toUpperCase());
				if(createArgs[i].equalsIgnoreCase("-mods")) mods = createArgs[i + 1].split(",");
				if(createArgs[i].equalsIgnoreCase("-pass")) password = createArgs[i + 1];
			}
		}

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
		sendMessage("Created " + room.type + " chatroom \"" + room.getName() + "\"!");
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
			user.getChatroom().remove(user);
			sendMessage("You have left the chatroom.");
		}
	}

	public void sendDisbandMessage(String message, String... disbandArgs)
	{
		String s = message.isEmpty() ? "" : " ";
		sendMessage("Are you sure you want to disband your chatroom" + s + message + s + "?");
		sendConfirmMessage("/chat disband -y" + (disbandArgs != null ? " " + ChatUtil.toString(disbandArgs) : ""));
	}

	@Override
	public void onCommand()
	{
		if(args.length == 0 && sender instanceof Player)
		{
			sendMessage("[Current Chatroom]", ChatColor.LIGHT_PURPLE + "This is already displayed in the scoreboard.");
			showInfo(user.getChatroom());
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("rooms") || args[0].equalsIgnoreCase("list"))
			{
				sendMessage("[Open Chatrooms]");
				for(ChatRoom room: plugin.chatrooms) if(room.type != ChatType.PRIVATE) showInfo(room);
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
					else sendMessage("You are not the owner of this chatroom.");
				}
				else sendMessage("Pleace specify which chatroom to disband.");
			}

			if(isPlayer())
			{
				ChatRoom room = user.getChatroom();
				if((room = plugin.getChatRoom(args[0])) != null)
				{
					join(room);
				}
				else sendError("Chatroom " + args[0] + " does not exist.");
			}
			else sendPlayerOnly();
		}
		else if(args.length >= 2)
		{
			String name = args[1];
			if(args[0].equalsIgnoreCase("create"))
			{
				if(isPlayer() && user.ownsChatroom())
				{
					sendDisbandMessage("and create " + name, ChatUtil.replace(args, 0, "-create"));
				}
				else create(name, ChatUtil.substr(args, 2));
			}
			else if(args[0].equalsIgnoreCase("disband"))
			{
				if(!isPlayer() || user.ownsChatroom())
				{
					sendDisbandMessage("and create " + name, ChatUtil.replace(args, 0, "-create"));
				}
				else sendError("You do not have permission to disband the chatroom.");
			}
			else if(args[0].equalsIgnoreCase("edit"))
			{
				ChatRoom room = user.getChatroom();
				for(int i = 0; i < args.length; i++)
				{
					if(args[i].equalsIgnoreCase("-private")) room.type = ChatType.PRIVATE;
					if(i + 1 < args.length)
					{
						if(args[i].equalsIgnoreCase("-pass")) room.setPassword(args[i + 1]);
					}
				}
			}
			else if(args[0].equalsIgnoreCase("kick"))
			{
				sendMessage("Kicked " + ChatUtil.toProperString(args));
				User u;
				for(int i = 0; i < args.length; i++)
				{
					if((u = plugin.getUser(args[i])) != null) user.getChatroom().add(u);
				}
			}
			else if(args[0].equalsIgnoreCase("invite"))
			{
				sendMessage("Invited " + ChatUtil.toProperString(args) + " to the chatroom.");
				ChatRoom room = user.getChatroom();
				for(int i = 0; i < args.length; i++)
				{
					User u;
					if((u = plugin.getUser(args[i])) != null)
					{
						room.add(u);
					}
					else sendPlayerNull(args[i]);
					if(i + 1 < args.length)
					{
						if((u = plugin.getUser(args[i + 1])) != null)
						{
							if(args[i].equalsIgnoreCase("-mod")) room.addModerator(u);
						}
						else sendPlayerNull(args[i + 1]);
					}
				}
			}
			else if(args[0].equalsIgnoreCase("promote"))
			{
				sendMessage("Promoting " + ChatUtil.toProperString(args) + " to moderator.");
				User u;
				for(int i = 0; i < args.length; i++)
				{
					if((u = plugin.getUser(args[i])) != null) user.getChatroom().addModerator(u);
					else sendPlayerNull(args[i]);
				}
			}
			else if(args[0].equalsIgnoreCase("demote"))
			{
				sendMessage("Demoting " + ChatUtil.toProperString(args) + ".");
				User u;
				for(int i = 0; i < args.length; i++)
				{
					if((u = plugin.getUser(args[i])) != null) user.getChatroom().removeModerator(u);
					else sendPlayerNull(args[i]);
				}
			}
			else sendUsage();
		}
		else sendUsage();
	}

	public void showInfo(ChatRoom room)
	{
		ChatColor color = room.type == ChatType.PARTY ? color = ChatColor.GOLD : ChatColor.GREEN;
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
				.tooltip(
						ChatColor.LIGHT_PURPLE + "Owner: " + ChatColor.RESET + owner,
						ChatColor.DARK_PURPLE + "Moderators: \n" + ChatColor.RESET + (moderators.isEmpty() ? "  <none>" : moderators.substring(0, moderators.length() - 1)),
						ChatColor.GREEN + "Members: \n" + ChatColor.RESET + (users.isEmpty() ? "  <none>" : users.substring(0, users.length() - 1))
						)
				.suggest("/chat " + room.getName());
		msg.send(sender);
	}
}
