package com._0xc4de.ae2exttable.client.container.terminals;

import appeng.api.storage.ITerminalHost;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAdvancedCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerAdvancedCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, 5, 5, ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL);
    }

    @Override
    protected void loadFromNBT() {

    }
}