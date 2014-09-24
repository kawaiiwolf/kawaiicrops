package com.kawaiiwolf.kawaiicrops.mod;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.proxies.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCrops {
	
	@Instance(value = "KawaiiCrops")
	public static KawaiiCrops instance;
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
     	
    }
	
}
