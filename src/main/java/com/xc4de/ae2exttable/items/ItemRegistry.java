package com.xc4de.ae2exttable.items;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;

public class ItemRegistry {
    public static final ItemBasicCraftingTerminal itemBasicCraftingTerminal = new ItemBasicCraftingTerminal("basic_crafting_terminal");
    public static void init() {
        final ModRegistry registry = AE2ExtendedCraftingTable.REGISTRY;
        registry.register(itemBasicCraftingTerminal, "basic_crafting_terminal");
        //itemExtendedCraftingTerminal.initModel();
    }
}
