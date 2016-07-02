package com.Akoot.daemons;

import org.bukkit.entity.Player;

import com.Akoot.daemons.chat.ChatRoom;

public class User
{
	private Player player;
	private ChatRoom chatroom;
	
	public User(Player player)
	{
		this.player = player;
		setChatroom(Daemons.getInstance().getChatRoom(("Global")));
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public ChatRoom getChatroom()
	{
		return chatroom;
	}
	
	public void setChatroom(ChatRoom room)
	{
		if(this.chatroom != null) this.chatroom.remove(this);
		this.chatroom = room;
		this.chatroom.add(this);
	}
}
