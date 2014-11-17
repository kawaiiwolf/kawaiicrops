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
			double rotation, spin;
			
			TexturedIcon[] icons = ((TileEntityKawaiiBigPot)te).getDisplayItems();

			// Temporary
			renderBaseItem(Blocks.wooden_pressure_plate.getIcon(0, 0), x, y, z, meta, 0.875f, TextureMap.locationBlocksTexture);
			
			if (icons.length == 1)
			{
				renderBaseItem(icons[0].icon, x, y + 0.35d, z, meta, 0.875f, icons[0].texture);
			}
			else
			{
				for (TexturedIcon icon : icons)
					if (icon != null)
						itemCount++;
				for (TexturedIcon icon : icons)
					if (icon != null)
					{
						rotation = itemCurrent++ * 2.0d * Math.PI / itemCount;
						spin = ((Minecraft.getSystemTime() / 32 + 61 * itemCurrent) % 360) / 180.0d * Math.PI;
						
						renderItem(icon.icon, meta, x, y, z, rotation, spin, icon.texture);
					}
				
				renderLiquid(((TileEntityKawaiiBigPot)te).getDisplayLiquid(), x, y + 0.35d, z, 0.875d);
			}
		}
	}
	
	private void renderBaseItem(IIcon icon, double x, double y, double z, int meta, float scale, ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef((meta * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotatef(90, 1, 0, 0);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

	private void renderLiquid(IIcon icon, double x, double y, double z, double scale)
	{
		if (icon == null) return;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		GL11.glPushMatrix();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
		GL11.glColor4d(1.0d, 1.0d, 1.0d, 0.9d);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glScaled(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotated(90d, 1, 0, 0);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	
	private void renderItem(IIcon icon, int meta, double x, double y, double z, double rotation, double spin, ResourceLocation texture)
	{
		if (icon == null) return;
		
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		float scale = 0.3f;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + (1 + scale + Math.sin(rotation)) / 4.0d, y + 0.125d + Math.abs(Math.sin(rotation * 3.0d + spin)) / 8.0d, z + ( 1 + scale + Math.cos(rotation)) / 4.0d);
		GL11.glScaled(scale, scale, scale);
		GL11.glRotated(90.0f, 10, Math.cos(spin), Math.sin(spin));
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

}
