package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

public class RecipeKawaiiBigPot extends RecipeKawaiiCookingBase 
{
	private static ArrayList<RecipeKawaiiCookingBase> allRecipies = new ArrayList<RecipeKawaiiCookingBase>();

	@Override
	protected int getMaxIngredients() {	return 6; }

	@Override
	protected void setOptions(ArrayList<String> options) { }

	@Override
	public ArrayList<RecipeKawaiiCookingBase> getAllRecipies() 
	{
		return allRecipies;
	}

	@Override
	public void register() 
	{
		allRecipies.add(this);
	}


}
