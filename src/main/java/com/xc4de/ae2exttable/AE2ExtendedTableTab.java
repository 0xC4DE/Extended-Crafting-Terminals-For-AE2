package com.xc4de.ae2exttable;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AE2ExtendedTableTab extends CreativeTabs {
    public AE2ExtendedTableTab() {
        super(Tags.MODID);
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(Blocks.DIRT);
    }
}
