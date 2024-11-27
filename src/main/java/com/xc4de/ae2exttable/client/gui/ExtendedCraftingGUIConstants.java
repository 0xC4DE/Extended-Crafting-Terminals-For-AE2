package com.xc4de.ae2exttable.client.gui;

import com.xc4de.ae2exttable.util.Vector2D;

// Stores ONLY the numbers for drawing the GUI, it's all hardcoded but it works.
// These are ALL magic numbers by nature, it's all measured from the texture ahead of time.
public enum ExtendedCraftingGUIConstants {
    // AE2 crafting terminal shares this
    BASIC_CRAFTING_TERMINAL(256,256, 230, 241, 0, 0, 92, 156, 0, 0,73, 3, Integer.MAX_VALUE),
    ADVANCED_CRAFTING_TERMINAL(512,512, 230, 269, 0, 0, 106, 186, 1, 0, 101, 3, Integer.MAX_VALUE),
    ELITE_CRAFTING_TERMINAL(512,512, 230, 241, 0, 0, 92, 156, 16, 0, 73, 3, Integer.MAX_VALUE),
    ULTIMATE_CRAFTING_TERMINAL(512,512, 230, 241, 0, 0, 92, 156, 37, 0, 73, 3, Integer.MAX_VALUE);


    public final Vector2D textureSize;
    public final Vector2D textureActualSize; // Because the textures have to be either 256x256 or 512x512 (rendering issues) this defines the real size of the texture that it takes
    public final Vector2D craftingGridOffset; // Offset for the ACTUAL grid to render, like the interactable one.
    public final Vector2D clearButtonOffset;
    public final Vector2D inventoryOffset;
    public final int reservedSpace;
    public final int minRows; // These are the rows of AE2 Items NOT anything with crafting
    public final int maxRows;
    ExtendedCraftingGUIConstants(int textureX, int textureY, int realTextureX, int realTextureY, int gridXOffset, int gridYOffset,
                                 int clearButtonX, int clearButtonY, int inventoryOffsetX, int inventoryOffsetY, int reservedSpace, int minRows, int maxRows) {
        this.textureSize = new Vector2D(textureX, textureY);
        this.textureActualSize = new Vector2D(realTextureX, realTextureY);
        this.craftingGridOffset = new Vector2D(gridXOffset, gridYOffset);
        this.clearButtonOffset = new Vector2D(clearButtonX, clearButtonY);
        this.inventoryOffset = new Vector2D(inventoryOffsetX, inventoryOffsetY);
        this.reservedSpace = reservedSpace;
        this.minRows = minRows;
        this.maxRows = maxRows;
    }
}