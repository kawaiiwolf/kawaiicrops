package com.kawaiiwolf.kawaiicrops.world;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

public class WorldGenKawaiiCrop extends WorldGenerator implements IWorldGenWithSoilBlocks {

	private BlockKawaiiCrop crop;
	
	public WorldGenKawaiiCrop (BlockKawaiiCrop crop)
	{
		this.crop = crop;
	}
	
	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) 
	{
		if (!crop.CropGrowsOn.contains(world.getBlock(x, y - 1, z))) 
			return false;

		Material m = world.getBlock(x, y, z).getMaterial();
		if (m != Material.air && m != Material.vine && m != Material.snow)
			return false;
		
		world.setBlock(x, y, z, crop, 7, 3);
		return true;
	}

	@Override
	public HashSet<Block> getSoil() {
		return crop.CropGrowsOn;
	}
}
