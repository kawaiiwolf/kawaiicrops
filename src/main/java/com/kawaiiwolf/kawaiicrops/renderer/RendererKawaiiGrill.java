package com.kawaiiwolf.kawaiicrops.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiGrill;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RendererKawaiiGrill extends TileEntitySpecialRenderer 
{

	public static RendererKawaiiGrill instance = null;
    private IModelCustom model;
    private ResourceLocation modelTexture;
    private IModelCustom modelLegs;
    private ResourceLocation modelLegsTexture;
    
    public RendererKawaiiGrill()
	{
		if (instance == null) 
		{
			instance = this;

			model = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/grill.base.obj"));
			modelLegs = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/grill.legs.obj"));
			modelTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/grill.base.png");
			modelLegsTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/grill.legs.png");
		}
	}
    
	public static void register() 
	{
		new RendererKawaiiGrill();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiGrill.class, instance);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		int meta = te.getBlockMetadata();
	
		RenderHelper.disableStandardItemLighting();
		renderModel(model, modelTexture, x, y + 1.0d/16.0d, z, meta);
		
		Material lower = te.getWorldObj().getBlock(te.xCoord, te.yCoord - 1, te.zCoord).getMaterial();
		if (lower == Material.fire || lower == Material.air)
			renderModel(modelLegs, modelLegsTexture, x, y - 1, z, meta);
		
		if (te != null && te instanceof TileEntityKawaiiGrill)
		{
			TileEntityKawaiiGrill grill = (TileEntityKawaiiGrill) te;
			
			TexturedIcon[] display = grill.getDisplayItems();
			for (int i = 0; i < 4; i++)
				if (display[i] != null)
					renderItem(display[i].icon, x + ((i & 1) > 0 ? -0.225d : 0.225d), y + 3.0d/15.0d, z + ((i & 2) > 0 ? -0.225d : 0.225d), meta, 0.4f, display[i].texture);
		}
	}
	
	private void renderItem(IIcon icon, double x, double y, double z, int meta, float scale, ResourceLocation texture)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef((meta * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslated(-0.5d, 0.0d, -0.5d);
		GL11.glRotatef(90, 1, 0, 0);
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1.0f / 16.0f);
		GL11.glPopMatrix();
	}
	
	private void renderModel(IModelCustom model, ResourceLocation texture, double x, double y, double z, int meta)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		float scale = 1.0f / 15.0f; // Special scaling
		
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef(((meta + 2) * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(scale, scale, scale);
		model.renderAll();
		GL11.glPopMatrix();
	}
}
