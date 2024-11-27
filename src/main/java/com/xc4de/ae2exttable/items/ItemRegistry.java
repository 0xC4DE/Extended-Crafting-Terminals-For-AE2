package com.xc4de.ae2exttable.items;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;

public class ItemRegistry {
    public static void init() {
        final ModRegistry registry = AE2ExtendedCraftingTable.REGISTRY;
        registry.register(new ItemBasicCraftingTerminal("basic_crafting_terminal"), "basic_crafting_terminal");
        registry.register(new ItemAdvancedCraftingTerminal("advanced_crafting_terminal"), "advanced_crafting_terminal");
        //itemExtendedCraftingTerminal.initModel();
    }
}
