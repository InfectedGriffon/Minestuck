package com.mraof.minestuck.jei;

import net.minecraft.world.item.ItemStack;

public class JeiDuality
{
	private final ItemStack object, weapon;
	
	public JeiDuality(ItemStack object, ItemStack weapon)
	{
		this.object = object;
		this.weapon = weapon;
	}
	
	public ItemStack getObject()
	{
		return object;
	}
	
	public ItemStack getWeapon()
	{
		return weapon;
	}
}
