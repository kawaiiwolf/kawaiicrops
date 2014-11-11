package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeKawaiiFryingPan extends RecipeKawaiiCookingBase 
{
	private static ArrayList<RecipeKawaiiCookingBase> allRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	private static ArrayList<RecipeKawaiiCookingBase> oilRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	private static ArrayList<RecipeKawaiiCookingBase> cleanRecipes = new ArrayList<RecipeKawaiiCookingBase>();
	
	public static String CookingOilItemsString = "";
	public static ArrayList<Item> CookingOilItems = new ArrayList<Item>();
	
	public int cookTime = 0;
	public int burnTime = 0;
	public boolean oil = false;
	public boolean greasy = false;
	public boolean texture = false;
	public Item harvest = null;

	public RecipeKawaiiFryingPan() {  }
	public RecipeKawaiiFryingPan(ItemStack output, Object... input) 
	{
		super(output,input);
		setOptions(options);
		options = null;
	}

	@Override
	protected int getMaxIngredients() {	return 3; }

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
				else if (option.equals("oil"))
					oil = true;
				else if (option.equals("texture"))
					texture = true;
				else if (option.equals("greasy"))
					greasy = true;
				else
				{
					Item item = NamespaceHelper.getItemByName(option);
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
	
	public ArrayList<RecipeKawaiiCookingBase> getFilteredRecipes(boolean oil)
	{
		if (oil)
			return oilRecipes;
		else
			return cleanRecipes;
	}

	@Override
	public void register() 
	{
		allRecipes.add(this);
		if(this.oil)
			this.oilRecipes.add(this);
		else
			this.cleanRecipes.add(this);
	}
	
	@Override
	public String toString()
	{
		return super.toString() +
				" | Cook: " + cookTime + 
				" | Burn: " + burnTime + 
				" | Oil: " + (oil ? "true" : "false") + 
				" | Texture: " + (texture ? "true" : "false") +
				" | Greasy: " + (greasy ? "true" : "false") +
				" | Harvest: " + (harvest == null ? "null" : NamespaceHelper.getItemName(harvest));
	}
}
