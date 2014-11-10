package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import net.minecraft.item.Item;

public class RecipeKawaiiBigPot extends RecipeKawaiiCookingBase 
{
	private static ArrayList<RecipeKawaiiCookingBase> allRecipies = new ArrayList<RecipeKawaiiCookingBase>();
	
	public static String CookingOilItemsString = "";
	public static ArrayList<Item> CookingOilItems = new ArrayList<Item>(); 
	public static String CookingWaterItemsString = "";
	public static ArrayList<Item> CookingWaterItems = new ArrayList<Item>(); 

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
