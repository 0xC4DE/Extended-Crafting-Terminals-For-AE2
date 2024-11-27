package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerEliteCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerEliteCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 7, 7, ExtendedCraftingGUIConstants.ELITE_CRAFTING_TERMINAL);
    }
}