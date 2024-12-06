package com.xc4de.ae2exttable.client.container.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com.xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBasicWirelessTerminal extends
    ContainerMEMonitorableTwo {

  private final WirelessTerminalGuiObject wirelessTerminalGUIObject;

  public ContainerBasicWirelessTerminal(InventoryPlayer inventoryPlayer, WirelessTerminalGuiObject gui) {
    super(inventoryPlayer, gui, true, 3, 3, ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
    wirelessTerminalGUIObject = gui;
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
