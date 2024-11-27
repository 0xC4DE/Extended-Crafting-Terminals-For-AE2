package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerUltimateCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerUltimateCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 9, 9, ExtendedCraftingGUIConstants.ULTIMATE_CRAFTING_TERMINAL);
    }
}