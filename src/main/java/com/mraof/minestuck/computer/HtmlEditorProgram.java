package com.mraof.minestuck.computer;

import com.mojang.blaze3d.platform.NativeImage;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HtmlEditorProgram extends ComputerProgram
{
	private final int WIDTH = 158, HEIGHT = 100;
	
	private final ResourceLocation resource = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/output.png");
	
	private boolean isEditing = false;
	private EditBox textField;
	
	public HtmlEditorProgram() { renderHTML(""); }
	
	private void renderHTML(String htmlContent)
	{
		var image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		var editorPane = new JEditorPane("text/html", htmlContent);
		editorPane.setSize(WIDTH, HEIGHT);
		editorPane.print(graphics);
		
		NativeImage base = new NativeImage(WIDTH, HEIGHT, false);
		
		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < HEIGHT; j++)
				base.setPixelRGBA(i, j, image.getRGB(i, j));
		
		Minecraft.getInstance().textureManager.register(resource, new DynamicTexture(base));
		
		graphics.dispose();
	}
	
	public void toggleEditing()
	{
		if (isEditing) {
			isEditing = false;
			textField.setVisible(false);
			renderHTML(textField.getValue());
		} else {
			isEditing = true;
			textField.setVisible(true);
		}
	}
	
	@Override
	public void onInitGui(ComputerScreen gui)
	{
		var xOffset = (gui.width - ComputerScreen.xSize) / 2;
		var yOffset = (gui.height - ComputerScreen.ySize) / 2;
		
		textField = gui.addRenderableWidget(new EditBox(Minecraft.getInstance().font, xOffset+9, yOffset+58, WIDTH, HEIGHT, Component.empty()));
		textField.setMaxLength(250);
		textField.setBordered(false);
		textField.setVisible(false);
		
		gui.addRenderableWidget(new ExtendedButton(xOffset+9, yOffset+38, 40, 20, Component.literal("edit"), b -> toggleEditing()));
	}
	
	@Override
	public void paintGui(GuiGraphics graphics, ComputerScreen gui, ComputerBlockEntity be)
	{
		var xOffset = (gui.width - ComputerScreen.xSize) / 2;
		var yOffset = (gui.height - ComputerScreen.ySize) / 2;
		
		if(!isEditing)
			graphics.blit(resource, xOffset + 9, yOffset + 58, 0, 0, WIDTH, HEIGHT);
	}
}
