package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemKawaiiIngredient extends Item 
{
	public String ToolTipText = "";
	private String name = "";
	public String OreDict = "";

	public String ContainerItemString = "";
	public Item ContainerItem;
	
	public ItemKawaiiIngredient(String name, String toolTip) 
	{
		setTextureName(Constants.MOD_ID + ":" + name);
		setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		ToolTipText = toolTip;
		
		setCreativeTab(ModItems.KawaiiCreativeTab);
	}
	
	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		ModItems.ModIngredients.add(this);
		GameRegistry.registerItem(this, Constants.MOD_ID + "." + name);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if (ToolTipText.length() > 0)
			list.add(ToolTipText);
	}
	
	@Override
    public Item getContainerItem()
    {
		if (super.getContainerItem() == null && ContainerItemString.length() > 0)
			this.setContainerItem(NamespaceHelper.getItemByName(ContainerItemString).getItem());
        return super.getContainerItem();
    }
	
	@Override
    public ItemStack getContainerItem(ItemStack stack)
    {
		if (super.getContainerItem(stack) == null && ContainerItemString.length() > 0)
			this.setContainerItem(NamespaceHelper.getItemByName(ContainerItemString).getItem());
        return super.getContainerItem(stack);
    }
}
