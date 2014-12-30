package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NamespaceHelper {
	
	public static String getBlockName(Block block){
		return GameData.getBlockRegistry().getNameForObject(block);
	}
	
	public static Block getBlockByName(String name) {
		return GameData.getBlockRegistry().getObject(name);
	}
	
	public static ArrayList<Block> getBlocksByName(String names) { return getBlocksByName(names,"[ ]"); }
	public static ArrayList<Block> getBlocksByName(String names, String separator)
	{
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (String name : names.split(separator))
		{
			Block block = getBlockByName(name);
			if (block != Block.getBlockById(0))
				blocks.add(block);
		}
		
		return blocks;
	}
	
	public static Iterator<Block> getBlockIterator() {
		return GameData.getBlockRegistry().iterator();
	}
	
	public static String getItemName(ItemStack item) { return getItemName (item.getItem()); }
	public static String getItemName(Item item) 
	{
		if (item == null) return null;
		return GameData.getItemRegistry().getNameForObject(item);
	}
	
	public static String getItemShortName(ItemStack item) { return getItemShortName(item.getItem()); }
	public static String getItemShortName(Item item) 
	{
		String s = getItemName(item);
		return s.substring(s.indexOf(":") + 1);
	}
	
	public static ItemStack getItemByName(String name) {
		if (name == null) 
			return null;
		
		int meta = 0;
		if (name.contains(Constants.META))
		{
			try
			{
				meta = Integer.parseInt(name.substring(name.indexOf(Constants.META) + 1));
			} catch (Exception e) {}
			name = name.substring(0, name.indexOf(Constants.META));
		}
		
		Item item = GameData.getItemRegistry().getObject(name);
		
		if (item == null) 
			return null;
		
		return new ItemStack(GameData.getItemRegistry().getObject(name), 1, meta);
	}
	
	public static ArrayList<ItemStack> getItemsByName(String names) { return getItemsByName(names,"[ ,]"); }
	public static ArrayList<ItemStack> getItemsByName(String names, String separator)
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		for (String name : names.split(separator))
		{
			ItemStack item = getItemByName(name);
			if (item != null)
				items.add(item);
		}
		
		return items;
	}
	
	public static Iterator<Item> getItemIterator() 
	{
		return GameData.getItemRegistry().iterator();
	}
	
	public static boolean isItemBlock(ItemStack item) { return isItemBlock(item.getItem()); }
	public static boolean isItemBlock(Item item) 
	{
		return Block.getBlockFromItem(item) != Blocks.air;
	}

	public static String getItemLocalizedName(Item item) { return getItemLocalizedName(new ItemStack(item)); }
	public static String getItemLocalizedName(ItemStack item) 
	{
		return item.getDisplayName();
	}
	
	public static String getBlockLocalizedName(Block block)
	{
		return StatCollector.translateToLocal(block.getUnlocalizedName() + ".name");
	}

	private interface IItemNameTransformer	{ public String transform(Item item); }
	private IItemNameTransformer[] transformers = 
	{
		new IItemNameTransformer() { public String transform(Item item) { return item.getUnlocalizedName() + ".name"; }}
	};
}








