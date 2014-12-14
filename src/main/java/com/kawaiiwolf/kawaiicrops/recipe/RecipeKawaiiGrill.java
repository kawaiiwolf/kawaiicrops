package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeKawaiiGrill extends RecipeKawaiiCookingBase 
{
	public static ArrayList<RecipeKawaiiCookingBase> allRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> hotRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> dryRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	
	public int cookTime = 0;
	public int burnTime = 0;
	public boolean dry = false;
	
	public RecipeKawaiiGrill() {}
	public RecipeKawaiiGrill(ItemStack output, Object... input) 
	{
		super(output,input);
		setOptions(options);
		options = null;
	}
	
	@Override
	protected int getMaxIngredients() { return 4; }

	@Override
	protected void setOptions(ArrayList<String> options) 
	{
		try
		{
			this.cookTime = Integer.parseInt(options.get(0));
			
			if (options.get(1).toLowerCase().equals("dry"))
				this.dry = true;
			else
				this.burnTime = Integer.parseInt(options.get(1));
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
	
	public ArrayList<RecipeKawaiiCookingBase> getFilteredRecipes(boolean dry)
	{
		if (dry)
			return dryRecipes;
		else
			return hotRecipes;
	}
	
	@Override
	public void register() 
	{
		allRecipes.add(this);
		if(this.dry)
			this.dryRecipes.add(this);
		else
			this.hotRecipes.add(this);
	}
	
	@Override
	public String toString()
	{
		return super.toString() +
				" | Cook: " + cookTime + 
				" | Burn: " + burnTime + 
				" | Dry: " + (dry ? "true" : "false");
	}
}
