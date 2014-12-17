package com.kawaiiwolf.kawaiicrops.event;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import com.kawaiiwolf.kawaiicrops.lib.DropTable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventKawaiiLivingDrop 
{
	public static HashMap<Class, DropTable> drops = new HashMap<Class, DropTable>();

	@SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event)
	{
		if (drops.keySet().contains(event.entityLiving.getClass()))
		{
			for (ItemStack item : drops.get(event.entityLiving.getClass()).generateLoot(event.entityLiving.worldObj.rand))
				event.entityLiving.entityDropItem(item, 1.0f);
		}
	}
}
