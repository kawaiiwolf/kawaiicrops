package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeHelper {

	public static boolean register2x2recipe(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length != 6) 
			return false;
		
		// Parse Output Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;
		
		// Parse Input
		ArrayList<Object> inputParts = new ArrayList<Object>();
		String inputMap = ""; 
		int inputMapIndex = 1;
		for(int i = 2; i < parts.length; i++)
		{
			
			if(parts[i].toLowerCase().equals("nothing"))
			{
				inputMap += " ";
			} 
			else 
			{
				Object param = parseIngredient(parts[i]);
				if (param == null)
					return false;
				
				inputParts.add(param);
				inputMap += "" + (inputMapIndex++);
			}
		}
		
		ArrayList<Object> input = new ArrayList<Object>();
		input.add(inputMap.substring(0, 2));
		input.add(inputMap.substring(2, 4));
		for (int i = 0; i < inputParts.size(); i++)
		{
			input.add(("" + (1 + i)).charAt(0));
			input.add(inputParts.get(i));
		}
		
		ItemStack output = (outputType == IngredientType.ITEM ? 
				new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
				new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));		
		
		GameRegistry.addRecipe(new ShapedOreRecipe(output, input.toArray()));
		
		return true;
	}
		
	public static boolean register3x3recipe(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length != 11) 
			return false;
		
		// Parse Output Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;
		
		// Parse Input
		ArrayList<Object> inputParts = new ArrayList<Object>();
		String inputMap = ""; 
		int inputMapIndex = 1;
		for(int i = 2; i < parts.length; i++)
		{
			
			if(parts[i].toLowerCase().equals("nothing"))
			{
				inputMap += " ";
			} 
			else 
			{
				Object param = parseIngredient(parts[i]);
				if (param == null)
					return false;
				
				inputParts.add(param);
				inputMap += "" + (inputMapIndex++);
			}
		}
		
		ArrayList<Object> input = new ArrayList<Object>();
		input.add(inputMap.substring(0, 3));
		input.add(inputMap.substring(3, 6));
		input.add(inputMap.substring(6, 9));
		for (int i = 0; i < inputParts.size(); i++)
		{
			input.add(("" + (1 + i)).charAt(0));
			input.add(inputParts.get(i));
		}
		
		ItemStack output = (outputType == IngredientType.ITEM ? 
				new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
				new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));		
		
		GameRegistry.addRecipe(new ShapedOreRecipe(output, input.toArray()));
		
		return true;
	}
	
	public static boolean registerShapelessRecipe(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length < 3) 
			return false;
		
		// Parse Output Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;
		
		// Parse Input
		ArrayList<Object> input = new ArrayList<Object>();
		for(int i = 2; i < parts.length; i++)
		{
			Object param = parseIngredient(parts[i]);
			if (param == null)
				return false;
			input.add(param);
		}

		ItemStack output = (outputType == IngredientType.ITEM ? 
			new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
			new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(output, input.toArray()));
		
		return true;
	}
	
	public static boolean registerSmeltingRecipe(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length != 3) 
			return false;
		
		// Parse Resulting Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;
		
		// Parse Input Type
		
		IngredientType inputType = parseIngredientType(parts[2]);
		if (inputType != IngredientType.ITEM && inputType != IngredientType.BLOCK)
			return false;
		
		ItemStack input = (inputType == IngredientType.ITEM ? 
				new ItemStack(NamespaceHelper.getItemByName(parts[2])) : 
				new ItemStack(NamespaceHelper.getBlockByName(parts[2])));

		ItemStack output = (outputType == IngredientType.ITEM ? 
				new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
				new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));

		GameRegistry.addSmelting(input, output, 0.1F);
		
		return true;
	}

	public static boolean registerCustomCuttingBoardRecpie(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length != 3) 
			return false;
		
		// Parse Output Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;

		
		// Parse Input Type
		IngredientType inputType = parseIngredientType(parts[2]);
		if (inputType == IngredientType.ERROR)
			return false;
		
		ItemStack output = (outputType == IngredientType.ITEM ? 
				new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
				new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));
		
		(new RecipeKawaiiCuttingBoard(output, parseIngredient(parts[2]))).register();
		
		return true;
	}
	
	public static boolean registerCustomFryingPanRecipe(String recipe)
	{
		String[] parts = recipe.trim().replaceAll("  ", " ").split("[ ]");
		if (parts.length < 5) 
			return false;
		
		// Parse Output Type
		IngredientType outputType = parseIngredientType(parts[0]);
		if (outputType != IngredientType.ITEM && outputType != IngredientType.BLOCK)
			return false;
		
		// Parse Output Number
		int outputNum;
		try {
			outputNum = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) { return false; }
		if (outputNum < 1 || outputNum > 64) return false;

		int ingredients = 0;
		boolean onOptions = false;
		ArrayList<Object> params = new ArrayList<Object>();
		
		for (int i = 2; i < parts.length; i++)
		{
			IngredientType type = parseIngredientType(parts[i]);
			
			// Items can be either ingredients or options (harvest item)
			if (type == IngredientType.ITEM)
			{
				if (!onOptions)
				{
					params.add(NamespaceHelper.getItemByName(parts[i]));
					ingredients++;
				}
				else
					params.add(parts[i]);
			}
			// BLocks can only be ingredients
			else if (!onOptions && type == IngredientType.BLOCK)
			{
				params.add(NamespaceHelper.getBlockByName(parts[i]));
				ingredients++;
			}
			else 
			{
				// If not block or item, and it's an ore type, add it as an ingredient. 
				if (!onOptions && type == IngredientType.ORE && !isNumber(parts[i]))
				{
					params.add(parts[i]);
					ingredients++;
				}
				// The first two options MUST be numbers, the cook times
				else if (!onOptions && isNumber(parts[i]))
				{
					if (i + 1 >= parts.length || !isNumber(parts[i + 1]))
						return false;
					params.add("|");
					params.add(parts[i++]);
					params.add(parts[i]);
					onOptions = true;
				}
				// Options ! It's not valid unless the first were numbers
				else
				{
					if(!onOptions) 
						return false;
					params.add(parts[i]);
				}
			}
		}
		
		if (ingredients < 1 || ingredients > 3)
			return false;
		
		try
		{
			ItemStack output = (outputType == IngredientType.ITEM ? 
					new ItemStack(NamespaceHelper.getItemByName(parts[0]),outputNum) : 
					new ItemStack(NamespaceHelper.getBlockByName(parts[0]),outputNum));
			
			(new RecipeKawaiiFryingPan(output,params.toArray())).register();
		} 
		catch (Exception exception) 
		{ 
			return false; 
		}
		
		return true;
	}
	
	private enum IngredientType { ITEM, BLOCK, ORE, ERROR };
	
	private static IngredientType parseIngredientType(String name)
	{
		Object o = NamespaceHelper.getItemByName(name);
		if (o != null) 
			return IngredientType.ITEM;

		o = NamespaceHelper.getBlockByName(name);
		if (o != null && o != Blocks.air) 
			return IngredientType.BLOCK;
		
		if (OreDictionary.getOres(name) != null)
			return IngredientType.ORE;
		
		return IngredientType.ERROR;
	}
	
	private static Object parseIngredient(String name)
	{
		Object o = NamespaceHelper.getItemByName(name);
		if (o != null) 
			return o;

		o = NamespaceHelper.getBlockByName(name);
		if (o != null && o != Blocks.air) 
			return o;
		
		if (OreDictionary.getOres(name) != null)
			return name;
		
		return null;
	}
	
	private static boolean isNumber(String s)
	{
		try
		{
			Integer.parseInt(s);
		}
		catch (Exception e) { return false;}
		return true;
	}
	
}
