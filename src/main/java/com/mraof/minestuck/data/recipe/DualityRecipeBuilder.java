package com.mraof.minestuck.data.recipe;

import com.google.gson.JsonObject;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class DualityRecipeBuilder
{
	private final ItemStack object;
	private final ItemStack weapon;
	
	private DualityRecipeBuilder(ItemStack object, ItemStack weapon)
	{
		this.object = object;
		this.weapon = weapon;
	}
	
	public static DualityRecipeBuilder of(ItemLike object, ItemLike weapon) {
		return new DualityRecipeBuilder(new ItemStack(object.asItem()), new ItemStack(weapon.asItem()));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver)
	{
		ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(weapon.getItem()));
		build(recipeSaver, new ResourceLocation(name.getNamespace(), name.getPath()));
	}
	
	public void build(Consumer<FinishedRecipe> recipeSaver, ResourceLocation id)
	{
		recipeSaver.accept(new DualityRecipeBuilder.Result(new ResourceLocation(id.getNamespace(), "dualities/"+id.getPath()), object, weapon));
	}
	
	public static class Result implements FinishedRecipe
	{
		private final ResourceLocation id;
		private final ItemStack object;
		private final ItemStack weapon;
		
		public Result(ResourceLocation id, ItemStack object,  ItemStack weapon)
		{
			this.id = Objects.requireNonNull(id);
			this.object = Objects.requireNonNull(object);
			this.weapon = Objects.requireNonNull(weapon);
		}
		
		@Override
		public void serializeRecipeData(JsonObject json)
		{
			json.add("object", Ingredient.of(object).toJson());
			json.add("weapon", Ingredient.of(weapon).toJson());
		}
		
		@Override
		public ResourceLocation getId()
		{
			return id;
		}
		
		@Override
		public RecipeSerializer<?> getType()
		{
			return MSRecipeTypes.DUALITY.get();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement()
		{
			return null;
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId()
		{
			return null;
		}
	}
}
