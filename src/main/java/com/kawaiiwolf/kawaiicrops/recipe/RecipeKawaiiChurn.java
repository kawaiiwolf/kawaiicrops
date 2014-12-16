package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class RecipeKawaiiChurn extends RecipeKawaiiCookingBase 
{
	public static ArrayList<RecipeKawaiiCookingBase> allRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> churnRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> millRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	
	public int ChurnTime = 1;
	public boolean churn = true;
	public boolean mill = false;
	
	public RecipeKawaiiChurn() { }
	public RecipeKawaiiChurn(ItemStack result, Object... recipe) 
	{
		super(result, recipe);
		setOptions(options);
		options = null;
	}

	@Override
	protected int getMaxIngredients() {	return 1; }

	@Override
	protected void setOptions(ArrayList<String> options) 
	{
		try
		{
			for (String option : options)
			{
				if (option.equals("|"))
					continue;
				
				else if (option.equals("churn"))
					churn = !(mill = false);
				else if (option.equals("mill"))
					churn = !(mill = true);
				else
					ChurnTime = Integer.parseInt(option);
			}
		}
		catch (Exception exception)
		{
            String ret = "Invalid options: ";
            for (Object tmp : options)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
		}	
	}

	@Override
	public ArrayList<RecipeKawaiiCookingBase> getAllRecipes() 
	{
		return allRecipes;
	}

	@Override
	public void register() 
	{
		allRecipes.add(this);
		if (churn)
			churnRecipes.add(this);
		if (mill)
			millRecipes.add(this);
	}
}
