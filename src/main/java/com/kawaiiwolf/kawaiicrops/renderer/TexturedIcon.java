package com.kawaiiwolf.kawaiicrops.renderer;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TexturedIcon 
{
	IIcon icon;
	ResourceLocation texture;
	
	TexturedIcon(IIcon icon, ResourceLocation texture)
	{
		this.icon = icon;
		this.texture = texture;
	}
	
	public TexturedIcon(ItemStack item)
	{
		this(item.getIconIndex(), NamespaceHelper.isItemBlock(item) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
	}
}
