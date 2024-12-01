package com.xc4de.ae2exttable;

import com.xc4de.ae2exttable.items.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AE2ExtendedTableTab extends CreativeTabs {
    public AE2ExtendedTableTab() {
        super(Tags.MODID);
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(ItemRegistry.BASIC_TERMINAL);
    }
}
