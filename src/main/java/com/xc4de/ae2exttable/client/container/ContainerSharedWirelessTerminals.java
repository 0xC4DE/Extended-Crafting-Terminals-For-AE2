package com.xc4de.ae2exttable.client.container;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.IUpgradeableCellContainer;
import appeng.api.storage.ITerminalHost;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.container.slot.SlotRestrictedInput;
import appeng.core.AEConfig;
import appeng.core.localization.PlayerMessages;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.parts.automation.StackUpgradeInventory;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.Platform;
import appeng.util.inv.IAEAppEngInventory;
import baubles.api.BaublesApi;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;


/**
 * Essentially a clone of ContainerWirelessCraftingTerminal, tweaks are made to support what I need
 * But other than that it's a direct rip because I have it setup to use MEMonitorableTwo
 */
public class ContainerSharedWirelessTerminals extends ContainerMEMonitorableTwo
    implements
    IUpgradeableCellContainer, IAEAppEngInventory,
    IInventorySlotAware {

  private final int slot;
  private double powerMultiplier = 0.5;
  private int ticks = 0;

  //protected AppEngInternalInventory upgrades;
  protected SlotRestrictedInput magnetSlot;

  public ContainerSharedWirelessTerminals(
      InventoryPlayer ip,
      ITerminalHost monitorable,
      WirelessTerminalGuiObjectTwo guiItemObject,
      int slotWidth, int slotHeight,
      ExtendedCraftingGUIConstants guiConst) {

    super(ip, monitorable, guiItemObject, slotWidth, slotHeight, guiConst);
    this.wt = guiItemObject;

    if (guiItemObject != null) {
      final int slotIndex =
          ((IInventorySlotAware) guiItemObject).getInventorySlot();
      if (!((IInventorySlotAware) guiItemObject).isBaubleSlot()) {
        this.lockPlayerInventorySlot(slotIndex);
      }
      this.slot = slotIndex;
    } else {
      this.slot = -1;
      this.lockPlayerInventorySlot(ip.currentItem);
    }

    this.loadFromNBT();
    this.setupUpgrades();
  }

  private double getPowerMultiplier() {
    return this.powerMultiplier;
  }

  void setPowerMultiplier(final double powerMultiplier) {
    this.powerMultiplier = powerMultiplier;
  }

  @Override
  public int availableUpgrades() {
    return 1;
  }

  @Override
  public void setupUpgrades() {
    if (this.wt != null) {
      // One upgrade slot
      for (int upgradeSlot = 0; upgradeSlot < availableUpgrades();
           upgradeSlot++) {
        this.magnetSlot = new SlotRestrictedInput(
            SlotRestrictedInput.PlacableItemType.UPGRADES, upgrades,
            upgradeSlot, 206, 135 + upgradeSlot * 18,
            this.getInventoryPlayer());
        this.magnetSlot.setNotDraggable();
        this.addSlotToContainer(magnetSlot);
      }
    }
  }

  @Override
  public int getInventorySlot() {
    return this.wt.getInventorySlot();
  }

  public boolean isBaubleSlot() {
    return this.wt.isBaubleSlot();
  }

  @Override
  protected void loadFromNBT() {
    NBTTagCompound data =
        wt.getItemStack().getTagCompound();
    if (data != null) {
      upgrades.readFromNBT(
          wt.getItemStack().getTagCompound()
              .getCompoundTag("upgrades"));
    }
  }

  @Override
  public void saveChanges() {
    if (Platform.isServer()) {
      NBTTagCompound tag = new NBTTagCompound();
      // For one reason or another upgrades is not guaranteed to exist at this point.
      this.upgrades.writeToNBT(tag, "upgrades");

      this.wt.saveChanges(tag);
    }
  }

  @Override
  public void detectAndSendChanges() {
    if (Platform.isServer()) {

      final ItemStack currentItem;
      if (wt.isBaubleSlot()) {
        currentItem = BaublesApi.getBaublesHandler(this.getPlayerInv().player)
            .getStackInSlot(this.slot);
      } else {
        currentItem = this.slot < 0 ? this.getPlayerInv().getCurrentItem() :
            this.getPlayerInv().getStackInSlot(this.slot);
      }

      if (currentItem.isEmpty()) {
        this.setValidContainer(false);
      } else if (!this.wt.getItemStack().isEmpty() &&
          currentItem != this.wt.getItemStack()) {
        if (ItemStack.areItemsEqual(
            this.wt.getItemStack(), currentItem)) {
          if (this.wt.isBaubleSlot()) {
            BaublesApi.getBaublesHandler(this.getPlayerInv().player)
                .setStackInSlot(this.slot,
                    this.wt.getItemStack());
          } else {
            this.getPlayerInv().setInventorySlotContents(this.slot,
                this.wt.getItemStack());
          }
        } else {
          this.setValidContainer(false);
        }
      }

      // drain 1 ae t
      this.ticks++;
      if (this.ticks > 10) {
        double ext = this.wt.extractAEPower(
            this.getPowerMultiplier() * this.ticks, Actionable.MODULATE,
            PowerMultiplier.CONFIG);
        if (ext < this.getPowerMultiplier() * this.ticks) {
          if (Platform.isServer() && this.isValidContainer()) {
            this.getPlayerInv().player.sendMessage(
                PlayerMessages.DeviceNotPowered.get());
          }

          this.setValidContainer(false);
        }
        this.ticks = 0;
      }

      if (!this.wt.rangeCheck()) {
        if (Platform.isServer() && this.isValidContainer()) {
          this.getPlayerInv().player.sendMessage(
              PlayerMessages.OutOfRange.get());
        }

        this.setValidContainer(false);
      } else {
        this.setPowerMultiplier(AEConfig.instance()
            .wireless_getDrainRate(this.wt.getRange()));
      }

      super.detectAndSendChanges();
    }
  }

  @Override
  public @NotNull ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn,
                                      @NotNull EntityPlayer player) {
    if (slotId >= 0 && slotId < this.inventorySlots.size()) {
      if (clickTypeIn == ClickType.PICKUP && dragType == 1) {
        if (this.inventorySlots.get(slotId) == magnetSlot) {
          ItemStack itemStack = magnetSlot.getStack();
          if (!magnetSlot.getStack().isEmpty()) {
            NBTTagCompound tag = itemStack.getTagCompound();
            if (tag == null) {
              tag = new NBTTagCompound();
            }
            if (tag.hasKey("enabled")) {
              boolean e = tag.getBoolean("enabled");
              tag.setBoolean("enabled", !e);
            } else {
              tag.setBoolean("enabled", false);
            }
            magnetSlot.getStack().setTagCompound(tag);
            magnetSlot.onSlotChanged();
            return ItemStack.EMPTY;
          }
        }
      }
    }
    return super.slotClick(slotId, dragType, clickTypeIn, player);
  }
}
