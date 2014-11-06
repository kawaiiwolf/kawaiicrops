package com.kawaiiwolf.kawaiicrops.proxies;

import com.kawaiiwolf.kawaiicrops.renderer.RendererKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.renderer.RendererKawaiiCuttingBoard;
import com.kawaiiwolf.kawaiicrops.renderer.RendererKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlocks;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCrop;

import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
		
	@Override
	public void registerRenderers() {
		
		RenderingHandlerKawaiiCropBlocks.register();
		RendererKawaiiCuttingBoard.register();
		RendererKawaiiFryingPan.register();
		RendererKawaiiBigPot.register();
	}
}
