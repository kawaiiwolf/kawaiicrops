package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.lib.*;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
		
		setCreativeTab(ModItems.KawaiiCreativeTab);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		list.add("You feel hungry just looking at it.");
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
