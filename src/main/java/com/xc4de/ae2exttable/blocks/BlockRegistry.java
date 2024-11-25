package com.xc4de.ae2exttable.blocks;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;

public class BlockRegistry {
    public static CustomExtendedTable customExtendedTable = new CustomExtendedTable();
    public static void init() {
        final ModRegistry registry = AE2ExtendedCraftingTable.REGISTRY;
        registry.register(customExtendedTable, "custom_extended_table");
    }
}
