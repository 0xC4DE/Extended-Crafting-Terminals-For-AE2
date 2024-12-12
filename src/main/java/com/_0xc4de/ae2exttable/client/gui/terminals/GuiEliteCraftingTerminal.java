package com._0xc4de.ae2exttable.client.gui.terminals;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerEliteCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiEliteCraftingTerminal extends GuiCraftingTerm {

    private GuiImgButton clearBtn;

    public GuiEliteCraftingTerminal(InventoryPlayer inventoryPlayer, final ITerminalHost te, ContainerEliteCraftingTerminal container) {
        super(inventoryPlayer, te, container, ExtendedCraftingGUIConstants.ELITE_CRAFTING_TERMINAL);
        setGuiType(AE2ExtendedGUIs.ELITE_CRAFTING_TERMINAL);
    }

    @Override
    protected String getBackground() {
        return "textures/gui/elite_extended_crafting_terminal.png";
    }
}

