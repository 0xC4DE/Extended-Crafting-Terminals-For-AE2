package com._0xc4de.ae2exttable.client.gui.terminals;

import appeng.api.storage.ITerminalHost;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerBasicCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import com._0xc4de.ae2exttable.part.PartBasicCraftingTerminal;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiBasicCraftingTerminal extends GuiCraftingTerm {

    public GuiBasicCraftingTerminal(InventoryPlayer inventoryPlayer, final ITerminalHost te, ContainerBasicCraftingTerminal container) {
        super(inventoryPlayer, te, container, ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
        setGuiType(AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL);
    }

    @Override
    protected String getBackground() {
        return "textures/gui/basic_extended_crafting_terminal.png";
    }
}

