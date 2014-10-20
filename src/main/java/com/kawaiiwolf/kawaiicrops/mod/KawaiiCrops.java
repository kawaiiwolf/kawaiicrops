package com.kawaiiwolf.kawaiicrops.mod;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.item.Item;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.*;
import com.kawaiiwolf.kawaiicrops.proxies.*;
import com.kawaiiwolf.kawaiicrops.tileentity.ModTileEntities;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCrops {
	
	@Instance(value = Constants.MOD_ID)
	public static KawaiiCrops instance;
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

	ConfigurationLoader config = null;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	config = new ConfigurationLoader(event.getSuggestedConfigurationFile().getParent());
    	config.loadConfiguration_PreInit();
    	
    	ModItems.register();
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
     	
    	proxy.registerRenderers();
    	ModTileEntities.register();
    	ModItems.registerOreDictionary();
    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
     	// Recipies
    	
    	config.loadConfiguration_PostInit();
    	ModBlocks.registerDropTables();
    }

    /**
     * TODO:
     * 
     * Set OreDict string in config loader
     * Recipies
     * REFERENCE_ORE_COMMENT
     * BUSH render type
     * Trees
     * World Gen & Mystery Seeds
     * Custom Cooking ! 
     */
}
