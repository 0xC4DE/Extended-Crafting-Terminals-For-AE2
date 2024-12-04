package com.xc4de.ae2exttable.client.container;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.api.networking.security.IActionHost;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotCraftingTerm;
import appeng.container.slot.SlotRestrictedInput;
import appeng.core.AEConfig;
import appeng.core.localization.PlayerMessages;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.parts.automation.StackUpgradeInventory;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.Platform;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import baubles.api.BaublesApi;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public class ContainerMEPortableTerminalTwo extends ContainerMEMonitorableTwo {

    protected final WirelessTerminalGuiObject wirelessTerminalGUIObject;
    private final int slot;
    private double powerMultiplier = 0.5;
    private int ticks = 0;

    protected AppEngInternalInventory upgrades;
    protected SlotRestrictedInput magnetSlot;

    // Slots for each wireless terminal
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);
    private final SlotCraftingMatrix[] craftingSlots;
    private final SlotCraftingTerm outputSlot;

    private AppEngInternalInventory craftingGrid;
    private IRecipe currentRecipe;

    private static final int craftingSlotWidth = 18;

    public ContainerMEPortableTerminalTwo(final InventoryPlayer inventoryPlayer, final WirelessTerminalGuiObject monitorable, final int slotWidth, final int slotHeight, ExtendedCraftingGUIConstants guiConst) {
        super(inventoryPlayer, monitorable, true, slotWidth, slotHeight, guiConst);

        final IItemHandler crafting = this.craftingGrid;

        // View Cell stuff. This is the same as ContainerMEMonitorableTwo
        for(int i = 0; i < 5; i++) {
            // Hacky. Removes old ViewCell slots.
            this.inventorySlots.remove(this.inventorySlots.size() - 1);
        }
        for(int y = 0; y < 5; ++y) {
            this.cellView[y] = new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.VIEW_CELL, ((IViewCellStorage)monitorable).getViewCellStorage(), y, 206, y * 18 + 8, this.getInventoryPlayer());
            this.cellView[y].setAllowEdit(this.canAccessViewCells);
            super.addSlotToContainer(this.cellView[y]);
        }

        // Begin instantiation for specific-to-terminal variables
        this.craftingSlots = new SlotCraftingMatrix[slotWidth * slotHeight];
        int craftingTableXOffset = guiConst.craftingGridOffset.x;
        int craftingTableYOffset = guiConst.craftingGridOffset.y;
        for (int y = 0; y < this.slotHeight; y++) {
            for (int x = 0; x < this.slotWidth; x++) {
                this.addSlotToContainer(this.craftingSlots[x + y * this.slotHeight] = new SlotCraftingMatrix(this, crafting, x + y * this.slotHeight, craftingTableXOffset + x * craftingSlotWidth, craftingTableYOffset + y * craftingSlotWidth));
            }
        }
        int outputX = guiConst.outputSlotOffset.x;
        int outputY = guiConst.outputSlotOffset.y;
        this.addSlotToContainer(this.outputSlot = new SlotCraftingTerm(this.getPlayerInv().player, this.getActionSource(), this
                .getPowerSource(), monitorable, crafting, crafting, this.output, outputX, outputY, this));

        this.bindPlayerInventory(inventoryPlayer, this.guiConst.inventoryOffset.x, this.guiConst.inventoryOffset.y);

        this.onCraftMatrixChanged(new WrapperInvItemHandler(crafting));


        // The rest of the instantiation
        if (monitorable != null) {
            final int slotIndex = ((IInventorySlotAware) monitorable).getInventorySlot();
            if (!((IInventorySlotAware) monitorable).isBaubleSlot()) {
                this.lockPlayerInventorySlot(slotIndex);
            }
            this.slot = slotIndex;
        } else {
            this.slot = -1;
            this.lockPlayerInventorySlot(inventoryPlayer.currentItem);
        }

            this.bindPlayerInventory(inventoryPlayer, 0, 0);

            this.wirelessTerminalGUIObject = monitorable;
            this.upgrades = new StackUpgradeInventory(wirelessTerminalGUIObject.getItemStack(), this, 2);
            this.loadFromNBT();
            this.setupUpgrades();
    }

    @Override
    public void detectAndSendChanges() {
        if (Platform.isServer()) {

            final ItemStack currentItem;
            if (wirelessTerminalGUIObject.isBaubleSlot()) {
                currentItem = BaublesApi.getBaublesHandler(this.getPlayerInv().player).getStackInSlot(this.slot);
            } else {
                currentItem = this.slot < 0 ? this.getPlayerInv().getCurrentItem() : this.getPlayerInv().getStackInSlot(this.slot);
            }

            if (currentItem.isEmpty()) {
                this.setValidContainer(false);
            } else if (!this.wirelessTerminalGUIObject.getItemStack().isEmpty() && currentItem != this.wirelessTerminalGUIObject.getItemStack()) {
                if (ItemStack.areItemsEqual(this.wirelessTerminalGUIObject.getItemStack(), currentItem)) {
                    if (wirelessTerminalGUIObject.isBaubleSlot()) {
                        BaublesApi.getBaublesHandler(this.getPlayerInv().player).setStackInSlot(this.slot, this.wirelessTerminalGUIObject.getItemStack());
                    } else {
                        this.getPlayerInv().setInventorySlotContents(this.slot, this.wirelessTerminalGUIObject.getItemStack());
                    }
                } else {
                    this.setValidContainer(false);
                }
            }

            // drain 1 ae t
            this.ticks++;
            if (this.ticks > 10) {
                double ext = this.wirelessTerminalGUIObject.extractAEPower(this.getPowerMultiplier() * this.ticks, Actionable.MODULATE, PowerMultiplier.CONFIG);
                if (ext < this.getPowerMultiplier() * this.ticks) {
                    if (Platform.isServer() && this.isValidContainer()) {
                        this.getPlayerInv().player.sendMessage(PlayerMessages.DeviceNotPowered.get());
                    }

                    this.setValidContainer(false);
                }
                this.ticks = 0;
            }

            if (!this.wirelessTerminalGUIObject.rangeCheck()) {
                if (Platform.isServer() && this.isValidContainer()) {
                    this.getPlayerInv().player.sendMessage(PlayerMessages.OutOfRange.get());
                }

                this.setValidContainer(false);
            } else {
                this.setPowerMultiplier(AEConfig.instance().wireless_getDrainRate(this.wirelessTerminalGUIObject.getRange()));
            }

            super.detectAndSendChanges();
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
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

    @Override
    protected IActionHost getActionHost() {
        return this.wirelessTerminalGUIObject;
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
        if (wirelessTerminalGUIObject != null) {
            for (int upgradeSlot = 0; upgradeSlot < availableUpgrades(); upgradeSlot++) {
                this.magnetSlot = new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.UPGRADES, upgrades, upgradeSlot, 206, 135 + upgradeSlot * 18, this.getInventoryPlayer());
                this.magnetSlot.setNotDraggable();
                this.addSlotToContainer(magnetSlot);
            }
        }
    }

    @Override
    public int getInventorySlot() {
        return wirelessTerminalGUIObject.getInventorySlot();
    }

    @Override
    public boolean isBaubleSlot() {
        return wirelessTerminalGUIObject.isBaubleSlot();
    }

    // Methods that have been moved up from lower-level containers for better organization
    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        // TODO: Make this the same as ContainerMEMonitorableTwo
    }

    @Override
    public void saveChanges() {
        if (Platform.isServer()) {
            NBTTagCompound tag = new NBTTagCompound();
            this.craftingGrid.writeToNBT(tag, "craftingGrid");
            this.upgrades.writeToNBT(tag, "upgrades");
            this.wirelessTerminalGUIObject.saveChanges(tag);
        }
    }

    @Override
    protected void loadFromNBT() {
        this.craftingGrid = new AppEngInternalInventory(this, 9);
        NBTTagCompound data = wirelessTerminalGUIObject.getItemStack().getTagCompound();
        if (data != null) {
            this.craftingGrid.readFromNBT(data, "craftingGrid");
        }
    }

    @Override
    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack) {
        if (inv == craftingGrid) {
            saveChanges();
        }
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("player")) {
            return new PlayerInvWrapper(this.getInventoryPlayer());
        } else if (name.equals("crafting")) {
            return this.craftingGrid;
        }
        return null;
    }

    @Override
    public boolean useRealItems() {
        return true;
    }
}
