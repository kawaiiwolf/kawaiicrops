package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.lib.*;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHungerPotion extends ItemFood {

	public ItemHungerPotion() {
		super(0, 0, false);
		setUnlocalizedName(Constants.MOD_ID + ".hungerpotion");
		setTextureName("potion_bottle_drinkable");
		setAlwaysEdible();
	}
	
	@Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }
	
	@Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		ItemStack ret = super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
		if (par3EntityPlayer.getFoodStats().getFoodLevel() > 6)
			par3EntityPlayer.getFoodStats().addStats(-6,0);
		return ret;
    }
}
