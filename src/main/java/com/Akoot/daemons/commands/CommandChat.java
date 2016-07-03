package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.chat.ChatRoom;

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
		if(args.length == 0)
		{
			sendMessage("[Current Chatroom]", ChatColor.LIGHT_PURPLE + "This is already displayed in the scoreboard.");
			listUsers(user.getChatroom());
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("rooms") || args[0].equalsIgnoreCase("list"))
			{
				sendMessage("[Open Chatrooms]");
				for(ChatRoom room: plugin.chatrooms) listUsers(room);
				return;
			}

			ChatRoom room = user.getChatroom();
			if((room = plugin.getChatRoom(args[0])) != null)
			{
				if(room != user.getChatroom())
				{
					user.setChatroom(room);
					sendMessage("Switched to " + room.getName());
				}
				else sendMessage("You are already in that room!");
			}
			else sendError("Chatroom " + args[0] + " does not exist.");
		}
		else if(args.length == 2)
		{
			String name = args[1];
			if(args[0].equalsIgnoreCase("create"))
			{
				if(user.getChatroom().getOwner().equals(user))
				{
					sendMessage("Disband " + user.getChatroom().getName() + " and create " + name + "?");
					sendCommand("Yes", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband");
					sendCommand("No", "Click to disband " + user.getChatroom().getName() + ".", "/chat disband");
				}
			}
		}
		else
		{
			sendUsage();
		}
	}

	public void listUsers(ChatRoom room)
	{
		String users = "";
		for(int i = 0; i < (room.getUsers().size() < 10 ? room.getUsers().size() : 10); i++) users += "  " + room.getUsers().get(i).getPlayer().getDisplayName() + "\n";
		if(room.getUsers().size() > 10) users += "and " + (room.getUsers().size() - 10) + " more...";
		sendCommand(ChatColor.GREEN + room.getName(), ChatColor.GREEN + (room.getUsers().size() > 0 ? "Users:\n  " + ChatColor.WHITE + users.trim() : "<Empty>"), "/chat " + room.getName());
	}
}
