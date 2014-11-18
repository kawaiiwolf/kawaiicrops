package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class RecipeKawaiiCuttingBoard extends RecipeKawaiiCookingBase 
{
	public static ArrayList<RecipeKawaiiCookingBase> allRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	
	public RecipeKawaiiCuttingBoard() { }
	public RecipeKawaiiCuttingBoard(ItemStack result, Object... recipe) 
	{
		super(result, recipe);
	}

	@Override
	protected int getMaxIngredients() {	return 1; }

	@Override
	protected void setOptions(ArrayList<String> options) { }

	@Override
	public ArrayList<RecipeKawaiiCookingBase> getAllRecipes() 
	{
		return allRecipes;
	}

	@Override
	public void register() 
	{
		allRecipes.add(this);
	}

}
