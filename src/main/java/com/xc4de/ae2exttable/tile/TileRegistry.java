package com.xc4de.ae2exttable.tile;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileRegistry {
    public static void init() {
        GameRegistry.registerTileEntity(TileCustomExtendedTable.class, new ResourceLocation("ae2exttable", "custom_extended_table"));
    }
}
