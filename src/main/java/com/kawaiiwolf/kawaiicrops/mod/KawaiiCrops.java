package com.kawaiiwolf.kawaiicrops.mod;

import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.*;
import com.kawaiiwolf.kawaiicrops.proxies.*;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;
import com.kawaiiwolf.kawaiicrops.tileentity.ModTileEntities;
import com.kawaiiwolf.kawaiicrops.world.ModWorldGen;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCrops {
	
	@SidedProxy(clientSide="com.kawaiiwolf.kawaiicrops.proxies.ClientProxy", serverSide="com.kawaiiwolf.kawaiicrops.proxies.CommonProxy")
	public static CommonProxy proxy;

	ConfigurationLoader config = null;
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) 
    {
    	config = new ConfigurationLoader(event.getSuggestedConfigurationFile().getParent());
    	config.loadConfiguration_PreInit();
    	ModBlocks.register();
    	ModItems.register();
    	ModWorldGen.register();
    }
 
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) 
    {
    	proxy.registerRenderers();
    	ModTileEntities.register();
    	ModItems.registerOreDictionary();
    }
 
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) 
    {
    	config.loadConfiguration_PostInit(event);
    	ModBlocks.registerDropTables();
    	
    	RecipeKawaiiCuttingBoard test = new RecipeKawaiiCuttingBoard(new ItemStack(Items.diamond), new ItemStack(Items.porkchop));
    	test.register();
    }
    
    /**
     * TODO:
     * 
     * remove output from tecooker array, move to new home
     * 
     * Custom Cooking:
     *   Custom Recipies
     *   Add particle effects to base TE & NBT
     *   Add Block rotation to base TE & NBT 
     *   Limit stack size to 1
     *   block on break -> Drop all
     *   block on place -> determine rotation
     *   
     *   OPtions:
     *   requires bowls
     *   cook ticks
     *   burn ticks
     *   oil/water
     *   use up fluid
     *   
     *   food items: Containers returned on eaten or used to fill stuff ?
     *   forge dictionary: cooking oil ?
     *    
     */
}
