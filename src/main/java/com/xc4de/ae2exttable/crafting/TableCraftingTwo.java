package com.xc4de.ae2exttable.crafting;

import com.blakebr0.extendedcrafting.lib.IExtendedTable;
import com.xc4de.ae2exttable.interfaces.IAE2ExtendedTable;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TableCraftingTwo extends InventoryCrafting implements ISidedInventory {
    public IAE2ExtendedTable tile;
    public IInventory handler;
    public Container container;
    public int lineSize;

    public TableCraftingTwo(Container container, IAE2ExtendedTable tile) {
        super(container, tile.getWidth(), tile.getHeight());
        this.tile = tile;
        this.handler = (IInventory)tile;
        this.container = container;
        this.lineSize = tile.getWidth();
    }

    public ItemStack getStackInSlot(int slot) {
        return slot >= this.getSizeInventory() ? ItemStack.EMPTY : this.handler.getStackInSlot(slot);
    }

    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < this.lineSize) {
            int x = row + column * this.lineSize;
            return this.getStackInSlot(x);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = this.handler.getStackInSlot(index);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= count) {
                ItemStack itemstack = stack.copy();
                this.handler.setInventorySlotContents(index, ItemStack.EMPTY);
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                ItemStack itemstack = stack.splitStack(count);
                if (stack.getCount() == 0) {
                    this.handler.setInventorySlotContents(index, ItemStack.EMPTY);
                }

                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.handler.setInventorySlotContents(slot, stack);
        this.container.onCraftMatrixChanged(this);
    }

    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }
}
