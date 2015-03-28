package com.kawaiiwolf.kawaiicrops.renderer;

import org.lwjgl.opengl.GL11;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiBarrel.BarrelModel;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiBarrel;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RendererkawaiiBarrel extends TileEntitySpecialRenderer 
{
	public static RendererkawaiiBarrel instance = null;
    
	private IModelCustom modelBarrel;
    private ResourceLocation modelBarrelTexture;
    
    private IModelCustom modelCrate;
    private ResourceLocation modelCrateTexture;
	
	public RendererkawaiiBarrel()
	{
		if (instance == null) 
		{
			instance = this;

			modelBarrel = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/barrel.obj"));
			modelBarrelTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/barrel.png");
			modelCrate = AdvancedModelLoader.loadModel(new ResourceLocation(Constants.MOD_ID + ":model/crate.obj"));
			modelCrateTexture = new ResourceLocation(Constants.MOD_ID + ":textures/model/crate.png");
		}			
	}
	
	public static void register() 
	{
		new RendererkawaiiBarrel();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKawaiiBarrel.class, instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale)
	{
		final float modelScale = 1.0f / 16.0f;
		if (te instanceof TileEntityKawaiiBarrel)
		{
			TileEntityKawaiiBarrel b = (TileEntityKawaiiBarrel)te;
			int meta = b.getWorldObj().getBlockMetadata(b.xCoord, b.yCoord, b.zCoord);
			
			RenderHelper.disableStandardItemLighting();
			
			if (b.getModel() == BarrelModel.BARREL)
			{
				renderModel(modelBarrel, modelBarrelTexture, x, y, z, meta, modelScale, modelScale);
				renderLabelIcon(b.getLabel(), x, y + 6.0d/16d, z, meta, 0.25d, -0.001d);
				renderLabelIcon(b.getLabelBacking(), x, y + 6.0d/16d, z, meta, 0.25d, 0d);
			}
			if (b.getModel() == BarrelModel.CRATE)
			{
				renderModel(modelCrate, modelCrateTexture, x, y, z, meta, modelScale * 0.975f, modelScale);
				renderLabelIcon(b.getLabel(), x, y + 4.0d/16d, z, meta, 0.5d, 1.0d/16.0d - 0.001d);
				renderLabelIcon(b.getLabelBacking(), x, y + 4.0d/16d, z, meta, 0.5d, 1.0d/16.0d);
			}
		}
	}
	
	private void renderModel(IModelCustom model, ResourceLocation texture, double x, double y, double z, int meta, float xzScale, float yScale)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5d, y, z + 0.5d);
		GL11.glRotatef(((meta + 2) * 90.0f), 0.0f, 1.0f, 0.0f);
		GL11.glScalef(xzScale, yScale, xzScale);
		model.renderAll();
		GL11.glPopMatrix();
	}
	
	private void renderLabelIcon(IIcon icon, double x, double y, double z, int meta, double scale, double inset)
	{
		if (icon == null) return;
		
		x += ((meta & 2) == 0 ? -0.0001d : 1.0001d) + ((meta & 1) == 1 ? inset * ((meta & 2) == 0 ? 1 : -1) : (meta == 0 ? (1.0d - scale)/2.0d : -(1.0d - scale)/2.0d));
		z += (((meta & 1) == ((meta >> 1) & 1)) ? -0.0001d : 1.0001d) + ((meta & 1) == 0 ? inset * (((meta & 1) == ((meta >> 1) & 1)) ? 1 : -1 ) : (meta == 1 ? -(1.0d - scale)/2.0d : (1.0d - scale)/2.0d));
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glScaled(scale, scale, scale);
		GL11.glRotatef(meta * 90.0f, 0.0f, 1.0f, 0.0f);
		
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);

		Tessellator.instance.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMinV());
        Tessellator.instance.addVertexWithUV(1.0D, 1.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMinV());
        Tessellator.instance.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMaxV());
		
		Tessellator.instance.draw();
		GL11.glPopMatrix();
	}
}
