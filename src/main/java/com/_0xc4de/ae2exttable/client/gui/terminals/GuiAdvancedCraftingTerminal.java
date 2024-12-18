package com._0xc4de.ae2exttable.client.gui.terminals;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAdvancedCraftingTerminal extends GuiCraftingTerm {

    public GuiAdvancedCraftingTerminal(InventoryPlayer inventoryPlayer, final ITerminalHost te, ContainerAdvancedCraftingTerminal container) {
        super(inventoryPlayer, te, container, ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL);
        setGuiType(AE2ExtendedGUIs.ADVANCED_CRAFTING_TERMINAL);
    }

    @Override
    protected String getBackground() {
        return "textures/gui/advanced_extended_crafting_terminal.png";
    }
}

