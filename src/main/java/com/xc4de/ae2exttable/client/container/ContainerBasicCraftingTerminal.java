package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;

import appeng.container.ContainerOpenContext;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerBasicCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 3, 3, ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
    }
}