package com.kawaiiwolf.kawaiicrops.lib;

import java.util.Iterator;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class NamespaceHelper {
	
	public static String getBlockName(Block block){
		return GameData.getBlockRegistry().getNameForObject(block);
	}
	
	public static Block getBlockByName(String name) {
		return GameData.getBlockRegistry().getObject(name);
	}
	
	public static Iterator<Block> getBlockIterator() {
		return GameData.getBlockRegistry().iterator();
	}
	
	public static String getItemName(Item item) {
		if (item == null) return null;
		return GameData.getItemRegistry().getNameForObject(item);
	}
	
	public static Item getItemByName(String name) {
		if (name == null) return null;
		return GameData.getItemRegistry().getObject(name);
	}
	
	public static Iterator<Item> getItemIterator() {
		return GameData.getItemRegistry().iterator();
	}
	
}
