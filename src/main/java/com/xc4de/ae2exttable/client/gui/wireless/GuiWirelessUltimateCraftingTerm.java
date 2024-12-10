package com.xc4de.ae2exttable.client.gui.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com.xc4de.ae2exttable.client.container.wireless.ContainerUltimateWirelessTerminal;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiWirelessUltimateCraftingTerm extends GuiCraftingTerm {
  public GuiWirelessUltimateCraftingTerm(
      InventoryPlayer inventoryPlayer,
      WirelessTerminalGuiObject te,
      ContainerUltimateWirelessTerminal c) {
    super(inventoryPlayer, te, c,
        ExtendedCraftingGUIConstants.ULTIMATE_CRAFTING_TERMINAL); // Shares same GUI const
    setGuiType(AE2ExtendedGUIs.WIRELESS_ULTIMATE_CRAFTING_TERMINAL);
  }

  @Override
  protected String getBackground() {
    return "textures/gui/ultimate_extended_crafting_terminal.png";
  }
}
