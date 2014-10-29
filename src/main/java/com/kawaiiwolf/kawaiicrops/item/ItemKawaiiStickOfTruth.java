package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemKawaiiStickOfTruth extends Item 
{
	public ItemKawaiiStickOfTruth()
	{
		setUnlocalizedName(Constants.MOD_ID + ".stickoftruth");
		setTextureName("stick");
		setMaxStackSize(1);
	}
	
    @Override
    public boolean onItemUse(ItemStack items, EntityPlayer entity, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
    	print("Block: " + NamespaceHelper.getBlockName(world.getBlock(x, y, z)) + ", Meta: " + world.getBlockMetadata(x, y, z));
    	return false;
    }
    
    private void print(String message)
    {
    	Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }
}
