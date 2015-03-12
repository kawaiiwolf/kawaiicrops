package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemKawaiiClothes extends ItemArmor 
{
	public String[] ToolTipText;
	public String Name = "";	
	
	public ItemKawaiiClothes(String name, ArmorMaterial mat, int type, String[] tooltips) throws IllegalArgumentException 
	{
		super(mat, 0, type);

		if (tooltips == null || tooltips.length != 4)
			throw new IllegalArgumentException("Tooltips must be a String array of size 4.");
		
		ToolTipText = tooltips;
		Name = name;
		
		setTextureName(Constants.MOD_ID + ":" + getPieceName());
		setUnlocalizedName(Constants.MOD_ID + "." + getPieceName());
	}

	private static String[] pieceName = new String[] { ".hat", ".top", ".pants", ".shoes" }; 
	private String getPieceName()
	{
		return Name + pieceName[armorType];
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
	{
	    return Constants.MOD_ID + ":textures/models/armor/" + Name + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) 
	{
		if (ToolTipText[armorType].length() > 0)
			list.add(ToolTipText[armorType]);
	}
	
	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		ModItems.ModClothes.add(this);
		GameRegistry.registerItem(this, getPieceName());
	}
}
