package com._0xc4de.ae2exttable.client;

import appeng.api.util.AEColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

// this is useless kinda because it screws with item colors, but I'm going to keep it around just in case
// It doesn't do anything, lol.
@SideOnly(Side.CLIENT)
public class ItemColors {
    public static void registerItemColors() {
        TerminalItemColor terminalItemColor = new TerminalItemColor();
        //Minecraft.getMinecraft().getItemColors().registerItemColorHandler(terminalItemColor, ItemRegistry.BASIC_TERMINAL);
    }

    @SideOnly(Side.CLIENT)
    public static class TerminalItemColor implements IItemColor {
        @Override
        public int colorMultiplier(ItemStack item, int tintIndex) {
            return AEColor.TRANSPARENT.getVariantByTintIndex(tintIndex);
        }
    }
}
