package com.xc4de.ae2exttable.client.gui;

import com.blakebr0.cucumber.util.Utils;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.container.ContainerCustomCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCustomCrafting extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Tags.MODID, "textures/gui/custom_crafting_table.png");

    public GuiCustomCrafting(ContainerCustomCrafting container) {
        super(container);
        this.xSize = 176;
        this.ySize = 170;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(Utils.localize("container.ae2exttable.custom_extended_table"), 32, 6, 4210752);
        this.fontRenderer.drawString(Utils.localize("container.inventory"), 8, this.ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
