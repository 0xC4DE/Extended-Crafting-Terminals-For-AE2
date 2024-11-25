package com.xc4de.ae2exttable.crafting;

import com.xc4de.ae2exttable.interfaces.IAE2ExtendedTable;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class TableCraftResultTwo extends InventoryCraftResult {
    private IAE2ExtendedTable tile;

    public TableCraftResultTwo(IAE2ExtendedTable tile) {
        this.tile = tile;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        this.tile.setResult(stack);
    }

    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.tile.getResult() : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int slot, int decrement) {
        ItemStack stack = this.tile.getResult();
        if (!stack.isEmpty()) {
            this.tile.setResult(ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
