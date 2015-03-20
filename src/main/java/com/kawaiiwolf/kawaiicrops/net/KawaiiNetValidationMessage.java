package com.kawaiiwolf.kawaiicrops.net;

import com.kawaiiwolf.kawaiicrops.lib.LockedConfiguration;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class KawaiiNetValidationMessage implements IMessage 
{

	private String hash;
	
	public KawaiiNetValidationMessage() { this(""); }
	
	public KawaiiNetValidationMessage(String hash)
	{
		this.hash = hash; 
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		hash = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, hash);
	}

	public static class ClientHandler implements IMessageHandler<KawaiiNetValidationMessage, IMessage>
	{
		@Override
		public IMessage onMessage(KawaiiNetValidationMessage message,  MessageContext ctx) 
		{
			return new KawaiiNetValidationMessage(LockedConfiguration.digest());
		}	
	}

	public static class ServerHandler implements IMessageHandler<KawaiiNetValidationMessage, IMessage>
	{
		@Override
		public IMessage onMessage(KawaiiNetValidationMessage message,  MessageContext ctx) 
		{
			if (!message.hash.equals(LockedConfiguration.digest()))
				ctx.getServerHandler().playerEntity.playerNetServerHandler.kickPlayerFromServer("Error: Client and server aren't using the same kawaiicrops configuration.\nPlease ask your server Administrator for an updated copy.");
			return null;
		}	
	}
}
