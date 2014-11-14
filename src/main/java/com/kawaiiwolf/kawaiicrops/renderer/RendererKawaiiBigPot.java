package com.kawaiiwolf.kawaiicrops.renderer;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.lib.FNV;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCuttingBoard;
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
import net.minecraft.util.ResourceLocation;

public class RendererKawaiiBigPot extends TileEntitySpecialRenderer 
{

	public static RendererKawaiiBigPot instance = null;
	
	public RendererKawaiiBigPot()
	{
		if (instance == null) {
			instance = this;
		}			
	}
	
	public static void register() 
	{
		new RendererKawaiiBigPot();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiBigPot.class, instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		if (te instanceof TileEntityKawaiiBigPot)
		{
			int meta = te.getBlockMetadata();
			int itemCount = 0, itemCurrent = 1;
			float rotation, spin;
			
			TexturedIcon[] icons = ((TileEntityKawaiiBigPot)te).getDisplayItems();;

			// Temporary
			renderBaseItem(Blocks.wooden_pressure_plate.getIcon(0, 0), x, y, z, meta, 0.875f, 90.0f, 1.0f, 0.0f, 0.0f, TextureMap.locationBlocksTexture);
			
			for (TexturedIcon icon : icons)
				if (icon != null)
					itemCount++;
			for (TexturedIcon icon : icons)
				if (icon != null)
				{
					rotation = itemCurrent++ / (float)itemCount;
					spin = (Math.abs(Minecraft.getSystemTime()) / 32 + 61 * itemCurrent) % 360;
					
					renderItem(icon.icon, x, y + 0.5d, z, rotation, spin, icon.texture);
				}
		}
	}
	
	private void renderBaseItem(IIcon icon, double x, double y, double z, int meta, float scale, float angle, float rotatex, float rotatey, float rotatez, ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef((meta * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotatef(angle, rotatex, rotatey, rotatez);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

	
	private void renderItem(IIcon icon, double x, double y, double z, float rotation, float spin, ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		float scale = 0.3f;
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		
		// Set Position
		GL11.glTranslated(x + 0.5d, y + 0.0625d - scale/2.0d, z + 0.5d);
		
		// Scale and get item into the right orientation
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(360.0f * rotation, 0.0f, 0.0f, 1.0f);
		
		// Draw
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

}
