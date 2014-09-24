package com.kawaiiwolf.kawaiicrops.mod;

import com.kawaiiwolf.kawaiicrops.helpers.Constants;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class KawaiiCropsMod {
	
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
