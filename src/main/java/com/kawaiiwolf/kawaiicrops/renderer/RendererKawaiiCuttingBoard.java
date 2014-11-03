package com.kawaiiwolf.kawaiicrops.renderer;

import java.awt.Color;

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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCuttingBoard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class RendererKawaiiCuttingBoard extends TileEntitySpecialRenderer {

	public static RendererKawaiiCuttingBoard instance = null;
	
	public RendererKawaiiCuttingBoard()
	{
		if (instance == null) {
			instance = this;
		}			
	}
	
	public static void register() 
	{
		new RendererKawaiiCuttingBoard();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiCuttingBoard.class, instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		// Temporary
		renderItem(new ItemStack(Items.item_frame), x, y, z, 1.0f, 90.0f, 1.0f, 0.0f, 0.0f);

		renderItem(new ItemStack(Items.porkchop), x + 0.3125d, y + 0.0625d, z + 0.3125d, 0.5f, 90.0f, 1.0f, 0.0f, 0.0f);
		renderItem(new ItemStack(Items.iron_sword), x, y + 0.0625, z, 0.5f, 90.0f, 1.0f, 0.0f, 0.0f);
	}
	
	private void renderItem(ItemStack item, double x, double y, double z, float scale, float angle, float rotatex, float rotatey, float rotatez)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
		IIcon icon = item.getItem().getIcon(item, 0);
		Color color = new Color(item.getItem().getColorFromItemStack(item, 0));

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(angle, rotatex, rotatey, rotatez);
		GL11.glColor3ub((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}

}
