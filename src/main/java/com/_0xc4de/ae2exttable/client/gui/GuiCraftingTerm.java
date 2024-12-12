package com._0xc4de.ae2exttable.client.gui;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketInventoryAction;
import appeng.helpers.InventoryAction;
import com.blakebr0.cucumber.util.Utils;
import com._0xc4de.ae2exttable.Tags;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiCraftingTerm extends GuiMEMonitorableTwo {

  private GuiImgButton clearBtn;
  private ContainerMEMonitorable container;
  private ITerminalHost te;

  public GuiCraftingTerm(
      InventoryPlayer inventoryPlayer,
      ITerminalHost te,
      ContainerMEMonitorable c,
      ExtendedCraftingGUIConstants guiConst) {
    super(inventoryPlayer, te, c, guiConst);
  }

  @Override
  public void initGui() {
    super.initGui();
    // guiLeft is the offset from the left side of the screen
    // guiTop is the offset from the top of the screen (often small)
    // ySize is how tall it is (like a yMAX) It can be simplified
    // This means the offset for the button is essentially an x,y pair
    this.buttonList.add(this.clearBtn = new GuiImgButton(this.guiLeft + this.getGuiConst().clearButtonOffset.x, this.guiTop + this.ySize - this.getGuiConst().clearButtonOffset.y, Settings.ACTIONS, ActionItems.STASH));
    this.clearBtn.setHalfSize(true);
  }

  @Override
  protected void actionPerformed(final GuiButton btn) {
    super.actionPerformed(btn);

    if (this.clearBtn == btn) {
      Slot s = null;
      final Container c = this.inventorySlots;
      for (final Object j : c.inventorySlots) {
        if (j instanceof SlotCraftingMatrix) {
          s = (Slot) j;
        }
      }

      if (s != null) {
        final PacketInventoryAction
            p = new PacketInventoryAction(InventoryAction.MOVE_REGION, s.slotNumber, 0);
        NetworkHandler.instance().sendToServer(p);
      }
    }
  }

  @Override
  public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
    super.drawFG(offsetX, offsetY, mouseX, mouseY);

    String displayName = Utils.localize(
        Tags.MODID + "." + this.getGuiType().toString().toLowerCase() + ".crafting");

    this.fontRenderer.drawString(displayName, 8, this.ySize - 96 + 1 - this.getReservedSpace(), 4210752);
  }

  // Also Guis might need custom offsets for upgrade slots
  public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
    if (PartGuiHandler.guiIsWirelessTerminal(this.getGuiType())) {
      this.bindTexture("guis/wirelessupgrades.png");
      // Maybe this should be a GUIConstant? I dont really care, it works
      int upgradeSlotOffset = 127 - 24;
      if (this.getGuiConst() == ExtendedCraftingGUIConstants.ULTIMATE_CRAFTING_TERMINAL) {
        upgradeSlotOffset = -34;
      }
      Gui.drawModalRectWithCustomSizedTexture(offsetX + 198, offsetY + upgradeSlotOffset, 0, 0, 32, 32, 32, 32);
    }
    super.drawBG(offsetX, offsetY, mouseX, mouseY);
  }

}
