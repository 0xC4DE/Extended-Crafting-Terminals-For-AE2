package com._0xc4de.ae2exttable.client.gui.wireless;

import appeng.helpers.WirelessTerminalGuiObject;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerEliteWirelessTerminal;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiCraftingTerm;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiWirelessEliteCraftingTerm extends GuiCraftingTerm {
  public GuiWirelessEliteCraftingTerm(
      InventoryPlayer inventoryPlayer,
      WirelessTerminalGuiObject te,
      ContainerEliteWirelessTerminal c) {
    super(inventoryPlayer, te, c,
        ExtendedCraftingGUIConstants.ELITE_CRAFTING_TERMINAL); // Shares same GUI const
    setGuiType(AE2ExtendedGUIs.WIRELESS_ELITE_CRAFTING_TERMINAL);
  }

  @Override
  protected String getBackground() {
    return "textures/gui/elite_extended_crafting_terminal.png";
  }
}
