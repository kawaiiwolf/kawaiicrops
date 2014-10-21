package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiCake;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockCake;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockKawaiiCake extends BlockCake {

	public String Name = "";
	public int Hunger = 2;
	public float Saturation = 0.1F;
	public boolean Enabled = true;
	public String ToolTipText = "";
	public PotionEffectHelper Potion = null;
	public String OreDict = "";
	
	private ItemKawaiiCake cake = null;
	
	public BlockKawaiiCake(String name)
	{
		super();
		this.Name = name;
		this.setBlockName(Constants.MOD_ID + "." + this.Name + ".cake");
		this.setBlockTextureName(Constants.MOD_ID + ":" + this.Name + ".cake");
	}
	
	public void register() 
	{
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		cake = new ItemKawaiiCake(this);
		cake.OreDict = OreDict;
		
		GameRegistry.registerItem(cake, Constants.MOD_ID + "." + this.Name + ".cake");
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
        this.onEat(world, x, y, z, player);
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        this.onEat(world, x, y, z, player);
    }

    private void onEat(World world, int x, int y, int z, EntityPlayer player)
    {
        if (player.canEat(false))
        {
            player.getFoodStats().addStats(this.Hunger, this.Saturation);

            if (!world.isRemote && this.Potion != null) {
            	for (com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper.Potion p : this.Potion.Effects)
            	{
            		if (world.rand.nextFloat() < p.Chance)
                    	player.addPotionEffect(p.getPotionEffect());
            	}
            }
            
            int l = world.getBlockMetadata(x, y, z) + 1;

            if (l >= 6)
            {
                world.setBlockToAir(x, y, z);
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y, z, l, 2);
            }
        }
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return cake;
    }
}
