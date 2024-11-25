package com.xc4de.ae2exttable.client.gui;

import appeng.client.gui.widgets.GuiScrollbar;
import appeng.client.gui.widgets.IScrollSource;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;

public class GuiScrollBar extends GuiScrollbar implements IScrollSource {
    private int displayX = 0;
    private int displayY = 0;
    private int width = 12;
    private int height = 16;
    private int pageSize = 1;
    private int maxScroll = 0;
    private int minScroll = 0;
    private int currentScroll = 0;

    public GuiScrollBar(int x, int y, int height) {
        this.displayX = x;
        this.displayY = y;
        this.height = height;
    }

    public void draw(GuiBase gui){
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/gui/container/creative_inventory/tabs.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (this.getRange() == 0) {
            gui.drawTexturedModalRect(this.displayX, this.displayY,232 + this.width, 0, this.width, 15);
        } else {
            int offset = (this.currentScroll - this.minScroll) * (this.height - 15) / this.getRange();
            gui.drawTexturedModalRect(this.displayX, offset+this.displayY, 232, 0, this.width, 15);
        }
    }

    public int getRange() {
        return this.maxScroll - this.minScroll;
    }

    public void applyRange() {
        this.currentScroll = Math.max(Math.min(this.currentScroll, this.maxScroll), this.minScroll);
    }

    public void click(int mouseY) {
        if (this.getRange() == 0){
            return;
        }
        this.currentScroll = mouseY - this.displayY;
        this.currentScroll = this.minScroll + ((this.currentScroll * 2 * this.getRange() / this.height));
        this.currentScroll = (this.currentScroll + 1) >> 1;
    }

    public void wheel(float delta) {
        delta = Math.max(Math.min(-delta, 1), -1);
        this.currentScroll += delta * this.pageSize;
        this.applyRange();
    }

    @Override
    public int getCurrentScroll() {
        return this.currentScroll;
    }
}
