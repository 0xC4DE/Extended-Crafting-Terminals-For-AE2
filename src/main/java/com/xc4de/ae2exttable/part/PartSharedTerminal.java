package com.xc4de.ae2exttable.part;

import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import appeng.api.parts.IPartCollisionHelper;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.IConfigManager;
import appeng.me.GridAccessException;

public abstract class PartSharedTerminal extends PartBase implements ITerminalHost, IAEAppEngInventory, IViewCellStorage {

    //private IConfigManager cm = new ThEConfigManager();
    private final AppEngInternalInventory viewCell = new AppEngInternalInventory(this, 5);


    public PartSharedTerminal(Item item) {
        super(item);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.viewCell.readFromNBT(nbt, "viewCell");
        //this.getConfigManager().readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.viewCell.writeToNBT(nbt, "viewCell");
        //this.getConfigManager().writeToNBT(nbt);
    }

    @Override
    public <T extends IAEStack<T>> IMEMonitor<T> getInventory(IStorageChannel<T> channel) {
        try {
            IGrid grid = this.gridNode.getGrid();
            if (grid == null) {
                throw new GridAccessException();
            }
            IStorageGrid storageGrid = grid.getCache(IStorageGrid.class);
            return storageGrid.getInventory(channel);
        } catch (GridAccessException e) {
            // Ignored
        }
        return null;
    }

    @Override
    public double getIdlePowerUsage() {
        return 0.5d;
    }

    @Override
    public IConfigManager getConfigManager() {
        return null;
    }

}
