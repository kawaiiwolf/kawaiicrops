package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class Util 
{

	public static boolean arrayContains (ArrayList<ItemStack> array, ItemStack element)
	{
		for (ItemStack i : array)
			if (i.getItem() == element.getItem() && i.getItemDamage() == element.getItemDamage())
				return true;
		return false;
	}
}
