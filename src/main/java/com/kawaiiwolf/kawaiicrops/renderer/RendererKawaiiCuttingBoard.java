package com.kawaiiwolf.kawaiicrops.renderer;

import java.awt.Color;

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
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCuttingBoard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class RendererKawaiiCuttingBoard extends TileEntitySpecialRenderer {

	public static RendererKawaiiCuttingBoard instance = null;
    private IModelCustom model;
    private ResourceLocation modelTexture;
	
	public RendererKawaiiCuttingBoard()
	{
		if (instance == null) {
			instance = this;

			model = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/cuttingboard.obj"));
			modelTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/cuttingboard.png");
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
		int meta = te.getBlockMetadata();
		
		// Temporary
		//renderItem(Blocks.wooden_pressure_plate.getIcon(0, 0), x, y, z, meta, 0.875f, 90.0f, 1.0f, 0.0f, 0.0f, TextureMap.locationBlocksTexture);
		
		renderModel(x, y, z, meta);

		TexturedIcon render = null;
		if (te instanceof TileEntityKawaiiCuttingBoard)
			render = ((TileEntityKawaiiCuttingBoard)te).getDisplayItems()[0];
		if (render != null)
			renderItem(render.icon, x - 0.0625d * ((meta & 2) == 2 ? 1.0d : -1.0d), y + 0.0625, z - 0.0625d * (((meta & 1) != ((meta >> 1) & 1)) ? 1.0d : -1.0d), meta, 0.5f, 90.0f, 1.0f, 0.0f, 0.0f, render.texture);
		
		renderItem(Items.iron_sword.getIconFromDamage(0), x + 0.3125d * ((meta & 2) == 2 ? 1.0d : -1.0d), y + 0.0625, z + 0.3125d * (((meta & 1) != ((meta >> 1) & 1)) ? 1.0d : -1.0d), meta, 0.5f, 90.0f, 1.0f, 0.0f, 0.0f, TextureMap.locationItemsTexture);
	}
	
	private void renderItem(IIcon icon, double x, double y, double z, int meta, float scale, float angle, float rotatex, float rotatey, float rotatez, ResourceLocation texture)
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
	
	private void renderModel(double x, double y, double z, int meta)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(modelTexture);

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5d, y + 0.03125d, z + 0.5d);
		GL11.glRotatef(((meta + 1) * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		model.renderAll();
		GL11.glPopMatrix();
	}

}
