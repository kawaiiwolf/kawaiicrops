package com.kawaiiwolf.kawaiicrops.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import com.kawaiiwolf.kawaiicrops.lib.DropTable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventKawaiiLivingDrop 
{
	public static HashMap<Class, DropTable[]> drops = new HashMap<Class, DropTable[]>();
	public static HashSet<Class> cull = new HashSet<Class>();

	@SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event)
	{
		if (drops.keySet().contains(event.entityLiving.getClass()))
		{
			if (cull.contains(event.entityLiving.getClass()))
				event.drops.clear();
			
			// Sanity check
			if (drops.get(event.entityLiving.getClass()) == null || drops.get(event.entityLiving.getClass()).length < 1) return;
			
			int table;
			if (event.entityLiving instanceof EntityAgeable && ((EntityAgeable)event.entityLiving).isChild() && drops.get(event.entityLiving.getClass()).length > 1)
				table = 1;
			else
				table = 0;
			
			for (ItemStack item : drops.get(event.entityLiving.getClass())[table].generateLoot(event.entityLiving.worldObj.rand))
				event.entityLiving.entityDropItem(item, 1.0f);
		}
	}
}
