package com.xc4de.ae2exttable.client.gui;

public enum AE2ExtendedGUIs {
    BASIC_CRAFTING_TERMINAL(3,3),
    ADVANCED_CRAFTING_TERMINAL(5,5),
    ELITE_CRAFTING_TERMINAL(7,7),
    ULTIMATE_CRAFTING_TERMINAL(9,9);

    private final int gridSize;

    AE2ExtendedGUIs(int gridX, int gridY) {
        this.gridSize = gridX*gridY;
    }

    public int getGridSize() {
        return gridSize;
    }
}
