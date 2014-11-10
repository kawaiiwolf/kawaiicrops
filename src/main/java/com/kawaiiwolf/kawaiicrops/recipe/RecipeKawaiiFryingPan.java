package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeKawaiiFryingPan extends RecipeKawaiiCookingBase 
{
	private static ArrayList<RecipeKawaiiCookingBase> allRecipies = new ArrayList<RecipeKawaiiCookingBase>();
	
	public static String CookingOilItemsString = "";
	public static ArrayList<Item> CookingOilItems = new ArrayList<Item>();
	
	public int cookTime = 0;
	public int burnTime = 0;
	public boolean oil = false;
	public boolean texture = false;
	public Item harvest = null;

	public RecipeKawaiiFryingPan() {  }
	public RecipeKawaiiFryingPan(ItemStack output, Object... input) 
	{
		super(output,input);
	}

	@Override
	protected int getMaxIngredients() {	return 3; }

	@Override
	protected void setOptions(ArrayList<String> options) 
	{ 
		try
		{
			cookTime = Integer.parseInt(options.get(0));
			burnTime = Integer.parseInt(options.get(1));

			for (int i = 2; i < options.size(); i++)
			{
				String option = options.get(i).toLowerCase();
				if (option == null || option.isEmpty() || option.equals("|"))
					continue;
				else if (option.equals("oil"))
					oil = true;
				else if (option.equals("texture"))
					texture = true;
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
