package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

public class NEIRecipeHandlerKawaiiFryingPan  extends TemplateRecipeHandler 
{
	private static ArrayList<PositionedStack> cleanPan = new ArrayList<PositionedStack>();
	private static ArrayList<PositionedStack> oiledPan = new ArrayList<PositionedStack>();
	private static ArrayList<PositionedStack> steamer = new ArrayList<PositionedStack>();
	static
	{
		cleanPan.add(new PositionedStack(new ItemStack(ModBlocks.fryingPan), 76, 14));
		oiledPan.add(new PositionedStack(new ItemStack(ModBlocks.fryingPan), 76, 14));
		steamer.add(new PositionedStack(new ItemStack(ModBlocks.fryingPan), 76, 14));
		
		ArrayList<ItemStack> oil = new ArrayList<ItemStack>();
		for (Item item : RecipeKawaiiFryingPan.CookingOilItems)
			oil.add(new ItemStack(item));
		oiledPan.add(new PositionedStack(oil, 68, 42));
		
		steamer.add(new PositionedStack(new ItemStack(ModItems.Steamer), 68, 42));
	}

	public class CachedFryingPanRecipe extends CachedRecipe
    {
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private PositionedStack output;
		private boolean oiled;
		private boolean steam;
		public String display;
		
		public CachedFryingPanRecipe(RecipeKawaiiFryingPan recipe)
		{
			this.oiled = recipe.oil; 
			this.output = new PositionedStack(recipe.output, 119, 24);
			this.display = (recipe.cookTime == 0 ? "" : "Cook Time: " + recipe.cookTime);
			this.steam = recipe.steam;
			
			if (recipe.input.size() == 1)
				this.input.add(new PositionedStack(recipe.input.get(0), 34, 24));
			else if (recipe.input.size() == 2)
				for (int i = 0; i < 2; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 34, 14 + 21 * i));
			else if (recipe.input.size() == 3) 
				for (int i = 0; i < 3; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 34, 6 + 18 * i));
			
			if (recipe.harvest != null)
				this.input.add(new PositionedStack(new ItemStack(recipe.harvest), 90, 42));
		}
		
        @Override
        public List<PositionedStack> getIngredients() 
        {
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
            return getCycledIngredients(cycleticks / 20, oiled ? oiledPan : (steam ? steamer : cleanPan));
        }
    }
	
	@Override
    public void loadCraftingRecipes(ItemStack result)
    {
		for (RecipeKawaiiCookingBase r : RecipeKawaiiFryingPan.allRecipes)
			if (r != null && r instanceof RecipeKawaiiFryingPan && NEIServerUtils.areStacksSameTypeCrafting(((RecipeKawaiiFryingPan)r).output, result))
				arecipes.add(new CachedFryingPanRecipe( (RecipeKawaiiFryingPan) r));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.fryingPan))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiFryingPan.allRecipes)
				arecipes.add(new CachedFryingPanRecipe((RecipeKawaiiFryingPan)r));
		else if (ingredient.getItem() == ModItems.Steamer)
		{
			for (RecipeKawaiiCookingBase r : RecipeKawaiiFryingPan.allRecipes)
				if (((RecipeKawaiiFryingPan)r).steam)
					arecipes.add(new CachedFryingPanRecipe((RecipeKawaiiFryingPan)r));
		}
		else
		for (RecipeKawaiiCookingBase r : RecipeKawaiiFryingPan.allRecipes)
			if (r != null && r instanceof RecipeKawaiiFryingPan)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) r;
				
				if (hasIngredient(recipe.input, ingredient) || (recipe.harvest != null && recipe.harvest == ingredient.getItem()))
					arecipes.add(new CachedFryingPanRecipe(recipe));
			}
		
		if (RecipeKawaiiFryingPan.CookingOilItems.contains(ingredient.getItem()))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiFryingPan.oilRecipes)
				arecipes.add(new CachedFryingPanRecipe((RecipeKawaiiFryingPan)r));
	}
	
	private boolean hasIngredient(Object o, ItemStack ingredient)
	{
		if (o instanceof ItemStack)
			return NEIServerUtils.areStacksSameTypeCrafting((ItemStack)o, ingredient);
		else if (o instanceof List)
		{
			for (ItemStack item : (ArrayList<ItemStack>)o)
				if (NEIServerUtils.areStacksSameTypeCrafting(item, ingredient))
					return true;
		}
		return false;
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		CachedFryingPanRecipe r = (CachedFryingPanRecipe)arecipes.get(recipe);
		
		if (r.display.length() > 0)
			Minecraft.getMinecraft().fontRenderer.drawString(r.display, 100, 6, 0x00000);
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Frying Pan";
	}

	@Override
	public String getGuiTexture()
	{
		return Constants.MOD_ID + ":textures/gui/nei2.png";
	}
}
