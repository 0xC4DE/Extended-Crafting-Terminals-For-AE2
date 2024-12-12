package com._0xc4de.ae2exttable.client.container.wireless;

import com._0xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerAdvancedWirelessTerminal extends
    ContainerSharedWirelessTerminals {

  public ContainerAdvancedWirelessTerminal(InventoryPlayer inventoryPlayer,
                                           WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, gui, 5, 5,
        ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL);
  }

}
