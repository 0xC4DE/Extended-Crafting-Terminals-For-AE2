package com.xc4de.ae2exttable.part;

import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.helpers.Reflected;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import net.minecraftforge.items.wrapper.InvWrapper;
import appeng.parts.reporting.AbstractPartTerminal;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.Platform;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.GuiHandler;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public class PartBasicCraftingTerminal extends PartSharedCraftingTerminal {

    private final ExtInternalInventory craftingGrid = new ExtInternalInventory("craftingGrid", 9, 64);

    @Reflected
    public PartBasicCraftingTerminal(final ItemStack is) {
        super(is);
    }

    public boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos) {
        if (Platform.isServer()) {
            PartGuiHandler.openGUI(AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL, player, this.getHost().getTile().getPos(), this.getSide());
        }

        return true;
    }
}
