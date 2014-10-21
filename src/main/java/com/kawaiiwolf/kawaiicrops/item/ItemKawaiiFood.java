package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemKawaiiFood extends ItemFood {
	
	public String ToolTipText = "";
	public String Name = "";
	public PotionEffectHelper Potion = null;
	public boolean DrinkEffect = false;
	public boolean EatAnytime = false;
	public String OreDict = "";

	public ItemKawaiiFood(String name, String toolTip, int hunger, float saturation)
	{
		this(name, toolTip, hunger, saturation, null);
	}
	

	public ItemKawaiiFood(String name, String toolTip, int hunger, float saturation, PotionEffectHelper potion) {
		this(name, toolTip, hunger, saturation, potion, false, false);
	}

	public ItemKawaiiFood(String name, String toolTip, int hunger, float saturation, PotionEffectHelper potion, boolean drink, boolean eatAnytime) {
		super(hunger, saturation, false);
		
		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.Name = name;
		this.Potion = potion;
		this.ToolTipText = toolTip;
		this.DrinkEffect = drink;
		this.EatAnytime = eatAnytime;
		
		if (eatAnytime) 
			setAlwaysEdible();
		
		ModItems.ModFoods.add(this);
	}
	
	@Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return (DrinkEffect ? EnumAction.drink : EnumAction.eat);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if (ToolTipText.length() > 0)
			list.add(ToolTipText);
	}

	@Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
		if (player.canEat(false) && !world.isRemote && this.Potion != null)
        	for (com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper.Potion p : this.Potion.Effects)
        		if (world.rand.nextFloat() < p.Chance)
                	player.addPotionEffect(p.getPotionEffect());
		
		return super.onEaten(stack, world, player);
    }
	

}