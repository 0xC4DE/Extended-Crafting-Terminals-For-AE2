package com._0xc4de.ae2exttable.client.container.wireless;

import com._0xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerEliteWirelessTerminal extends
    ContainerSharedWirelessTerminals {

  public ContainerEliteWirelessTerminal(InventoryPlayer inventoryPlayer,
                                        WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, gui, 7, 7,
        ExtendedCraftingGUIConstants.ELITE_CRAFTING_TERMINAL);
  }

}
