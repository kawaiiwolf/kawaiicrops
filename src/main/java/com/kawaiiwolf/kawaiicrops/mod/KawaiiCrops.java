package com.kawaiiwolf.kawaiicrops.mod;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.event.ModEvents;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.*;
import com.kawaiiwolf.kawaiicrops.proxies.*;
import com.kawaiiwolf.kawaiicrops.tileentity.ModTileEntities;
import com.kawaiiwolf.kawaiicrops.world.ModWorldGen;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION_FULL)
public class KawaiiCrops {
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

	ConfigurationLoader config = null;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
    	config = new ConfigurationLoader(event);
    	config.loadConfiguration_PreInit();
    	ModBlocks.register();
    	ModItems.register();
    	ModWorldGen.register();
    	ModEvents.register();
    	
    	// Dynamically set mcmod.info version
    	event.getModMetadata().version = Constants.VERSION;
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) 
    {
    	config.loadConfiguration_Init();
    	proxy.registerRenderers();
    	ModTileEntities.register();
    	ModItems.registerOreDictionary();

    	if (Loader.isModLoaded("Waila"))
    		FMLInterModComms.sendMessage("Waila", "register", "com.kawaiiwolf.kawaiicrops.waila.WailaTileHandler.callbackRegister");
    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) 
    {
    	config.loadConfiguration_PostInit();
    	ModBlocks.registerDropTables();
    	ModBlocks.registerCookingBlockLists();
    }
    
    /**
     * TODO:
     * 
     * TEST TEST TEST
     * Pitchers 
     * 
     * Changes:
     * 
     *   Sort option for general.cfg
     *   
     *   RENAMED ALL INTERNAL ITEMS. Since they automatically include mod name, the redundant
     *   "kawaiicrops." will be removed from the troublesome "kawaiicrops:kawaiicrops.itemname". This
     *   might break existing items but anyone working with the recipes from here on out will be much
     *   happier !
     *   
     *   Note: Recipies should be easy to change over. Search "kawaiicrops:kawaiicrops." and replace
     *   with "kawaiicrops:". Done ! Sorry to anyone's world who was broken as a result of this.
     */
}
