package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAdvancedCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerAdvancedCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 5, 5, ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL);
    }
}