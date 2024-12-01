package com.xc4de.ae2exttable.items;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;

public class ItemRegistry {
    public static ItemBasicCraftingTerminal BASIC_TERMINAL = new ItemBasicCraftingTerminal("basic_crafting_terminal");
    public static ItemAdvancedCraftingTerminal ADVANCED_TERMINAL = new ItemAdvancedCraftingTerminal("advanced_crafting_terminal");
    public static ItemEliteCraftingTerminal ELITE_TERMINAL = new ItemEliteCraftingTerminal("elite_crafting_terminal");
    public static ItemUltimateCraftingTerminal ULTIMATE_TERMINAL = new ItemUltimateCraftingTerminal("ultimate_crafting_terminal");

    public static void init() {
        final ModRegistry registry = AE2ExtendedCraftingTable.REGISTRY;
        registry.register(BASIC_TERMINAL, "basic_crafting_terminal");
        registry.register(ADVANCED_TERMINAL, "advanced_crafting_terminal");
        registry.register(ELITE_TERMINAL, "elite_crafting_terminal");
        registry.register(ULTIMATE_TERMINAL, "ultimate_crafting_terminal");
        initModels();
    }

    public static void initModels() {
        BASIC_TERMINAL.initModel();
        ADVANCED_TERMINAL.initModel();
        ELITE_TERMINAL.initModel();
        ULTIMATE_TERMINAL.initModel();
    }
}
