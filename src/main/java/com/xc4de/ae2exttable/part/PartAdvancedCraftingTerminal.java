package com.xc4de.ae2exttable.part;

import appeng.helpers.Reflected;
import appeng.util.Platform;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class PartAdvancedCraftingTerminal extends PartSharedCraftingTerminal {

    @Reflected
    public PartAdvancedCraftingTerminal(final ItemStack is) {
        super(is, AE2ExtendedGUIs.ADVANCED_CRAFTING_TERMINAL.getGridSize());
    }

    public boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos) {
        if (Platform.isServer()) {
            PartGuiHandler.openGUI(AE2ExtendedGUIs.ADVANCED_CRAFTING_TERMINAL, player, this.getHost().getTile().getPos(), this.getSide());
        }

        return true;
    }
}
