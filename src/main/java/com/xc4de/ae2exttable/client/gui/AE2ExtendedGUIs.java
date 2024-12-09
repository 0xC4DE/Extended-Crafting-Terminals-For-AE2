package com.xc4de.ae2exttable.client.gui;

public enum AE2ExtendedGUIs {
    DEFAULT,
    BASIC_CRAFTING_TERMINAL(3,3),
    ADVANCED_CRAFTING_TERMINAL(5,5),
    ELITE_CRAFTING_TERMINAL(7,7),
    ULTIMATE_CRAFTING_TERMINAL(9,9),

    // Begin wireless section
    WIRELESS_BASIC_CRAFTING_TERMINAL(3,3),
    WIRELESS_ADVANCED_CRAFTING_TERMINAL(5,5),
    WIRELESS_ELITE_CRAFTING_TERMINAL(7,7),
    WIRELESS_ULTIMATE_CRAFTING_TERMINAL(9,9);

    private final int gridSize;
    private final int gridX;
    private final int gridY;

    AE2ExtendedGUIs() {
        this.gridSize = 0;
        this.gridY = 0;
        this.gridX = 0;
    }

    AE2ExtendedGUIs(int gridX, int gridY) {
        this.gridSize = gridX*gridY;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
