package com.kawaiiwolf.kawaiicrops.renderer;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderingHandlerKawaiiCropBlock implements ISimpleBlockRenderingHandler {

	private static int renderId = 0;
	
	public static RenderingHandlerKawaiiCropBlock instance = null;
	
	public RenderingHandlerKawaiiCropBlock() {
		if (instance == null) {
			instance = this;
			renderId = RenderingRegistry.getNextAvailableRenderId();
		}
	}
	
	public static void register() {
		new RenderingHandlerKawaiiCropBlock();
		RenderingRegistry.registerBlockHandler(instance.getRenderId(), instance);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) { }

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
		if (block != null && block instanceof BlockKawaiiCrop)
		{
	        Tessellator tessellator = Tessellator.instance;
	        
	        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
	        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
	        
	        IIcon iicon = renderer.getIconSafe(block.getIcon(world, x, y, z, 0));

	        switch (((BlockKawaiiCrop)block).RenderType){
				case CROSS:
					drawCross(tessellator, iicon, x, y, z, 1.0f);
					break;
				case HASH:
					drawHash(tessellator, iicon, x, y, z);
					break;
				default:
					break;
	        }
		}
		return true;
	}
	
    public void drawHash(Tessellator tessellator, IIcon iicon, double x, double y, double z)
    {
        double d3 = (double)iicon.getMinU();
        double d4 = (double)iicon.getMinV();
        double d5 = (double)iicon.getMaxU();
        double d6 = (double)iicon.getMaxV();
        double d7 = x + 0.5D - 0.25D;
        double d8 = x + 0.5D + 0.25D;
        double d9 = z + 0.5D - 0.5D;
        double d10 = z + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d5, d4);
        d7 = x + 0.5D - 0.5D;
        d8 = x + 0.5D + 0.5D;
        d9 = z + 0.5D - 0.25D;
        d10 = z + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + 1.0D, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + 1.0D, d10, d5, d4);
    }

    public void drawCross(Tessellator tessellator, IIcon iicon, double x, double y, double z, float height)
    {
        double d3 = (double)iicon.getMinU();
        double d4 = (double)iicon.getMinV();
        double d5 = (double)iicon.getMaxU();
        double d6 = (double)iicon.getMaxV();
        double d7 = 0.45D * (double)height;
        double d8 = x + 0.5D - d7;
        double d9 = x + 0.5D + d7;
        double d10 = z + 0.5D - d7;
        double d11 = z + 0.5D + d7;
        tessellator.addVertexWithUV(d8, y + (double)height, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d9, y + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d9, y + (double)height, d11, d5, d4);
        tessellator.addVertexWithUV(d9, y + (double)height, d11, d3, d4);
        tessellator.addVertexWithUV(d9, y + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + (double)height, d10, d5, d4);
        tessellator.addVertexWithUV(d8, y + (double)height, d11, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d11, d3, d6);
        tessellator.addVertexWithUV(d9, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d9, y + (double)height, d10, d5, d4);
        tessellator.addVertexWithUV(d9, y + (double)height, d10, d3, d4);
        tessellator.addVertexWithUV(d9, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d11, d5, d6);
        tessellator.addVertexWithUV(d8, y + (double)height, d11, d5, d4);
    }
}

