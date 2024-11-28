package com.xc4de.ae2exttable.part;

import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.helpers.Reflected;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractPartTerminal;
import appeng.util.Platform;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.List;

public abstract class PartSharedCraftingTerminal extends AbstractPartTerminal {

    @PartModels
    protected static final ResourceLocation MODEL_BASE = new ResourceLocation("appliedenergistics2", "part/display_base");
    @PartModels
    protected static final ResourceLocation MODEL_STATUS_OFF = new ResourceLocation("appliedenergistics2", "part/display_status_off");
    @PartModels
    protected static final ResourceLocation MODEL_STATUS_ON = new ResourceLocation("appliedenergistics2", "part/display_status_on");
    @PartModels
    protected static final ResourceLocation MODEL_STATUS_HAS_CHANNEL = new ResourceLocation("appliedenergistics2", "part/display_status_has_channel");
    @PartModels
    public static final ResourceLocation MODEL_OFF = new ResourceLocation(AppEng.MOD_ID, "part/crafting_terminal_off");
    @PartModels
    public static final ResourceLocation MODEL_ON = new ResourceLocation(AppEng.MOD_ID, "part/crafting_terminal_on");

    public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
    public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
    public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

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
        drops.add(this.getItemStack());
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
        AE2ExtendedCraftingTable.LOGGER.error("Writing NBT: " + tag);
        tag.setTag("crafting", this.craftingGrid.serializeNBT());
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("crafting")) {
            AE2ExtendedCraftingTable.LOGGER.error("CRAFTING GRID: " + new InvWrapper(this.craftingGrid).getSlots());
            return new InvWrapper(this.craftingGrid);
        }
        return super.getInventoryByName(name);
    }

    @Override
    public IPartModel getStaticModels() {
        if (this.isPowered() && this.isActive()) {
           return MODELS_HAS_CHANNEL;
        } else if (this.isPowered()) {
            return MODELS_ON;
        }
        return MODELS_OFF;
    }

    @Override
    public abstract boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos);
}
