package com.xc4de.ae2exttable.client.gui;

import com.xc4de.ae2exttable.util.Vector2D;

// Stores ONLY the numbers for drawing the GUI, it's all hardcoded but it works.
// These are ALL magic numbers by nature, it's all measured from the texture ahead of time.
public enum ExtendedCraftingGUIConstants {
    // AE2 crafting terminal shares this
    BASIC_CRAFTING_TERMINAL(256,256, 230, 241, 37, -72, 92, 156, 0, 0,131,-54,73, 3, Integer.MAX_VALUE),
    ADVANCED_CRAFTING_TERMINAL(512,512, 230, 269, 15, -103, 107, 186, 10-9, 0, 143,-68,101, 3, Integer.MAX_VALUE),
    ELITE_CRAFTING_TERMINAL(512,512, 230, 303, 9, -138, 135, 223, 17-9, 0, 165,-85,136, 3, Integer.MAX_VALUE),
    ULTIMATE_CRAFTING_TERMINAL(512,512, 230, 340, 9, -174, 172, 258, 38-9, 0, 206,156,172, 3, Integer.MAX_VALUE);


    public final Vector2D textureSize;
    public final Vector2D textureActualSize; // Because the textures have to be either 256x256 or 512x512 (rendering issues) this defines the real size of the texture that it takes

    // X value is defined as the offset from the top left of the "inside" of the box to the far left side of the GUI
    // Y value is defined as the offset from the top pixel of the players inventory texture (the dark one) to the same pixel, it also must be negative because of offsets
    public final Vector2D craftingGridOffset; // Offset for the ACTUAL grid to render, like the interactable one.
    public final Vector2D clearButtonOffset;
    public final Vector2D inventoryOffset;
    // Xvalue is defined as the fourth dark pixel from the left of the texture
    // Yvalue is calculated like the Y value for crafting grid offset
    public final Vector2D outputSlotOffset;

    // Somewhat nebulous argument that defines the space that the area with the crafting grid will take up
    // This is calculated as the Y value from the bottom dark pixel of the last ME Row BEFORE the crafting grid all the way down to the bottom minus 99, why 99? idk.
    // All I know is that it works when you do the above
    public final int reservedSpace;
    public final int minRows; // These are the rows of AE2 Items NOT anything with crafting
    public final int maxRows;
    ExtendedCraftingGUIConstants(int textureX, int textureY, int realTextureX, int realTextureY, int gridXOffset, int gridYOffset,
                                 int clearButtonX, int clearButtonY, int inventoryOffsetX, int inventoryOffsetY, int outputSlotX, int outputSlotY, int reservedSpace, int minRows, int maxRows) {
        this.textureSize = new Vector2D(textureX, textureY);
        this.textureActualSize = new Vector2D(realTextureX, realTextureY);
        this.craftingGridOffset = new Vector2D(gridXOffset, gridYOffset);
        this.clearButtonOffset = new Vector2D(clearButtonX, clearButtonY);
        this.inventoryOffset = new Vector2D(inventoryOffsetX, inventoryOffsetY);
        this.outputSlotOffset = new Vector2D(outputSlotX, outputSlotY);
        this.reservedSpace = reservedSpace;
        this.minRows = minRows;
        this.maxRows = maxRows;
    }
}