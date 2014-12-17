package com.kawaiiwolf.kawaiicrops.event;

import net.minecraftforge.common.MinecraftForge;

public class ModEvents 
{
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(new EventKawaiiLivingDrop());
	}
}
