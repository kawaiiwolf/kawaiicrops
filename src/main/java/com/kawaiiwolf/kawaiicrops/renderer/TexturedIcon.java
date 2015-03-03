package com.kawaiiwolf.kawaiicrops.renderer;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TexturedIcon 
{
	IIcon icon;
	ResourceLocation texture;
	
	public TexturedIcon(IIcon icon, ResourceLocation texture)
	{
		this.icon = icon != null ? icon : ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		this.texture = texture;
	}
	
	public TexturedIcon(ItemStack item)
	{
		this((item != null ? item.getIconIndex() : null), item == null || NamespaceHelper.isItemBlock(item) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
	}
}
