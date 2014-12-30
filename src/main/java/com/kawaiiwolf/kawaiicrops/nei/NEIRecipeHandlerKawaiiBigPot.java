package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.Util;
import com.kawaiiwolf.kawaiicrops.nei.NEIRecipeHandlerKawaiiFryingPan.CachedFryingPanRecipe;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class NEIRecipeHandlerKawaiiBigPot extends TemplateRecipeHandler
{
	private static ArrayList<PositionedStack> oilPot = new ArrayList<PositionedStack>();
	private static ArrayList<PositionedStack> waterPot = new ArrayList<PositionedStack>();
	private static ArrayList<PositionedStack> milkPot = new ArrayList<PositionedStack>();
	static
	{
		oilPot.add(new PositionedStack(new ItemStack(ModBlocks.bigPot), 76, 14));
		waterPot.add(new PositionedStack(new ItemStack(ModBlocks.bigPot), 76, 14));
		milkPot.add(new PositionedStack(new ItemStack(ModBlocks.bigPot), 76, 14));
		
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (ItemStack item : RecipeKawaiiBigPot.CookingOilItems)
			list.add(item);
		oilPot.add(new PositionedStack(list, 68, 42));

		list = new ArrayList<ItemStack>();
		for (ItemStack item : RecipeKawaiiBigPot.CookingMilkItems)
			list.add(item);
		milkPot.add(new PositionedStack(list, 68, 42));

		list = new ArrayList<ItemStack>();
		for (ItemStack item : RecipeKawaiiBigPot.CookingWaterItems)
			list.add(item);
		waterPot.add(new PositionedStack(list, 68, 42));
	}
	private enum Type { OIL, MILK, WATER };
	
	public class CachedBigPotRecipe extends CachedRecipe
    {
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private PositionedStack output;
		private Type type;
		public String display;
		
		public CachedBigPotRecipe(RecipeKawaiiBigPot recipe)
		{
			if (recipe.oil) type = Type.OIL;
			if (recipe.milk) type = Type.MILK;
			if (recipe.water) type = Type.WATER;

			this.output = new PositionedStack(recipe.output, 119, 24);
			this.display = (recipe.cookTime == 0 ? "" : "Cook Time: " + recipe.cookTime);
			
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
			else
			{
				for (int i = 0; i < 4; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), 25 + 18 * (i & 1), 6 + 18 * (i / 2)));
				for (int i = 4; i < size; i++)
					this.input.add(new PositionedStack(recipe.input.get(i), size == 5 ? 34 : (25 + 18 * (i & 1)), 6 + 18 * (i / 2)));
			}
			
			if (recipe.harvest != null)
				this.input.add(new PositionedStack(recipe.harvest, 90, 42));
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
			switch (type)
			{
				case WATER:	return getCycledIngredients(cycleticks / 20, waterPot);
				case MILK:	return getCycledIngredients(cycleticks / 20, milkPot);
				case OIL:	return getCycledIngredients(cycleticks / 20, oilPot);
			}
			return null;
        }
    }
	
	@Override
    public void loadCraftingRecipes(ItemStack result)
    {
		for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.allRecipes)
			if (r != null && r instanceof RecipeKawaiiBigPot && NEIServerUtils.areStacksSameTypeCrafting(((RecipeKawaiiBigPot)r).output, result))
				arecipes.add(new CachedBigPotRecipe( (RecipeKawaiiBigPot) r));
    }
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		if (ingredient.getItem() == Item.getItemFromBlock(ModBlocks.bigPot))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.allRecipes)
				arecipes.add(new CachedBigPotRecipe((RecipeKawaiiBigPot)r));
		else
		for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.allRecipes)
			if (r != null && r instanceof RecipeKawaiiBigPot)
			{
				RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) r;
				
				if (hasIngredient(recipe.input, ingredient) || (recipe.harvest != null && recipe.harvest.getItem() == ingredient.getItem() && recipe.harvest.getItemDamage() == ingredient.getItemDamage()))
					arecipes.add(new CachedBigPotRecipe(recipe));
			}
		
		if (Util.arrayContains(RecipeKawaiiBigPot.CookingOilItems, ingredient))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.oilRecipes)
				arecipes.add(new CachedBigPotRecipe((RecipeKawaiiBigPot)r));
		else if (Util.arrayContains(RecipeKawaiiBigPot.CookingMilkItems, ingredient))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.milkRecipes)
				arecipes.add(new CachedBigPotRecipe((RecipeKawaiiBigPot)r));
		else if (Util.arrayContains(RecipeKawaiiBigPot.CookingWaterItems, ingredient))
			for (RecipeKawaiiCookingBase r : RecipeKawaiiBigPot.waterRecipes)
				arecipes.add(new CachedBigPotRecipe((RecipeKawaiiBigPot)r));
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
		CachedBigPotRecipe r = (CachedBigPotRecipe)arecipes.get(recipe);
		
		if (r.display.length() > 0)
			Minecraft.getMinecraft().fontRenderer.drawString(r.display, 100, 6, 0x00000);
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Big Pot";
	}

	@Override
	public String getGuiTexture()
	{
		return Constants.MOD_ID + ":textures/gui/nei2.png";
	}
}
