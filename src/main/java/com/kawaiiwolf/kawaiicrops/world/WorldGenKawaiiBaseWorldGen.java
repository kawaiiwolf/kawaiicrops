package com.kawaiiwolf.kawaiicrops.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenKawaiiBaseWorldGen implements IWorldGenerator 
{
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		for (WorldGen gen : generators)
		{
			if (gen.weight <= 0 || rand.nextInt(gen.weight) != 0) continue;
			int x = 16 * chunkX + rand.nextInt(16);
			int z = 16 * chunkZ + rand.nextInt(16);
			
			if (world.getBiomeGenForCoords(x, z).temperature < gen.minTemperature) continue;
			if (world.getBiomeGenForCoords(x, z).temperature > gen.maxTemperature) continue;
			if (world.getBiomeGenForCoords(x, z).rainfall < gen.minRainfall) continue;
			if (world.getBiomeGenForCoords(x, z).rainfall > gen.maxRainfall) continue;
			if (gen.biomeBlacklist.contains(world.getBiomeGenForCoords(x, z).biomeName.toLowerCase())) continue;
			
			int y = getY(world, x, z, ((IWorldGenWithSoilBlocks)gen.generator).getSoil());
			
			if (y == -1) continue;
			
			gen.generator.generate(world, rand, x, y, z);
		}
	}

	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		GameRegistry.registerWorldGenerator(this, 20);
	}
	
	private int getY(World world, int x, int z, HashSet<Block> soil)
	{
		int y = world.getHeightValue(x, z);
		Block current = world.getBlock(x, y + 1, z), last;
		for (int failed = 0; y > 0 && failed < 16; y--, failed++)
		{
			last = current;
			current = world.getBlock(x, y, z);
			if (current == Blocks.air) continue;
			Material m = last.getMaterial();
			if (soil.contains(current) && (m == Material.air || m == Material.vine || m == Material.snow))
				return y + 1;
		}
		return -1;
	}
	
	public ArrayList<WorldGen> generators = new ArrayList<WorldGen>();
	public static class WorldGen
	{
		public WorldGenerator generator;
		public int weight = 0;
		public float minTemperature = 0.0f;
		public float maxTemperature = 1.0f;
		public float minRainfall = 0.0f;
		public float maxRainfall = 1.0f;
		public String biomeBlacklist = "";
		
		public WorldGen() { }
		
		public WorldGen(WorldGenerator generator, int weight, float minTemperature, float maxTemperature, float minRainfall, float maxRainfall, String biomeBlacklist)
		{
			assert generator instanceof IWorldGenWithSoilBlocks : "generator needs to implement IWorldGenWithSoilBlocks";
			
			this.generator = generator;
			this.weight = weight;
			this.minTemperature = minTemperature;
			this.maxTemperature = maxTemperature;
			this.minRainfall = minRainfall;
			this.maxRainfall = maxRainfall;
			this.biomeBlacklist = biomeBlacklist;
		}
	}
}
