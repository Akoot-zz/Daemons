package com.Akoot.daemons;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.Akoot.cthulhu.util.CthFile;
import com.Akoot.daemons.chat.ChatRoom;
import com.Akoot.daemons.scoreboard.ChatScoreboard;

public class User
{
	private Player player;
	private ChatScoreboard chatboard;
	private ChatRoom chatroom;
	private CthFile config;
	
	public User(Player player)
	{
		System.out.println("SOMEONE CREATED A NEW USER");
		this.player = player;
		this.chatboard = new ChatScoreboard(this);
		this.config = new CthFile(Daemons.getInstance().getUserDir(), player.getUniqueId().toString() + ".cth");
		if(!config.exists()) config.create();
		if(!config.has("nickname")) config.set("nickname", player.getName());
		if(!config.has("chatroom")) config.set("chatroom", Daemons.getInstance().globalChatroom.getName());
		setChatroom(config.getString("chatroom"));
		player.setDisplayName(config.getString("nickname"));
	}
	
	public CthFile getConfig()
	{
		return config;
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
		this.chatroom = room;
		config.set("chatroom", room.getName());
		this.chatroom.add(this);
		this.chatboard.update();
	}
	
	public boolean ownsChatroom()
	{
		return this.chatroom.getOwner() == this;
	}
	
	public boolean moderatesChatroom()
	{
		return this.chatroom.getModerators().contains(this) || ownsChatroom();
	}
	
	public void sendMessage(String message)
	{
		player.sendMessage(message);
	}
	
	public String getName()
	{
		return player.getName();
	}
	
	public String getDisplayName()
	{
		return player.getDisplayName();
	}
	
	public UUID getUUID()
	{
		return player.getUniqueId();
	}
	
	public boolean equals(User user)
	{
		if(user.getPlayer() == this.getPlayer()) return true;
		return false;
	}
	
	public int getPlaytime()
	{
		return this.config.getInt("playtime");
	}
	
	public boolean hasPermission(String permission)
	{
		return (player.hasPermission(permission) || player.isOp());
	}

	public void setChatroom(String roomName)
	{
		ChatRoom room;
		if((room = Daemons.getInstance().getChatRoom(roomName)) != null)
		{
			setChatroom(room);
		}
	}
}
