package com._0xc4de.ae2exttable.client.gui.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerBasicWirelessTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiWirelessBasicCraftingTerm extends GuiCraftingTerm {
  public GuiWirelessBasicCraftingTerm(
      InventoryPlayer inventoryPlayer,
      WirelessTerminalGuiObject te,
      ContainerBasicWirelessTerminal c) {
    super(inventoryPlayer, te, c,
        ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL); // Shares same GUI const
    setGuiType(AE2ExtendedGUIs.WIRELESS_BASIC_CRAFTING_TERMINAL);
  }

  @Override
  protected String getBackground() {
    return "textures/gui/basic_extended_crafting_terminal.png";
  }
}
