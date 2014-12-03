package com.kawaiiwolf.kawaiicrops.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiChurn;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCuttingBoard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RendererKawaiiChurn extends TileEntitySpecialRenderer 
{
	public static RendererKawaiiChurn instance = null;
    private IModelCustom model;
    private ResourceLocation modelTexture;
	
	public RendererKawaiiChurn()
	{
		if (instance == null) 
		{
			instance = this;

			model = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/cuttingboard.obj"));
			modelTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/cuttingboard.png");
		}
	}
	
	public static void register() 
	{
		new RendererKawaiiChurn();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiChurn.class, instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		int meta = te.getBlockMetadata();
	
		renderModel(x, y, z, meta);
		renderModel(x, y + 0.5d, z, meta);
		
	}
	
	private void renderModel(double x, double y, double z, int meta)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(modelTexture);

		float scale = 1.0f / 16.0f;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef(((meta + 2) * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		model.renderAll();
		GL11.glPopMatrix();
	}
}
