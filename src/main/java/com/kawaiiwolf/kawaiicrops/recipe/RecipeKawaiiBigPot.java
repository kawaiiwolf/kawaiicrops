package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeKawaiiBigPot extends RecipeKawaiiCookingBase 
{
	public static ArrayList<RecipeKawaiiCookingBase> allRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> oilRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> waterRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	public static ArrayList<RecipeKawaiiCookingBase> milkRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	
	public static String CookingOilItemsString = "";
	public static ArrayList<ItemStack> CookingOilItems = new ArrayList<ItemStack>();
	
	public static String CookingWaterItemsString = "";
	public static ArrayList<ItemStack> CookingWaterItems = new ArrayList<ItemStack>();
	
	public static String CookingMilkItemsString = "";
	public static ArrayList<ItemStack> CookingMilkItems = new ArrayList<ItemStack>();
	
	public int cookTime = 0;
	public int burnTime = 0;
	public boolean oil = false;
	public boolean water = true;
	public boolean milk = false;
	public boolean keepLiquid = false;
	public boolean texture = false;
	public ItemStack harvest = null;

	public RecipeKawaiiBigPot() {  }
	public RecipeKawaiiBigPot(ItemStack output, Object... input) 
	{
		super(output,input);
		setOptions(options);
		options = null;
	}

	@Override
	protected int getMaxIngredients() {	return 6; }

	@Override
	protected void setOptions(ArrayList<String> options) 
	{ 
		try
		{
			this.cookTime = Integer.parseInt(options.get(0));
			this.burnTime = Integer.parseInt(options.get(1));

			for (int i = 2; i < options.size(); i++)
			{
				String option = options.get(i).toLowerCase();
				if (option == null || option.isEmpty() || option.equals("|"))
					continue;
				else if (options.equals("water"))
				{
					water = true;
					oil = milk = false;
				}
				else if (option.equals("oil"))
				{
					oil = true;
					milk = water = false;
				}
				else if (option.equals("milk"))
				{
					milk = true;
					oil = water = false;
				}
				else if (option.equals("texture"))
					texture = true;
				else if (option.equals("keep"))
					keepLiquid = true;
				else
				{
					ItemStack item = NamespaceHelper.getItemByName(option);
					if (item != null)
						harvest = item;
					else
					{
						throw new RuntimeException("DUMMY");
					}
				}
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
	
	public ArrayList<RecipeKawaiiCookingBase> getFilteredRecipes(String liquid)
	{
		if (liquid.equals("oil"))
			return oilRecipes;
		if (liquid.equals("water"))
			return waterRecipes;
		if (liquid.equals("milk"))
			return milkRecipes;
		
		return allRecipes;
	}

	@Override
	public void register() 
	{
		allRecipes.add(this);
		if(this.oil)
			this.oilRecipes.add(this);
		else
			this.waterRecipes.add(this);
	}
	
	@Override
	public String toString()
	{
		return super.toString() +
				" | Cook: " + cookTime + 
				" | Burn: " + burnTime + 
				" | Liquid: " + (oil ? "oil" : "water") + 
				" | Texture: " + (texture ? "true" : "false") +
				" | Keep: " + (keepLiquid ? "true" : "false") +
				" | Harvest: " + (harvest == null ? "null" : NamespaceHelper.getItemName(harvest));
	}
}
