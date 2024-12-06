package com.xc4de.ae2exttable.client.container.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com.xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicWirelessTerminal extends
    ContainerMEMonitorableTwo {

  private final WirelessTerminalGuiObject wirelessTerminalGUIObject;

  public ContainerBasicWirelessTerminal(InventoryPlayer inventoryPlayer, WirelessTerminalGuiObjectTwo gui) {
    super(inventoryPlayer, gui, true, 3, 3, ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
    wirelessTerminalGUIObject = gui;
  }

  @Override
  protected void loadFromNBT() {}

  @Override
  public int getInventorySlot() {
    return 0;
  }

  @Override
  public boolean isBaubleSlot() {
    return false;
  }
}
