package com.kawaiiwolf.kawaiicrops.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public abstract class RecipeKawaiiCookingBase 
{
    public ItemStack output = null;
    public ArrayList<Object> input = new ArrayList<Object>();
    protected ArrayList<String> options = null;
    
    public static String CookingHeatSourcesString = "";
    public static ArrayList<Block> CookingHeatSources = new ArrayList<Block>();
    
    public static String CookingFireString = "";
    public static ArrayList<Block> CookingFire = new ArrayList<Block>();

    protected RecipeKawaiiCookingBase() { }
	protected RecipeKawaiiCookingBase(ItemStack result, Object... recipe)
    {
        output = result.copy();
        options = new ArrayList<String>();
        boolean onOptions = false;
        int ingredientCount = 0;
        
        for (Object in : recipe)
        {
            if (!onOptions && in instanceof ItemStack)
            {
                input.add(((ItemStack)in).copy());
            }
            else if (!onOptions && in instanceof Item)
            {
                input.add(new ItemStack((Item)in));
            }
            else if (!onOptions && in instanceof Block)
            {
                input.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
            	if ((String)in == "|")
            		onOptions = true;
            	else if (!onOptions && OreDictionary.getOres((String)in) != null)
            		input.add(OreDictionary.getOres((String)in));
            	else
            	{
            		onOptions = true;
            		options.add((String)in);
            	}
            }
            else
            {
                String ret = "Invalid recipe: ";
                for (Object tmp :  recipe)
                    ret += tmp + ", ";
                ret += output;
                throw new RuntimeException(ret);
            }
            ingredientCount++;
            if (ingredientCount >= getMaxIngredients())
            	onOptions = true;
        }
        // Because java is dumb, make sure children call:
        //   setOptions(options);
    }
	
	protected abstract int getMaxIngredients();
	
	protected abstract void setOptions(ArrayList<String> options);
	
	public abstract ArrayList<RecipeKawaiiCookingBase> getAllRecipes();
	
	public abstract void register();
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isFullMatch(List<ItemStack> ingredients)
	{
		return matches(ingredients) == 0;
	}
	
	public boolean isPartialMatch(List<ItemStack> ingredients)
	{
		return matches(ingredients) > 0;
	}
	
	// returns number of ingredients remaining unmatched, -1 on mismatch
    public int matches(List<ItemStack> ingredients)
    {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (ItemStack ingredient : ingredients)
        {
            if (ingredient != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = OreDictionary.itemMatches((ItemStack)next, ingredient, true);
                    }
                    else if (next instanceof ArrayList)
                    {
                        Iterator<ItemStack> itr = ((ArrayList<ItemStack>)next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = OreDictionary.itemMatches(itr.next(), ingredient, true);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return -1;
                }
            }
        }
        
        return required.size();
    }
    
    @Override
    public String toString()
    {
    	String code = NamespaceHelper.getItemName(output.getItem()) + " [" + output.stackSize + "] = ";
    	for (int i = 0; i < input.size(); i++)
    	{
    		Object o = input.get(i);
    		if (o instanceof ItemStack)
    			code += NamespaceHelper.getItemName((ItemStack) o);
    		else if (o instanceof Block)
    			code += NamespaceHelper.getBlockName((Block) o);
    		else if (o instanceof Item)
    			code += NamespaceHelper.getItemName((Item) o);
    		else
    			code += o.toString();
    		if (i + 1 != input.size())
    			code += ", ";
    	}
    	return code;
    }
    		
	@Override
	public int hashCode()
	{
		if (cashedHashCode == null)
			cashedHashCode = toString().hashCode();
		return cashedHashCode.intValue();
	}
	private Integer cashedHashCode = null;
}
