package com.kawaiiwolf.kawaiicrops.renderer;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiFryingPan;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class RendererKawaiiFryingPan extends TileEntitySpecialRenderer 
{
	public static RendererKawaiiFryingPan instance = null;
	
	public RendererKawaiiFryingPan()
	{
		if (instance == null) {
			instance = this;
		}			
	}
	
	public static void register() 
	{
		new RendererKawaiiFryingPan();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiFryingPan.class, instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		int meta = te.getBlockMetadata();
		int itemCount = 0;
		ItemStack[] itemsToRender;
		
		// Temporary
		renderBaseItem(new ItemStack(Blocks.wooden_pressure_plate), x, y, z, meta, 0.875f, 90.0f, 1.0f, 0.0f, 0.0f, true);

		if (te instanceof TileEntityKawaiiFryingPan)
		{
			itemsToRender = ((TileEntityKawaiiFryingPan)te).getDisplayItems();
			for (ItemStack item : itemsToRender)
				itemCount++;
			//renderItem
		}
	}

	private void renderBaseItem(ItemStack item, double x, double y, double z, int meta, float scale, float angle, float rotatex, float rotatey, float rotatez, boolean isBlock)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(isBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		IIcon icon = item.getItem().getIcon(item, 0);
		Color color = new Color(item.getItem().getColorFromItemStack(item, 0));

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef((meta * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotatef(angle, rotatex, rotatey, rotatez);
		GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

	
	private void renderItem(ItemStack item, double x, double y, double z, int meta, float scale, float angle, float rotatex, float rotatey, float rotatez, boolean isBlock)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(isBlock ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		IIcon icon = item.getItem().getIcon(item, 0);
		Color color = new Color(item.getItem().getColorFromItemStack(item, 0));

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef((meta * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotatef(angle, rotatex, rotatey, rotatez);
		GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

}
