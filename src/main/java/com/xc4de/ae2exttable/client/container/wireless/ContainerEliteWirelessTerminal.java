package com.xc4de.ae2exttable.client.container.wireless;

import com.xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerEliteWirelessTerminal extends
    ContainerSharedWirelessTerminals {

  public ContainerEliteWirelessTerminal(InventoryPlayer inventoryPlayer,
                                        WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, gui, 3, 3,
        ExtendedCraftingGUIConstants.ELITE_CRAFTING_TERMINAL);
  }

}
