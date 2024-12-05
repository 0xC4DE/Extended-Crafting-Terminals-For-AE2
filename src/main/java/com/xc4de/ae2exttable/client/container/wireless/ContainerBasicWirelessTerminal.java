package com.xc4de.ae2exttable.client.container.wireless;

import appeng.api.storage.ITerminalHost;
import appeng.container.implementations.ContainerMEMonitorable;
import com.xc4de.ae2exttable.client.gui.terminals.GuiCraftingTerm;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicWirelessTerminal extends GuiCraftingTerm {

  public ContainerBasicWirelessTerminal(InventoryPlayer inventoryPlayer,
                                        ITerminalHost te,
                                        ContainerMEMonitorable c,
                                        ExtendedCraftingGUIConstants guiConst) {
    super(inventoryPlayer, te, c, guiConst);
  }
}
