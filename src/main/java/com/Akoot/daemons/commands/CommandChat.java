package com.Akoot.daemons.commands;

import org.bukkit.ChatColor;

import com.Akoot.daemons.User;
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
			sendMessage("Current Chatroom: " + user.getChatroom().getName());
		}
		else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("rooms"))
			{
				sendMessage("[Open Chatrooms]");
				for(ChatRoom room: plugin.chatrooms)
				{
					sendMessage(ChatColor.GREEN + room.getName() + ":");
					sendMessage(ChatColor.DARK_GREEN + "  Users:");
					for(User u: room.getUsers())
					{
						if(room.getUsers().indexOf(u) >= 10)
						{
							sendMessage("and " + (room.getUsers().size() - 10) + " more...");
							break;
						}
						sendMessage(ChatColor.WHITE + "    " + u.getPlayer().getName());
					}
				}
				return;
			}

			ChatRoom room = user.getChatroom();
			if((room = plugin.getChatRoom(args[0])) != null)
			{
				user.setChatroom(room);
				sender.sendMessage("Switched to " + room.getName());
			}
			else sendError("Chatroom " + args[0] + " does not exist.");
		}
		else
		{
			sendUsage();
		}
	}
}
