package com.kawaiiwolf.kawaiicrops.proxies;

import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlock;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCrop;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
		
	@Override
	public void registerRenderers() {
		
		RenderingHandlerKawaiiCropBlock.register();
	}
}
