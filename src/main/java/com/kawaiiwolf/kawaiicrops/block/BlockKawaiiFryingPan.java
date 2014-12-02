package com.kawaiiwolf.kawaiicrops.block;

import java.util.HashMap;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiFryingPan;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockKawaiiFryingPan extends BlockKawaiiCookingBlock {

	public static HashMap<RecipeKawaiiFryingPan,IIcon> FoodTextures;
	
	protected BlockKawaiiFryingPan() 
	{
		super(Material.iron, "fryingpan", true);
		
		maxY = 4.0d / 16.0d;
		minX = minZ = 1.0d / 16.0d;
		maxX = maxZ = 1.0d - minX;
		
		this.setBlockTextureName(Constants.MOD_ID + ":fryingpan");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiFryingPan();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
		super.registerBlockIcons(register);
		
		FoodTextures = new HashMap<RecipeKawaiiFryingPan,IIcon>();
		
		for (RecipeKawaiiCookingBase base : (new RecipeKawaiiFryingPan()).getAllRecipes())
		{
			RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan)base;
			
			if (recipe.texture)
				FoodTextures.put(recipe, register.registerIcon(Constants.MOD_ID + ":" + NamespaceHelper.getItemShortName(recipe.output) + ".fryingpan"));
		}
    }
}
