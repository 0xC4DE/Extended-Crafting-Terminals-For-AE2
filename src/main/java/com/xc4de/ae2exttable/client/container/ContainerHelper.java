package com.xc4de.ae2exttable.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerHelper extends Container {

    int playerSlotWidth = 18; // always 18 probably
    int xOffsetPlayerInventory = 8;
    int yOffsetPlayerInventory = 88;
    int xOffsetPlayerHotbar = 8;
    int yOffsetPlayerHotbar = 146; // Doesn't change, solid line

    public void addPlayerInventorySlots(InventoryPlayer player) {
        int wy, ex;
        // Player Inventory
        for (wy = 0; wy < 3; wy++) {
            for (ex = 0; ex < 9; ex++) {
                // index is x * y * 9 because of how the inventory is indexed, it is +9 to skip hotbar
                this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, xOffsetPlayerInventory + ex * playerSlotWidth, yOffsetPlayerInventory + wy * playerSlotWidth));
            }
        }

        // Player Hotbar
        for (ex = 0; ex < 9; ex++) {
            this.addSlotToContainer(new Slot(player, ex, xOffsetPlayerHotbar + ex * playerSlotWidth, yOffsetPlayerHotbar));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}
