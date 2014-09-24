package com.kawaiiwolf.kawaiicrops.mod;

import com.kawaiiwolf.kawaiicrops.helpers.Constants;
import com.kawaiiwolf.kawaiicrops.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCropsMod {
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	System.out.println("KAWAIICROPS PRE INIT EVENT");
    	
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	System.out.println("KAWAIICROPS INIT EVENT");

    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	System.out.println("KAWAIICROPS POST INIT EVENT");
     	
    }
	
}
