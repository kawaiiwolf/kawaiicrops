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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class NEIRecipeHandlerKawaiiCuttingBoard extends TemplateRecipeHandler
{
	private static ArrayList<PositionedStack> cookingBlock = new ArrayList<PositionedStack>();
	static
	{
		cookingBlock.add(new PositionedStack(new ItemStack(ModBlocks.cuttingBoard), 76, 14));
	}

	public class CachedCuttingBoardRecipe extends CachedRecipe
    {
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private PositionedStack output;
		
		public CachedCuttingBoardRecipe(RecipeKawaiiCuttingBoard recipe)
		{
			this.input.add(new PositionedStack(recipe.input.get(0), 34, 24));
			this.output = new PositionedStack(recipe.output, 119, 24);
		}
		
        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, input);
        }
		
		@Override
		public PositionedStack getResult() 
		{
			return output;
		}
		
		@Override
        public List<PositionedStack> getOtherStacks() 
        {
            return getCycledIngredients(cycleticks / 20, cookingBlock);
        }
    }
	
	@Override
    public void loadCraftingRecipes(ItemStack result)
    {
		for (RecipeKawaiiCookingBase r : RecipeKawaiiCuttingBoard.allRecipes)
			if (r != null && r instanceof RecipeKawaiiCuttingBoard && NEIServerUtils.areStacksSameTypeCrafting(((RecipeKawaiiCuttingBoard)r).output, result))
				arecipes.add(new CachedCuttingBoardRecipe( (RecipeKawaiiCuttingBoard) r));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		boolean skip;
		
		if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.cuttingBoard))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiCuttingBoard.allRecipes)
				arecipes.add(new CachedCuttingBoardRecipe((RecipeKawaiiCuttingBoard)r));
		else
		for (RecipeKawaiiCookingBase r : RecipeKawaiiCuttingBoard.allRecipes)
			if (r != null && r instanceof RecipeKawaiiCuttingBoard)
			{
				skip = false;
				RecipeKawaiiCuttingBoard recipe = (RecipeKawaiiCuttingBoard) r;
				
				for (int i = 0; i < r.input.size(); i++)
					if (skip) break;
					else if (r.input.get(i) instanceof ItemStack && NEIServerUtils.areStacksSameTypeCrafting(ingredient, (ItemStack) r.input.get(i))) 
						arecipes.add(new CachedCuttingBoardRecipe(recipe));
					else if (r.input.get(i) instanceof List)
						for (ItemStack item : (ArrayList<ItemStack>)r.input.get(i))
							if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, item))
							{
								arecipes.add(new CachedCuttingBoardRecipe(recipe));
								skip = true;
								break;
							}
			}
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Cutting Board";
	}

	@Override
	public String getGuiTexture()
	{
		return Constants.MOD_ID + ":textures/gui/nei.png";
	}
}
