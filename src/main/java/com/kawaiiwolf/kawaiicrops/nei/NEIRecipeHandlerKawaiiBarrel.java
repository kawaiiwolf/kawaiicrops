package com.kawaiiwolf.kawaiicrops.nei;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiBarrel;
import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

public class NEIRecipeHandlerKawaiiBarrel extends TemplateRecipeHandler 
{
	public class CachedBarrelRecipe extends CachedRecipe
	{
		private ArrayList<PositionedStack> input = new ArrayList<PositionedStack>();
		private ArrayList<PositionedStack> output = new ArrayList<PositionedStack>();
		public boolean requiredText = false;
		public boolean forbiddenText = false;
		
		public CachedBarrelRecipe(BlockKawaiiBarrel barrel)
		{
			input.add(new PositionedStack(new ItemStack(barrel), 34, 24));
			
			ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
			for (ItemStack i : barrel.UnfinishedDropTable.getDisplay()) drops.add(i);
			for (ItemStack i : barrel.FinishedDropTable.getDisplay()) drops.add(i);
			for (ItemStack i : barrel.RuinedDropTable.getDisplay()) drops.add(i);
			
			output.add(new PositionedStack(drops, 119, 24));
			
			if (!barrel.RequiredBlocks.isEmpty())
			{
				ArrayList<ItemStack> r = new ArrayList<ItemStack>();
				for (Block b : barrel.RequiredBlocks)
					r.add(new ItemStack(b));
				output.add(new PositionedStack(r, 77, 15));
				
				requiredText = true;
			}
			
			if (!barrel.ForbiddenBlocks.isEmpty())
			{
				ArrayList<ItemStack> f = new ArrayList<ItemStack>();
				for (Block b : barrel.ForbiddenBlocks)
					f.add(new ItemStack(b));
				output.add(new PositionedStack(f, 77, 42));
				
				forbiddenText = true;
			}
		}
		
        @Override
        public List<PositionedStack> getIngredients() {
            return input;
        }
		
		@Override
		public PositionedStack getResult() 
		{
			return null;
		}
		
		@Override
        public List<PositionedStack> getOtherStacks() 
        {
            return getCycledIngredients(cycleticks / 20, output);
        }
		
	}
	
	@Override
    public void loadCraftingRecipes(ItemStack result)
    {
		for (BlockKawaiiBarrel b : ModBlocks.AllBarrels)
			if (barrelContains(b,result))
				arecipes.add(new CachedBarrelRecipe((BlockKawaiiBarrel)b));
    }
	
	private boolean barrelContains(BlockKawaiiBarrel barrel, ItemStack item)
	{
		for (ItemStack i : barrel.UnfinishedDropTable.getDisplay())
			if (item.getItem() == i.getItem() && (item.getItemDamage() == OreDictionary.WILDCARD_VALUE || item.getItemDamage() == i.getItemDamage()))
				return true;
		for (ItemStack i : barrel.FinishedDropTable.getDisplay())
			if (item.getItem() == i.getItem() && (item.getItemDamage() == OreDictionary.WILDCARD_VALUE || item.getItemDamage() == i.getItemDamage()))
				return true;
		for (ItemStack i : barrel.RuinedDropTable.getDisplay())
			if (item.getItem() == i.getItem() && (item.getItemDamage() == OreDictionary.WILDCARD_VALUE || item.getItemDamage() == i.getItemDamage()))
				return true;
		return false;
	}
	
	@Override
    public void loadUsageRecipes(ItemStack ingredient) 
	{
		Block b = Block.getBlockFromItem(ingredient.getItem());
		if (b instanceof BlockKawaiiBarrel)
			arecipes.add(new CachedBarrelRecipe((BlockKawaiiBarrel)b));
	}
	
	@Override
	public void drawExtras(int recipe)
	{
		if (((CachedBarrelRecipe)arecipes.get(recipe)).requiredText)
			Minecraft.getMinecraft().fontRenderer.drawString("Required", 62, 6, 0x00000);
		
		if (((CachedBarrelRecipe)arecipes.get(recipe)).forbiddenText)
			Minecraft.getMinecraft().fontRenderer.drawString("Forbidden", 62, 34, 0x00000);
	}
	
	@Override
	public String getRecipeName() 
	{
		return "Aged Barrels";
	}

	@Override
	public String getGuiTexture() 
	{
		return Constants.MOD_ID + ":textures/gui/nei.png";
	}

}
