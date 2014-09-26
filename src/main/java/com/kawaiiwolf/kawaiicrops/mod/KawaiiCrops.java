package com.kawaiiwolf.kawaiicrops.mod;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

//import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.*;
import com.kawaiiwolf.kawaiicrops.proxies.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCrops {
	
	public static HashMap<String,Item> items = new HashMap<String,Item>();
	public static HashMap<String,Block> blocks = new HashMap<String,Block>();
	
	@Instance(value = Constants.MOD_ID)
	public static KawaiiCrops instance;
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	// Init Blocks/Items/etc
    	
    	(new com.kawaiiwolf.kawaiicrops.item.ItemHungerPotion()).register();
    	
    	ConfigurationLoader.loadConfiguration(event.getSuggestedConfigurationFile().getParent());
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    	// Init TileEntities/Events/Renderes
    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
     	// Other mod intergation. 
    }
	
}
