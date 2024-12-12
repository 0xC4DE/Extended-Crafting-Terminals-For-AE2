package com._0xc4de.ae2exttable.client.container.wireless;

import com._0xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerUltimateWirelessTerminal extends
    ContainerSharedWirelessTerminals {

  public ContainerUltimateWirelessTerminal(InventoryPlayer inventoryPlayer,
                                           WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, gui, 9, 9,
        ExtendedCraftingGUIConstants.ULTIMATE_CRAFTING_TERMINAL);
  }

}
