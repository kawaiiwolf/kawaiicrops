package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import scala.actors.threadpool.Arrays;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class NEIRecipeHandlerKawaiiCuttingBoard extends TemplateRecipeHandler
{
	private static PositionedStack cookingBlock = new PositionedStack(new ItemStack(ModBlocks.cuttingBoard), 76, 14);

	public class CachedCuttingBoardRecipe extends CachedRecipe
    {
		private PositionedStack input;
		private PositionedStack output;
		
		public CachedCuttingBoardRecipe(Object input, Object output)
		{
			this.input = new PositionedStack(input, 34, 24);
			this.output = new PositionedStack(output, 119, 24);
		}
		
		@Override
		public PositionedStack getIngredient()
		{
			return input;
		}
		
		@Override
		public PositionedStack getResult() 
		{
			return output;
		}
		
		@Override
        public PositionedStack getOtherStack() 
        {
            return cookingBlock;
        }
    }
	
	@Override
    public void loadCraftingRecipes(ItemStack result)
    {
		for (RecipeKawaiiCookingBase r : RecipeKawaiiCuttingBoard.allRecipies)
			if (r != null && r instanceof RecipeKawaiiCuttingBoard && ((RecipeKawaiiCuttingBoard)r).output.getItem() == result.getItem())
				arecipes.add(new CachedCuttingBoardRecipe(((RecipeKawaiiCuttingBoard)r).input.get(0), ((RecipeKawaiiCuttingBoard)r).output));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		arecipes.add(new CachedCuttingBoardRecipe(new ItemStack(ModItems.BurntFood), new ItemStack(ModItems.BurntFood)));

		// Guess you're doing it wrong ?  Start over !

	}
	
	@Override
	public String getRecipeName() 
	{
		return "My Custom Recipe ! <3";
	}

	@Override
	public String getGuiTexture() 
	{
		return Constants.MOD_ID + ":textures/gui/nei.png";
	}
}
