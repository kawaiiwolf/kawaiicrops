package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.lib.IToolTipBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemKawaiiTooltipBlock extends ItemBlock 
{
	private final Block block;
	
	public ItemKawaiiTooltipBlock(Block block) 
	{
		super(block);
		this.block = block;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) 
	{
		if (block instanceof IToolTipBlock)
		{
			String text = ((IToolTipBlock)block).getToolTip();
			if (text.length() > 0)
				list.add(text);
		}
	}
}
