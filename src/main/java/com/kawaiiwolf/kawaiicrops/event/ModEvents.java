package com.kawaiiwolf.kawaiicrops.event;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

public class ModEvents 
{
	public static void register()
	{
		MinecraftForge.EVENT_BUS.register(new EventKawaiiLivingDrop());
		MinecraftForge.EVENT_BUS.register(new EventKawaiiMultiHarvest());
		FMLCommonHandler.instance().bus().register(new EventKawaiiLoginEvent());
	}
}
