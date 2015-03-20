package com.kawaiiwolf.kawaiicrops.event;

import com.kawaiiwolf.kawaiicrops.net.KawaiiNetValidationMessage;
import com.kawaiiwolf.kawaiicrops.net.ModNetty;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class EventKawaiiLoginEvent 
{

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) 
	{
		if (event.player instanceof EntityPlayerMP)
			ModNetty.network.sendTo(new KawaiiNetValidationMessage("PING"), (EntityPlayerMP)event.player);
    }
}
