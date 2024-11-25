package com.xc4de.ae2exttable.client.container;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.xc4de.ae2exttable.crafting.TableCraftResultTwo;
import com.xc4de.ae2exttable.crafting.TableCraftingTwo;
import com.xc4de.ae2exttable.crafting.TableResultHandlerTwo;
import com.xc4de.ae2exttable.tile.TileCustomExtendedTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCustomCrafting extends ContainerHelper {

    public InventoryCrafting matrix;
    public IInventory result;
    public TileCustomExtendedTable tile;

    int slotWidth = 18;
    int xOffsetFirstSlot = 14;
    int yOffsetFirstSlot = 27;

    public ContainerCustomCrafting(InventoryPlayer player, TileCustomExtendedTable tile) {
        this.tile = tile;
        this.matrix = new TableCraftingTwo(this, tile);
        this.result = new TableCraftResultTwo(tile);

        this.addSlotToContainer(new TableResultHandlerTwo(this.matrix, this.result, tile.getWorld(), 0, 136, 32));

        int wy, ex;
        for (wy = 0; wy < 2; wy++) {
            for (ex = 0; ex < 5; ex++) {
                this.addSlotToContainer(new Slot(this.matrix, ex + wy * 5, xOffsetFirstSlot + ex * slotWidth, yOffsetFirstSlot + wy * slotWidth));
            }
        }

        this.addPlayerInventorySlots(player);

    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        ItemStack result = TableRecipeManager.getInstance().findMatchingRecipe(this.matrix, this.tile.getWorld());
        this.result.setInventorySlotContents(0, result);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 10 && slotNumber < 46) {
                if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            this.onCraftMatrixChanged(this.matrix);
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUsableByPlayer(player);
    }
}
