package com.xc4de.ae2exttable.items;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.items.terminals.ItemAdvancedCraftingTerminal;
import com.xc4de.ae2exttable.items.terminals.ItemBasicCraftingTerminal;
import com.xc4de.ae2exttable.items.terminals.ItemEliteCraftingTerminal;
import com.xc4de.ae2exttable.items.terminals.ItemUltimateCraftingTerminal;
import com.xc4de.ae2exttable.items.wireless.ItemWirelessAdvancedTerminal;
import com.xc4de.ae2exttable.items.wireless.ItemWirelessBasicTerminal;
import com.xc4de.ae2exttable.items.wireless.ItemWirelessEliteTerminal;
import com.xc4de.ae2exttable.items.wireless.ItemWirelessUltimateTerminal;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {
    private static final Map<Item, AE2ExtendedGUIs> FORWARD_LOOKUP = new HashMap<>();
    private static final Map<AE2ExtendedGUIs, Item> REVERSE_LOOKUP = new HashMap<>();

    // Wired terms go here
    public static ItemBasicCraftingTerminal BASIC_TERMINAL = new ItemBasicCraftingTerminal("basic_crafting_terminal");
    public static ItemAdvancedCraftingTerminal ADVANCED_TERMINAL = new ItemAdvancedCraftingTerminal("advanced_crafting_terminal");
    public static ItemEliteCraftingTerminal ELITE_TERMINAL = new ItemEliteCraftingTerminal("elite_crafting_terminal");
    public static ItemUltimateCraftingTerminal ULTIMATE_TERMINAL = new ItemUltimateCraftingTerminal("ultimate_crafting_terminal");

    // Wireless terms go here
    public static ItemWirelessBasicTerminal WIRELESS_BASIC_TERMINAL = new ItemWirelessBasicTerminal();
    public static ItemWirelessAdvancedTerminal WIRELESS_ADVANCED_TERMINAL = new ItemWirelessAdvancedTerminal();
    public static ItemWirelessEliteTerminal WIRELESS_ELITE_TERMINAL = new ItemWirelessEliteTerminal();
    public static ItemWirelessUltimateTerminal WIRELESS_ULTIMATE_TERMINAL = new ItemWirelessUltimateTerminal();

    public static void init() {
        final ModRegistry registry = AE2ExtendedCraftingTable.REGISTRY;
        registry.register(BASIC_TERMINAL, "basic_crafting_terminal");
        REVERSE_LOOKUP.put(AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL, BASIC_TERMINAL);

        registry.register(ADVANCED_TERMINAL, "advanced_crafting_terminal");
        REVERSE_LOOKUP.put(AE2ExtendedGUIs.ADVANCED_CRAFTING_TERMINAL, ADVANCED_TERMINAL);

        registry.register(ELITE_TERMINAL, "elite_crafting_terminal");
        REVERSE_LOOKUP.put(AE2ExtendedGUIs.ELITE_CRAFTING_TERMINAL, ELITE_TERMINAL);

        registry.register(ULTIMATE_TERMINAL, "ultimate_crafting_terminal");
        REVERSE_LOOKUP.put(AE2ExtendedGUIs.ULTIMATE_CRAFTING_TERMINAL, ULTIMATE_TERMINAL);

        registry.register(WIRELESS_BASIC_TERMINAL, "wireless_basic_crafting_terminal");
        FORWARD_LOOKUP.put(WIRELESS_BASIC_TERMINAL, AE2ExtendedGUIs.WIRELESS_BASIC_CRAFTING_TERMINAL);

        registry.register(WIRELESS_ADVANCED_TERMINAL, "wireless_advanced_crafting_terminal");
        FORWARD_LOOKUP.put(WIRELESS_ADVANCED_TERMINAL, AE2ExtendedGUIs.WIRELESS_ADVANCED_CRAFTING_TERMINAL);

        registry.register(WIRELESS_ELITE_TERMINAL, "wireless_elite_crafting_terminal");
        FORWARD_LOOKUP.put(WIRELESS_ELITE_TERMINAL, AE2ExtendedGUIs.WIRELESS_ELITE_CRAFTING_TERMINAL);

        registry.register(WIRELESS_ULTIMATE_TERMINAL, "wireless_ultimate_crafting_terminal");
        FORWARD_LOOKUP.put(WIRELESS_ULTIMATE_TERMINAL, AE2ExtendedGUIs.WIRELESS_ULTIMATE_CRAFTING_TERMINAL);

        initModels();
    }

    public static void initModels() {
        BASIC_TERMINAL.initModel();
        ADVANCED_TERMINAL.initModel();
        ELITE_TERMINAL.initModel();
        ULTIMATE_TERMINAL.initModel();
    }

    // ONLY returns the ITEM for the GUI type, cannot do wireless terminals
    public static Item partByGuiType(AE2ExtendedGUIs guiType) {
        return REVERSE_LOOKUP.get(guiType);
    }

    public static AE2ExtendedGUIs guiByItem(Item item) {
        return FORWARD_LOOKUP.get(item);
    }
}
