package com.kawaiiwolf.kawaiicrops.renderer;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderingHandlerKawaiiCropBlocks implements ISimpleBlockRenderingHandler {

	private static int renderId = 0;
	
	public static RenderingHandlerKawaiiCropBlocks instance = null;
	
	public RenderingHandlerKawaiiCropBlocks() {
		if (instance == null) {
			instance = this;
			renderId = RenderingRegistry.getNextAvailableRenderId();
		}
	}
	
	public static void register() {
		new RenderingHandlerKawaiiCropBlocks();
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
			renderCropBlock(world, x, y, z, (BlockKawaiiCrop)block, modelId, renderer);
		
		if (block != null && block instanceof BlockKawaiiTreeBlocks)
			renderTreeBlock(world, x, y, z, (BlockKawaiiTreeBlocks)block, modelId, renderer);
		
		return true;
	}
	
	private void renderTreeBlock(IBlockAccess world, int x, int y, int z, BlockKawaiiTreeBlocks block, int modelId, RenderBlocks renderer) 
	{
		Tessellator tessellator = Tessellator.instance;
        
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        switch (block.getState(world, x, y, z))
        {
        	case SAPLING: 
        		drawCross(tessellator, renderer.getIconSafe(block.getSaplingIcon()), x, y, z, 1.0f);
        		break;
        		
        	case FRUIT:
        	case FRUITRIPE:
        		drawCross(tessellator, renderer.getIconSafe(block.getFruitForStage(world, x, y, z)), x, y, z, 1.0f);
        		break;

        	case LEAF:
        		drawBlock(tessellator, renderer.getIconSafe(block.getLeafIcon()), x, y, z);
        		break;

        	case FRUITLEAF:
        	case FRUITLEAFRIPE:
        		drawBlock(tessellator, renderer.getIconSafe(block.getLeafIcon()), x, y, z, 1.0D);
        		drawBlock(tessellator, renderer.getIconSafe(block.getFruitForStage(world, x, y, z)), x, y, z, 0.875D);
        		break;
        }
	}

	public void renderCropBlock(IBlockAccess world, int x, int y, int z, BlockKawaiiCrop block, int modelId, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;
        
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        
        IIcon iicon = renderer.getIconSafe(block.getIcon(world, x, y, z, 0));

        switch (block.RenderType){
			case CROSS:
				drawCross(tessellator, iicon, x, y, z, 1.0f);
				break;
			case HASH:
				drawHash(tessellator, iicon, x, y, z);
				break;
			case BLOCK:
				drawBlock(tessellator, iicon, x, y, z);
				break;
			default:
				break;
        }
	}
	
    public void drawHash(Tessellator tessellator, IIcon iicon, double x, double y, double z)
    {
        drawFaceMirror(tessellator, iicon, x + 0.25D, y + 0.0D, z + 0.0D, x + 0.25D, y + 1.0D, z + 0.0D, x + 0.25D, y + 1.0D, z + 1.0D);
        drawFaceMirror(tessellator, iicon, x + 0.75D, y + 0.0D, z + 0.0D, x + 0.75D, y + 1.0D, z + 0.0D, x + 0.75D, y + 1.0D, z + 1.0D);
        drawFaceMirror(tessellator, iicon, x + 0.0D, y + 0.0D, z + 0.25D, x + 0.0D, y + 1.0D, z + 0.25D, x + 1.0D, y + 1.0D, z + 0.25D);
        drawFaceMirror(tessellator, iicon, x + 0.0D, y + 0.0D, z + 0.75D, x + 0.0D, y + 1.0D, z + 0.75D, x + 1.0D, y + 1.0D, z + 0.75D);
    }

    public void drawCross(Tessellator tessellator, IIcon iicon, double x, double y, double z, double scale)
    {
        double minX = x + 0.5D - 0.45D * scale;
        double maxX = x + 0.5D + 0.45D * scale;
        double minY = y;
        double maxY = y + scale;
        double minZ = z + 0.5D - 0.45D * scale;
        double maxZ = z + 0.5D + 0.45D * scale;

        drawFace(tessellator, iicon,minX, minY, minZ,minX, maxY, minZ,maxX, maxY, maxZ);
        drawFace(tessellator, iicon,maxX, minY, minZ,maxX, maxY, minZ,minX, maxY, maxZ);     
    }
    
    public void drawBlock(Tessellator tessellator, IIcon iicon, double x, double y, double z)
    {
    	drawBlock (tessellator, iicon, x, y, z, 1.0d);
    }

    public void drawBlock(Tessellator tessellator, IIcon iicon, double x, double y, double z, double scale)
    {
    	double lower = 0.5D - (0.5D * scale);
    	double upper = 0.5D + (0.5D * scale);
    	
    	drawFaceSingle(tessellator, iicon, x + lower, y + lower, z + lower, x + lower, y + upper, z + lower, x + upper, y + upper, z + lower);
    	drawFaceSingle(tessellator, iicon, x + upper, y + lower, z + lower, x + upper, y + upper, z + lower, x + upper, y + upper, z + upper);
    	drawFaceSingle(tessellator, iicon, x + upper, y + lower, z + upper, x + upper, y + upper, z + upper, x + lower, y + upper, z + upper);
    	drawFaceSingle(tessellator, iicon, x + lower, y + lower, z + upper, x + lower, y + upper, z + upper, x + lower, y + upper, z + lower);
    	drawFaceSingle(tessellator, iicon, x + upper, y + upper, z + upper, x + upper, y + upper, z + lower, x + lower, y + upper, z + lower);
    	drawFaceSingle(tessellator, iicon, x + lower, y + lower, z + upper, x + lower, y + lower, z + lower, x + upper, y + lower, z + lower);
    }
    
    /*
     *      u   U
     *    v 2---3
     *      | / |
     *    V 1---+
     */
    public void drawFace(Tessellator tessellator, IIcon iicon, 
    		double x1, double y1, double z1, 
    		double x2, double y2, double z2, 
    		double x3, double y3, double z3)
    {
        double minU = (double)iicon.getMinU();
        double minV = (double)iicon.getMinV();
        double maxU = (double)iicon.getMaxU();
        double maxV = (double)iicon.getMaxV();
    	
    	tessellator.addVertexWithUV(x1, y1, z1, minU, maxV);
    	tessellator.addVertexWithUV(x2, y2, z2, minU, minV);
    	tessellator.addVertexWithUV(x3, y3, z3, maxU, minV);
    	tessellator.addVertexWithUV(x1 + x3 - x2, y1 + y3 - y2, z1 + z3 - z2, maxU, maxV);
    	
    	tessellator.addVertexWithUV(x1, y1, z1, minU, maxV);
    	tessellator.addVertexWithUV(x1 + x3 - x2, y1 + y3 - y2, z1 + z3 - z2, maxU, maxV);
    	tessellator.addVertexWithUV(x3, y3, z3, maxU, minV);
    	tessellator.addVertexWithUV(x2, y2, z2, minU, minV);
    }
    
    public void drawFaceSingle(Tessellator tessellator, IIcon iicon, 
    		double x1, double y1, double z1, 
    		double x2, double y2, double z2, 
    		double x3, double y3, double z3)
    {
        double minU = (double)iicon.getMinU();
        double minV = (double)iicon.getMinV();
        double maxU = (double)iicon.getMaxU();
        double maxV = (double)iicon.getMaxV();
    	
    	tessellator.addVertexWithUV(x1, y1, z1, maxU, maxV);
    	tessellator.addVertexWithUV(x2, y2, z2, maxU, minV);
    	tessellator.addVertexWithUV(x3, y3, z3, minU, minV);
    	tessellator.addVertexWithUV(x1 + x3 - x2, y1 + y3 - y2, z1 + z3 - z2, minU, maxV);
    	
    }    
    
    public void drawFaceMirror(Tessellator tessellator, IIcon iicon, 
    		double x1, double y1, double z1, 
    		double x2, double y2, double z2, 
    		double x3, double y3, double z3)
    {
        double minU = (double)iicon.getMinU();
        double minV = (double)iicon.getMinV();
        double maxU = (double)iicon.getMaxU();
        double maxV = (double)iicon.getMaxV();
    	
    	tessellator.addVertexWithUV(x1, y1, z1, maxU, maxV);
    	tessellator.addVertexWithUV(x2, y2, z2, maxU, minV);
    	tessellator.addVertexWithUV(x3, y3, z3, minU, minV);
    	tessellator.addVertexWithUV(x1 + x3 - x2, y1 + y3 - y2, z1 + z3 - z2, minU, maxV);
    	
    	tessellator.addVertexWithUV(x1, y1, z1, minU, maxV);
    	tessellator.addVertexWithUV(x1 + x3 - x2, y1 + y3 - y2, z1 + z3 - z2, maxU, maxV);
    	tessellator.addVertexWithUV(x3, y3, z3, maxU, minV);
    	tessellator.addVertexWithUV(x2, y2, z2, minU, minV);
    }
}

