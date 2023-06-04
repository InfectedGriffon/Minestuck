package com.mraof.minestuck.jei;

import net.minecraft.world.item.ItemStack;

public class JeiDuality
{
	public static final String TITLE = "minestuck.jei.duality";
	
	private final ItemStack object;
	private final ItemStack weapon;
	
	public JeiDuality(ItemStack object, ItemStack weapon)
	{
		this.object = object;
		this.weapon = weapon.copy();
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
