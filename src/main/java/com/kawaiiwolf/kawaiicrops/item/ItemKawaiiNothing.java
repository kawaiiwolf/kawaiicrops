package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKawaiiNothing extends ItemKawaiiIngredient
{
	public ItemKawaiiNothing() 
	{
		super("nothing", "Clean this item out to get back the container.");
	}
	
	@Override
    public void onUpdate(ItemStack item, World world, Entity entity, int slot, boolean p_77663_5_) 
	{
		item.stackSize = 0;
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			player.inventory.setInventorySlotContents(slot, null);
		}
	}

    @Override
    public void onCreated(ItemStack item, World world, EntityPlayer entity) 
    {
    	item.stackSize = 0;
    }
}
