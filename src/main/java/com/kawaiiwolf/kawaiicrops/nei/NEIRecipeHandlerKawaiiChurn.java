package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiChurn;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class NEIRecipeHandlerKawaiiChurn extends TemplateRecipeHandler 
{
	private static ArrayList<PositionedStack> cookingBlock = new ArrayList<PositionedStack>();
	static
	{
		cookingBlock.add(new PositionedStack(new ItemStack(ModBlocks.churn), 76, 14));
	}

	public class CachedChurnRecipe extends CachedRecipe
    {
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private PositionedStack output;
		
		public CachedChurnRecipe(RecipeKawaiiChurn recipe)
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
		for (RecipeKawaiiCookingBase r : RecipeKawaiiChurn.allRecipes)
			if (r != null && r instanceof RecipeKawaiiChurn && NEIServerUtils.areStacksSameTypeCrafting(((RecipeKawaiiChurn)r).output, result))
				arecipes.add(new CachedChurnRecipe( (RecipeKawaiiChurn) r));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		boolean skip;
		
		if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.churn))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiChurn.allRecipes)
				arecipes.add(new CachedChurnRecipe((RecipeKawaiiChurn)r));
		else
		for (RecipeKawaiiCookingBase r : RecipeKawaiiChurn.allRecipes)
			if (r != null && r instanceof RecipeKawaiiChurn)
			{
				skip = false;
				RecipeKawaiiChurn recipe = (RecipeKawaiiChurn) r;
				
				for (int i = 0; i < r.input.size(); i++)
					if (skip) break;
					else if (r.input.get(i) instanceof ItemStack && NEIServerUtils.areStacksSameTypeCrafting(ingredient, (ItemStack) r.input.get(i))) 
						arecipes.add(new CachedChurnRecipe(recipe));
					else if (r.input.get(i) instanceof List)
						for (ItemStack item : (ArrayList<ItemStack>)r.input.get(i))
							if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, item))
							{
								arecipes.add(new CachedChurnRecipe(recipe));
								skip = true;
								break;
							}
			}
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Churn";
	}

	@Override
	public String getGuiTexture()
	{
		return Constants.MOD_ID + ":textures/gui/nei.png";
	}
}
