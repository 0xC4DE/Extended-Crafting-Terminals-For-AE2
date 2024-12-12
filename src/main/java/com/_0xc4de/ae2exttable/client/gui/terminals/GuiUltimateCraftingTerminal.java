package com._0xc4de.ae2exttable.client.gui.terminals;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketInventoryAction;
import appeng.helpers.InventoryAction;
import com.blakebr0.cucumber.util.Utils;
import com._0xc4de.ae2exttable.Tags;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiUltimateCraftingTerminal extends GuiCraftingTerm {

    public GuiUltimateCraftingTerminal(InventoryPlayer inventoryPlayer, final ITerminalHost te, ContainerUltimateCraftingTerminal container) {
        super(inventoryPlayer, te, container, ExtendedCraftingGUIConstants.ULTIMATE_CRAFTING_TERMINAL);
        setGuiType(AE2ExtendedGUIs.ULTIMATE_CRAFTING_TERMINAL);
    }

    @Override
    protected String getBackground() {
        return "textures/gui/ultimate_extended_crafting_terminal.png";
    }
}

