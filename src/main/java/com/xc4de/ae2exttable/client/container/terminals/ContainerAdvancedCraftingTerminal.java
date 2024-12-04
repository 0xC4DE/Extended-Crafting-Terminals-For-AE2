package com.xc4de.ae2exttable.client.container.terminals;

import appeng.api.storage.ITerminalHost;
import com.xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAdvancedCraftingTerminal extends ContainerMEMonitorableTwo {
    public ContainerAdvancedCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false, 5, 5, ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL);
    }

    @Override
    public int availableUpgrades() {
        return 0;
    }

    @Override
    public void setupUpgrades() {

    }

    @Override
    protected void loadFromNBT() {

    }

    @Override
    public int getInventorySlot() {
        return 0;
    }

    @Override
    public boolean isBaubleSlot() {
        return false;
    }
}