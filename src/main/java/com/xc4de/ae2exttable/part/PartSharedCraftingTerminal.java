package com.xc4de.ae2exttable.part;

import appeng.helpers.Reflected;
import appeng.parts.reporting.AbstractPartTerminal;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

public abstract class PartSharedCraftingTerminal extends AbstractPartTerminal {

    public AE2ExtendedGUIs guiType;
    protected ExtInternalInventory craftingGrid;

    @Reflected
    public PartSharedCraftingTerminal(final ItemStack is) {
        super(is);
        craftingGrid = new ExtInternalInventory("crafting", 3 * 3, 64);
    }

    @Reflected
    public PartSharedCraftingTerminal(final ItemStack is, final int gridSize) {
        super(is);
        this.craftingGrid = new ExtInternalInventory("crafting", gridSize, 64);
    }

    @Override
    public void getDrops(final List<ItemStack> drops, final boolean wrenched) {
        super.getDrops(drops, wrenched);

        for (final ItemStack is : this.craftingGrid) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
        //drops.add(this.getItemStack());
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("crafting")) {
            this.craftingGrid.deserializeNBT(tag.getTagList("crafting", 10));
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setTag("crafting", this.craftingGrid.serializeNBT());
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("crafting")) {
            return new InvWrapper(this.craftingGrid);
        }
        return super.getInventoryByName(name);
    }

    @Override
    public abstract boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos);

    // Overwrite me!
    public abstract AE2ExtendedGUIs getGuiType();

}
