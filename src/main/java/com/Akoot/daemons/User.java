package com.Akoot.daemons;

import org.bukkit.entity.Player;

import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.scoreboard.ChatScoreboard;

public class User
{
	private Player player;
	private ChatScoreboard chatboard;
	private ChatRoom chatroom;
	
	public User(Player player)
	{
		System.out.println("SOMEONE CREATED A NEW USER");
		this.player = player;
		this.chatboard = new ChatScoreboard(this);
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
	
	public void setBoard()
	{
		chatboard.update();
		this.player.setScoreboard(chatboard.getBoard());
	}
	
	public ChatScoreboard getChatScoreboard()
	{
		return chatboard;
	}
	
	public void setChatroom(ChatRoom room)
	{
		if(this.chatroom != null) this.chatroom.remove(this);
		this.chatroom = room;
		this.chatroom.add(this);
		this.chatboard.update();
	}
	
	public boolean equals(User user)
	{
		if(user.getPlayer() == this.getPlayer()) return true;
		return false;
	}
}
