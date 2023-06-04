package com.mraof.minestuck.item.crafting;

import com.google.gson.JsonObject;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.jei.JeiDuality;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

// TODO
// - find some way to make each item both input and output
// - some sort of distinction between sheathing
//

public class DualityRecipe implements Recipe<SimpleContainer>
{
	private final ResourceLocation id;
	private final ItemStack object;
	private final ItemStack weapon;
	
	public DualityRecipe(ResourceLocation id, ItemStack object, ItemStack weapon)
	{
		this.id = id;
		this.object = object;
		this.weapon = weapon;
	}
	
	@Override
	public boolean matches(SimpleContainer container, Level level)
	{
		return false;
	}
	
	@Override
	public ItemStack assemble(SimpleContainer pContainer)
	{
		return weapon;
	}
	
	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return true;
	}
	
	@Override
	public ItemStack getResultItem()
	{
		return weapon.copy();
	}
	
	@Override
	public ResourceLocation getId()
	{
		return id;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return MSRecipeTypes.DUALITY.get();
	}
	
	@Override
	public RecipeType<?> getType()
	{
		return MSRecipeTypes.DUALITY_TYPE.get();
	}
	
	public List<JeiDuality> getJeiForm() {
		return Collections.singletonList(new JeiDuality(object, weapon));
	}
	
	public static class Serializer implements RecipeSerializer<DualityRecipe> {
		public static final ResourceLocation ID = new ResourceLocation(Minestuck.MOD_ID, "duality");
		
		@Override
		public DualityRecipe fromJson(ResourceLocation id, JsonObject json)
		{
			var object = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("object"));
			var weapon = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("weapon"));
			return new DualityRecipe(id, object, weapon);
		}
		
		@Override
		public @Nullable DualityRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
		{
			var object = buf.readItem();
			var weapon = buf.readItem();
			return new DualityRecipe(id, object, weapon);
		}
		
		@Override
		public void toNetwork(FriendlyByteBuf buf, DualityRecipe recipe)
		{
			buf.writeItemStack(recipe.object, false);
			buf.writeItemStack(recipe.weapon, false);
		}
	}
	
}
