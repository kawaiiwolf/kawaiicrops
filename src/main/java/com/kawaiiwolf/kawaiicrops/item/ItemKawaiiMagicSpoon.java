package com.kawaiiwolf.kawaiicrops.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemKawaiiMagicSpoon extends ItemKawaiiIngredient 
{
	public ItemKawaiiMagicSpoon() 
	{
		super("houchou", "Quicker Cooking through Magic.");
		setMaxStackSize(1);
	}
	
	@Override
	public boolean hasEffect(ItemStack item)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack item)
	{
		return EnumRarity.epic;
	}
}
