package com.kawaiiwolf.kawaiicrops.block;

import java.util.HashMap;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiBigPot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockKawaiiBigPot extends BlockKawaiiCookingBlock 
{
	public static HashMap<RecipeKawaiiBigPot,IIcon> FoodTextures;
	public static IIcon MilkTexture;
	public static IIcon OilTexture;
	public static IIcon WaterTexture;
	
	protected BlockKawaiiBigPot()
	{
		super(Material.iron, "bigpot", true);
		
		this.setBlockTextureName(Constants.MOD_ID + ":bigpot");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiBigPot();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
		super.registerBlockIcons(register);
		
		FoodTextures = new HashMap<RecipeKawaiiBigPot,IIcon>();
		
		MilkTexture = register.registerIcon(Constants.MOD_ID + ":liquid.milk");
		OilTexture = register.registerIcon(Constants.MOD_ID + ":liquid.oil");
		WaterTexture = register.registerIcon(Constants.MOD_ID + ":liquid.water");
		
		for (RecipeKawaiiCookingBase base : (new RecipeKawaiiBigPot()).getAllRecipes())
		{
			RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot)base;
			
			if (recipe.texture)
				FoodTextures.put(recipe, register.registerIcon(Constants.MOD_ID + ":" + NamespaceHelper.getItemShortName(recipe.output) + ".bigpot"));
		}
    }

}
