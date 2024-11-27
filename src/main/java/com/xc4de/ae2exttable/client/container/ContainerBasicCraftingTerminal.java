package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerBasicCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 3, 3);
    }
}