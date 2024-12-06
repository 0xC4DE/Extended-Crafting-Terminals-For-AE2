/**
 * Original file from AE2-UEL, modified to work with Extended Crafting.
 * Modified by: 0XC4DE
 */
package com.xc4de.ae2exttable.client.container;

import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.WirelessTerminalGuiObject;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.part.ExtInternalInventory;
import net.minecraft.init.Blocks;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.GuiMEMonitorableTwo;
import com.xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.List;

public abstract class ContainerMEMonitorableTwo extends ContainerMEMonitorable implements IAEAppEngInventory, IContainerCraftingPacket {

    private IRecipe currentRecipe;
    protected final int slotWidth;
    protected final int slotHeight;
    private final SlotCraftingTerm outputSlot;
    final ExtendedCraftingGUIConstants guiConst;
    private PartSharedCraftingTerminal ct;
    private WirelessTerminalGuiObject wt;

    protected final SlotCraftingMatrix[] craftingSlots;
    private final AppEngInternalInventory output;

    private static final int craftingSlotWidth = 18;

    public ContainerMEMonitorableTwo(final InventoryPlayer ip, final ITerminalHost monitorable, final boolean isWireless,
                                     final int slotWidth, final int slotHeight, ExtendedCraftingGUIConstants guiConst) {
        super(ip, monitorable, isWireless);

        // This check is here to debug easier
        for (int i = 0; i < 5; i++) {
            // Hacky. Removes old ViewCell slots.
            this.inventorySlots.remove(this.inventorySlots.size() - 1);
        }
        for(int y = 0; y < 5; ++y) {
            this.cellView[y] = new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.VIEW_CELL, ((IViewCellStorage)monitorable).getViewCellStorage(), y, 206, y * 18 + 8, this.getInventoryPlayer());
            this.cellView[y].setAllowEdit(this.canAccessViewCells);
            super.addSlotToContainer(this.cellView[y]);
        }

        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.output =  new AppEngInternalInventory(this, 1);
        this.craftingSlots = new SlotCraftingMatrix[this.slotWidth * this.slotHeight];
        final IItemHandler crafting;
        if (monitorable instanceof PartSharedCraftingTerminal) {
            this.ct = (PartSharedCraftingTerminal) monitorable;
            crafting = this.ct.getInventoryByName("crafting");
        } else {
            this.wt = (WirelessTerminalGuiObject) monitorable;
            craftingGrid = new ExtInternalInventory("crafting", 3 * 3, 64);
            crafting = this.wt.getInventoryByName("crafting");
        }
        this.guiConst = guiConst;

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

        // This is specifically for the slots, not the gui portion
        // Player Inventory for Tables, offsetX is the distance from the left edge of the GUI to the left edge of the player inventory
        // where the players inventory left edge is the first "inner" pixel of the leftmost slot minus 9 pixels (because why not)
        // some of my GUIs shift this. so. you know. it's a thing.
        this.bindPlayerInventory(ip, this.guiConst.inventoryOffset.x, this.guiConst.inventoryOffset.y);

        this.onCraftMatrixChanged(new WrapperInvItemHandler(crafting));
    }

    public void onCraftMatrixChanged(IInventory inventory) {
        final ContainerNull cn = new ContainerNull();
        final InventoryCrafting ic = new InventoryCrafting(cn, this.slotWidth, this.slotHeight);
        for (int x = 0; x < this.slotWidth*this.slotHeight; x++) {
            AE2ExtendedCraftingTable.LOGGER.error("RealSize: " + this.craftingSlots.length);
            AE2ExtendedCraftingTable.LOGGER.error("Slot: " + x + " Stack: " + this.craftingSlots[x].getStack());
            ic.setInventorySlotContents(x, this.craftingSlots[x].getStack());
        }
        ItemStack result = TableRecipeManager.getInstance().findMatchingRecipe(ic, this.getInventoryPlayer().player.world);
        if (result == null) {
            this.outputSlot.putStack(new ItemStack(Blocks.AIR));
        } else {
            this.outputSlot.putStack(result);
        }
    }

    public void postUpdate(final List<IAEItemStack> list) {
        for (final IAEItemStack is : list) {
            this.items.add(is);
        }
        ((GuiMEMonitorableTwo) this.getGui()).postUpdate(list);
    }

    public abstract int availableUpgrades();

    public abstract void setupUpgrades();

    public void saveChanges() {

    }

    protected abstract void loadFromNBT();

    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack) {

    }

    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("player")) {
            return new PlayerInvWrapper(this.getInventoryPlayer());
        }
        return this.ct.getInventoryByName(name);
    }

    public boolean useRealItems() {
        return true;
    }

    public IRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public abstract int getInventorySlot();

    public abstract boolean isBaubleSlot();
}