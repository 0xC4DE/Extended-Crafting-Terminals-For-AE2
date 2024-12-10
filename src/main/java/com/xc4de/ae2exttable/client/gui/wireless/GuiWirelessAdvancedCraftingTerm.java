package com.xc4de.ae2exttable.client.gui.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com.xc4de.ae2exttable.client.container.wireless.ContainerAdvancedWirelessTerminal;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiWirelessAdvancedCraftingTerm extends GuiCraftingTerm {
  public GuiWirelessAdvancedCraftingTerm(
      InventoryPlayer inventoryPlayer,
      WirelessTerminalGuiObject te,
      ContainerAdvancedWirelessTerminal c) {
    super(inventoryPlayer, te, c,
        ExtendedCraftingGUIConstants.ADVANCED_CRAFTING_TERMINAL); // Shares same GUI const
    setGuiType(AE2ExtendedGUIs.WIRELESS_ADVANCED_CRAFTING_TERMINAL);
  }

  @Override
  protected String getBackground() {
    return "textures/gui/advanced_extended_crafting_terminal.png";
  }
}
