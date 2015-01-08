package com.kawaiiwolf.kawaiicrops.event;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventKawaiiMultiHarvest 
{
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event) 
	{
		if (event.block instanceof BlockKawaiiCrop)
		{
			BlockKawaiiCrop block = (BlockKawaiiCrop) event.block;
			
			if (block.MultiHarvest && event.blockMetadata == 7)
			{
				block.breakAndReplantBlocks(event.world, event.x, event.y, event.z, event.block, event.blockMetadata);
				event.setCanceled(true);
			}
		}
		
	}
}
