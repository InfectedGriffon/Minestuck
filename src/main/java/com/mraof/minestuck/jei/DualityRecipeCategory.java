package com.mraof.minestuck.jei;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.MSItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DualityRecipeCategory implements IRecipeCategory<JeiDuality>
{
	public static final String TITLE = "minestuck.jei.duality";
	private final IDrawable background, icon;
	
	DualityRecipeCategory(IGuiHelper guiHelper)
	{
		background = guiHelper.createDrawable(new ResourceLocation(Minestuck.MOD_ID, "textures/gui/duality.png"), 0, 0, 66, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MSItems.CROCKER_FORK.get()));
	}
	
	@Override
	public RecipeType<JeiDuality> getRecipeType()
	{
		return MinestuckJeiPlugin.DUALITY;
	}
	
	@Override
	public Component getTitle()
	{
		return Component.translatable(TITLE);
	}
	
	@Override
	public IDrawable getBackground()
	{
		return background;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, JeiDuality recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.getObject());
		builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(recipe.getObject());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 49, 1).addItemStack(recipe.getWeapon());
		builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(recipe.getWeapon());
	}
}
