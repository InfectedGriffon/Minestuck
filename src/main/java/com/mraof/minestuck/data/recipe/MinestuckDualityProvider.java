package com.mraof.minestuck.data.recipe;

import com.mraof.minestuck.item.MSItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class MinestuckDualityProvider extends RecipeProvider
{
	public MinestuckDualityProvider(DataGenerator pGenerator)
	{
		super(pGenerator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
	{
		
		DualityRecipeBuilder.of(MSItems.KISSY_CUTIE_HEART_HITTER.get(), MSItems.KISSY_CUTIE_HEART_SPLITTER.get()).build(consumer);
		
		DualityRecipeBuilder.of(MSItems.ACE_OF_SPADES.get(), MSItems.HORSE_HITCHER.get()).build(consumer);
		DualityRecipeBuilder.of(MSItems.ACE_OF_CLUBS.get(), MSItems.CLUB_OF_FELONY.get()).build(consumer);
		DualityRecipeBuilder.of(MSItems.ACE_OF_DIAMONDS.get(), MSItems.CUESTICK.get()).build(consumer);
		
		DualityRecipeBuilder.of(MSItems.CROCKER_SPOON.get(), MSItems.CROCKER_FORK.get()).build(consumer);
		DualityRecipeBuilder.of(MSItems.EDISONS_SERENITY.get(), MSItems.EDISONS_FURY.get()).build(consumer);
		
		DualityRecipeBuilder.of(MSItems.LIPSTICK.get(), MSItems.LIPSTICK_CHAINSAW.get()).build(consumer);
	}
	
	@Override
	public String getName()
	{
		return "Weapon/Object Duality";
	}
}
