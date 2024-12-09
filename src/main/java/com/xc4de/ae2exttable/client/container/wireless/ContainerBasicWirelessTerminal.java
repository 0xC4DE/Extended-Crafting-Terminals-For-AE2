package com.xc4de.ae2exttable.client.container.wireless;

import com.xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicWirelessTerminal extends
    ContainerSharedWirelessTerminals {

  public ContainerBasicWirelessTerminal(InventoryPlayer inventoryPlayer,
                                        WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, gui, 3, 3,
        ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
  }

}