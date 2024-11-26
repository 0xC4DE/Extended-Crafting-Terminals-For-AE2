package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.implementations.ContainerMEMonitorable;
import com.xc4de.ae2exttable.client.gui.GuiMEMonitorableTwo;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.List;

public class ContainerMEMonitorableTwo extends ContainerMEMonitorable {

    public ContainerMEMonitorableTwo(final InventoryPlayer ip, final ITerminalHost monitorable, final boolean isWireless) {
        super(ip, monitorable, isWireless);
    }

    public void postUpdate(final List<IAEItemStack> list) {
        for (final IAEItemStack is : list) {
            this.items.add(is);
        }
        ((GuiMEMonitorableTwo) this.getGui()).postUpdate(list);
    }
}