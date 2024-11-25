package com.xc4de.ae2exttable.client.gui;

import com.xc4de.ae2exttable.client.container.ContainerCustomCrafting;
import com.xc4de.ae2exttable.tile.TileCustomExtendedTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class GuiHandler implements IGuiHandler {
    public static final int CUSTOM_EXTENDED_TABLE = 1;

    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == CUSTOM_EXTENDED_TABLE) {
            return new ContainerCustomCrafting(player.inventory, (TileCustomExtendedTable) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == CUSTOM_EXTENDED_TABLE) {
            return new GuiCustomCrafting(new ContainerCustomCrafting(player.inventory, (TileCustomExtendedTable) world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }
}
