package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
	
	public static Item getItemByName(String name) {
		if (name == null) return null;
		return GameData.getItemRegistry().getObject(name);
	}
	
	public static ArrayList<Item> getItemsByName(String names) { return getItemsByName(names,"[ ,]"); }
	public static ArrayList<Item> getItemsByName(String names, String separator)
	{
		ArrayList<Item> items = new ArrayList<Item>();
		
		for (String name : names.split(separator))
		{
			Item item = getItemByName(name);
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
}
