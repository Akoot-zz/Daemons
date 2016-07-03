package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.Akoot.daemons.User;
import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.chat.Chats.ChatType;

import mkremins.fanciful.FancyMessage;

public class CommandChat extends Command
{
	public CommandChat()
	{
		this.name = "Chat";
		this.color = ChatColor.GOLD;
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
				for(ChatRoom room: plugin.chatrooms) showInfo(room);
				return;
			}
			else if(args[0].equalsIgnoreCase("leave"))
			{
				if(isPlayer())
				{
					if(user.ownsChatroom())
					{
						sendMessage("Disband " + user.getChatroom().getName() + "?");
						sendCommand("Yes", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband -y");
						sendCommand("No", "Click to NOT disband " + user.getChatroom().getName() + ".", "/chat disband -n");
					}
					else
					{
						user.getChatroom().remove(user);
						sendMessage("You have left the chatroom.");
					}
				}
				else sendPlayerOnly();
			}
			else if(args[0].equalsIgnoreCase("disband"))
			{
				if(isPlayer())
				{
					if(user.ownsChatroom())
					{
						sendMessage("Disband " + user.getChatroom().getName() + "?");
						sendCommand("Yes", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband -y");
						sendCommand("No", "Click to NOT disband " + user.getChatroom().getName() + ".", "/chat disband -n");
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
					if(room != user.getChatroom())
					{
						user.getChatroom().getUsers().remove(this);
						user.setChatroom(room);
						sendMessage("Switched to " + room.getName());
					}
					else sendMessage("You are already in that room!");
				}
				else sendError("Chatroom " + args[0] + " does not exist.");
			}
			else sendPlayerOnly();
		}
		else if(args.length == 2)
		{
			String name = args[1];
			if(args[0].equalsIgnoreCase("create"))
			{
				if(isPlayer() && user.ownsChatroom())
				{
					sendMessage("Disband " + user.getChatroom().getName() + " and create " + name + "?");
					sendCommand("Yes", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband -y");
					sendCommand("No", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband -n");
				}
				else
				{
					ChatRoom room = new ChatRoom(isPlayer() ? ChatType.PRIVATE : ChatType.PUBLIC, name, isPlayer() ? false : true);
					plugin.registerChatRoom(room);
					sendMessage("Successfully created" + room.type + " chatroom \"" + room.getName() + "\"!");
					if(isPlayer())
					{
						user.getChatroom().remove(user);
						user.setChatroom(room);
						room.setOwner(user);
						sendMessage("You will no longer receive global chat messages.");
						sendMessage("To enable or disable global chat messages, type /chat global <true/false>");
						sendCommand(ChatColor.LIGHT_PURPLE + "[Click here for more commands]", ChatColor.AQUA + "Click for more commands", "/chat commands");
					}
				}
			}
			else if(args[0].equalsIgnoreCase("disband"))
			{
				if(!isPlayer() || user.ownsChatroom())
				{
					if(args[1].equalsIgnoreCase("-y") && isPlayer())
					{
						String roomName = user.getChatroom().getName();
						user.getChatroom().disband();
						sendMessage("Successfully disbanded " + roomName + ". RIP");
					}
					else if(args[1].equalsIgnoreCase("-n") && isPlayer())
					{
						sendMessage(user.getChatroom().getName() + " will NOT be disbanded.");
					}
					else
					{
						if(plugin.getChatRoom(args[1]) != null)
						{
							String roomName = plugin.getChatRoom(args[1]).getName();
							plugin.getChatRoom(args[1]).disband();
							sendMessage("Successfully disbanded " + roomName + ". RIP");
						}
						else sendError("Chatroom " + args[1] + " does not exist!");
					}
				}
				else sendError("You do not have permission to disband the chatroom.");
			}
		}
		else sendUsage();
	}

	public void showInfo(ChatRoom room)
	{
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
		FancyMessage msg = new FancyMessage(room.getName()).color(ChatColor.GREEN)
				.tooltip(
				ChatColor.LIGHT_PURPLE + "Owner: " + ChatColor.RESET + owner,
				ChatColor.DARK_PURPLE + "Moderators: \n" + ChatColor.RESET + (moderators.isEmpty() ? "  <none>" : moderators.substring(0, moderators.length() - 1)),
				ChatColor.GREEN + "Members: \n" + ChatColor.RESET + (users.isEmpty() ? "  <none>" : users.substring(0, users.length() - 1))
				)
				.suggest("/chat " + room.getName());
		msg.send(sender);
	}
}
