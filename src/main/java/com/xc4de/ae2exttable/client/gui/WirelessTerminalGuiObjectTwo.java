package com.xc4de.ae2exttable.client.gui;

import appeng.api.features.IWirelessTermHandler;
import appeng.api.storage.IStorageMonitorable;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import com.xc4de.ae2exttable.interfaces.IWirelessTermGui;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.part.ExtInternalInventory;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;


/**
 * This somewhat shadows PartSharedCraftingTerminal in a way
 */
public class WirelessTerminalGuiObjectTwo extends WirelessTerminalGuiObject {

    public AE2ExtendedGUIs guiType;
    protected ExtInternalInventory craftingGrid;
    protected ItemStack effectiveItem;

    public WirelessTerminalGuiObjectTwo(IWirelessTermHandler wh, ItemStack is, EntityPlayer ep, World w, int x, int y, int z) {
        super(wh, is, ep, w, x, y, z);
        this.effectiveItem = is;
        AE2ExtendedGUIs gui = ItemRegistry.guiByItem(is.getItem());
        if (gui != null) {
            this.guiType = gui;
        } else {
            this.guiType = AE2ExtendedGUIs.WIRELESS_BASIC_TERMINAL;
        }
        this.craftingGrid = new ExtInternalInventory("crafting", guiType.getGridSize(), 64);
        this.loadFromNBT();
    }

    /**
     * Gets the contents of the inventory stored on the terminal object
     */
    public void loadFromNBT() {
        // Loads ViewCells and upgrades
        super.loadFromNBT();
        if (this.effectiveItem != null) {
            NBTTagCompound tag = this.effectiveItem.getTagCompound();
            if (tag != null) {
                this.craftingGrid.deserializeNBT(tag.getTagList("crafting", 10));
            }
        }
    }

    /**
     * Saves the contents of the inventory after saving view cells and others, I hope
     */
    @Override
    public void saveChanges() {
        super.saveChanges();
        if (this.effectiveItem != null) {
            NBTTagCompound tag = this.effectiveItem.getTagCompound();
            if (tag != null) {
                tag.setTag("crafting", this.craftingGrid.serializeNBT());
            }
        }
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("crafting")) {
            return new InvWrapper(this.craftingGrid);
        }
        return super.getInventoryByName(name);
    }

    @Override
    public void onChangeInventory(IItemHandler inv, int slot, InvOperation mc, ItemStack removedStack, ItemStack newStack) {
        this.saveChanges();
    }
}
