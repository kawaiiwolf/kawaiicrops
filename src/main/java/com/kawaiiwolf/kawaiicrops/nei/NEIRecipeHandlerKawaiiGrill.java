package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiGrill;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class NEIRecipeHandlerKawaiiGrill extends TemplateRecipeHandler 
{
	private static ArrayList<PositionedStack> cookingBlock = new ArrayList<PositionedStack>();
	private static PositionedStack fireBlock = null;;
	
	static
	{
		cookingBlock.add(new PositionedStack(new ItemStack(ModBlocks.grill), 76, 14));
	}
	public class CachedGrillRecipe extends CachedRecipe
    {
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private PositionedStack output;
		public String display;
		
		public CachedGrillRecipe(RecipeKawaiiGrill recipe)
		{
			this.output = new PositionedStack(recipe.output, 119, 24);
			this.display = (recipe.cookTime == 0 ? "" : (recipe.dry ? "Dry" : "Cook") + " Time: " + recipe.cookTime);
			
			int size = recipe.input.size();
			
			if (size == 1)
				this.input.add(new PositionedStack(recipe.input.get(0), 34, 24));
			else if (size == 2)
				for (int i = 0; i < 2; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 34, 14 + 21 * i));
			else if (size == 3) 
				for (int i = 0; i < 3; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 34, 6 + 18 * i));
			else if (size == 4)
				for (int i = 0; i < 4; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 25 + 18 * (i & 1), 14 + 21 * (i / 2)));
			
			if(fireBlock == null)
			{
				ArrayList<ItemStack> fire = new ArrayList<ItemStack>();
				for (Block block : RecipeKawaiiCookingBase.CookingFire)
					fire.add(new ItemStack(block));
				fireBlock = new PositionedStack(fire, 76, 34);
			}
			
			if (!recipe.dry && fireBlock != null)
				this.input.add(fireBlock);
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
		for (RecipeKawaiiCookingBase r : RecipeKawaiiGrill.allRecipes)
			if (r != null && r instanceof RecipeKawaiiGrill && NEIServerUtils.areStacksSameTypeCrafting(((RecipeKawaiiGrill)r).output, result))
				arecipes.add(new CachedGrillRecipe( (RecipeKawaiiGrill) r));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		boolean skip;
		
		if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.grill))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiGrill.allRecipes)
				arecipes.add(new CachedGrillRecipe((RecipeKawaiiGrill)r));
		else
		for (RecipeKawaiiCookingBase r : RecipeKawaiiGrill.allRecipes)
			if (r != null && r instanceof RecipeKawaiiGrill)
			{
				skip = false;
				RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) r;
				
				for (int i = 0; i < r.input.size(); i++)
					if (skip) break;
					else if (r.input.get(i) instanceof ItemStack && NEIServerUtils.areStacksSameTypeCrafting(ingredient, (ItemStack) r.input.get(i))) 
						arecipes.add(new CachedGrillRecipe(recipe));
					else if (r.input.get(i) instanceof List)
						for (ItemStack item : (ArrayList<ItemStack>)r.input.get(i))
							if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, item))
							{
								arecipes.add(new CachedGrillRecipe(recipe));
								skip = true;
								break;
							}
			}
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		CachedGrillRecipe r = (CachedGrillRecipe)arecipes.get(recipe);
		
		if (r.display.length() > 0)
			Minecraft.getMinecraft().fontRenderer.drawString(r.display, 100, 6, 0x00000);
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Grill";
	}

	@Override
	public String getGuiTexture()
	{
		return Constants.MOD_ID + ":textures/gui/nei.png";
	}

}
