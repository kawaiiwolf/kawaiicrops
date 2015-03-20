package com.kawaiiwolf.kawaiicrops.net;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ModNetty 
{
	public static SimpleNetworkWrapper network;
	
	public static void register()
	{
		network = NetworkRegistry.INSTANCE.newSimpleChannel("kawaiicrops");
		
		network.registerMessage(KawaiiNetValidationMessage.ClientHandler.class, KawaiiNetValidationMessage.class, 0, Side.CLIENT);
		network.registerMessage(KawaiiNetValidationMessage.ServerHandler.class, KawaiiNetValidationMessage.class, 0, Side.SERVER);
	}
}
