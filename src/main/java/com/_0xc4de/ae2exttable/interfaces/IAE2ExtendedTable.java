package com._0xc4de.ae2exttable.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IAE2ExtendedTable {
    NonNullList<ItemStack> getMatrix();
    ItemStack getResult();
    void setResult(ItemStack stack);
    void setInventorySlotContents(int slot, ItemStack stack);
    int getWidth();
    int getHeight();
}
