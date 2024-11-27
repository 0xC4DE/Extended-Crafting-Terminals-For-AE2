package com.xc4de.ae2exttable.client.gui;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.storage.ITerminalHost;
import appeng.api.util.AEPartLocation;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.client.container.ContainerAdvancedCraftingTerminal;
import com.xc4de.ae2exttable.client.container.ContainerBasicCraftingTerminal;
import com.xc4de.ae2exttable.client.container.ContainerEliteCraftingTerminal;
import com.xc4de.ae2exttable.client.container.ContainerUltimateCraftingTerminal;
import com.xc4de.ae2exttable.client.gui.terminals.GuiAdvancedCraftingTerminal;
import com.xc4de.ae2exttable.client.gui.terminals.GuiBasicCraftingTerminal;
import com.xc4de.ae2exttable.client.gui.terminals.GuiEliteCraftingTerminal;
import com.xc4de.ae2exttable.client.gui.terminals.GuiUltimateCraftingTerminal;
import com.xc4de.ae2exttable.part.PartAdvancedCraftingTerminal;
import com.xc4de.ae2exttable.part.PartBasicCraftingTerminal;
import com.xc4de.ae2exttable.part.PartEliteCraftingTerminal;
import com.xc4de.ae2exttable.part.PartUltimateCraftingTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class PartGuiHandler implements IGuiHandler {
    public static void openGUI(AE2ExtendedGUIs gui, EntityPlayer player, BlockPos pos, AEPartLocation side) {
        if (gui == null) {
            throw new IllegalArgumentException("gui cannot be null");
        }
        else if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }

        if (pos != null) {
            player.openGui(AE2ExtendedCraftingTable.instance, PartGuiHandler.calculateOrdinal(gui, side), player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static int calculateOrdinal(AE2ExtendedGUIs gui, AEPartLocation side) {
        if (side == null) {
            return AEPartLocation.UP.ordinal();
        }
        return (gui.ordinal() << 4) | side.ordinal();
    }

    public static AE2ExtendedGUIs getGUIFromOrdinal(int ordinal) {
        return AE2ExtendedGUIs.values()[ordinal >> 4];
    }

    public static AEPartLocation getSideFromOrdinal(int ordinal) {
        return AEPartLocation.fromOrdinal(ordinal & 7);
    }

    public static IPart getPartFromWorld(World world, BlockPos pos, AEPartLocation side) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IPartHost) {
            return ((IPartHost) te).getPart(side);
        }
        return null;
    }


    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        AE2ExtendedGUIs guiID = PartGuiHandler.getGUIFromOrdinal(ID);
        AEPartLocation side = PartGuiHandler.getSideFromOrdinal(ID);
        IPart part = PartGuiHandler.getPartFromWorld(world, new BlockPos(x,y,z), side);
        switch(guiID) {
            case BASIC_CRAFTING_TERMINAL:
                return new ContainerBasicCraftingTerminal(player.inventory, (PartBasicCraftingTerminal) part);
            case ADVANCED_CRAFTING_TERMINAL:
                return new ContainerAdvancedCraftingTerminal(player.inventory, (PartAdvancedCraftingTerminal) part);
            case ELITE_CRAFTING_TERMINAL:
                return new ContainerEliteCraftingTerminal(player.inventory, (PartEliteCraftingTerminal) part);
            case ULTIMATE_CRAFTING_TERMINAL:
                return new ContainerUltimateCraftingTerminal(player.inventory, (PartUltimateCraftingTerminal) part);
            default:
                return null;
        }
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        AE2ExtendedGUIs guiID = PartGuiHandler.getGUIFromOrdinal(ID);
        AEPartLocation side = PartGuiHandler.getSideFromOrdinal(ID);
        IPart part = PartGuiHandler.getPartFromWorld(world, new BlockPos(x,y,z), side);

        switch(guiID) {
            case BASIC_CRAFTING_TERMINAL:
                return new GuiBasicCraftingTerminal(player.inventory, (ITerminalHost) part, new ContainerBasicCraftingTerminal(player.inventory, (PartBasicCraftingTerminal) part));
            case ADVANCED_CRAFTING_TERMINAL:
                return new GuiAdvancedCraftingTerminal(player.inventory, (ITerminalHost) part, new ContainerAdvancedCraftingTerminal(player.inventory, (PartAdvancedCraftingTerminal) part));
            case ELITE_CRAFTING_TERMINAL:
                return new GuiEliteCraftingTerminal(player.inventory, (ITerminalHost) part, new ContainerEliteCraftingTerminal(player.inventory, (PartEliteCraftingTerminal) part));
            case ULTIMATE_CRAFTING_TERMINAL:
                return new GuiUltimateCraftingTerminal(player.inventory, (ITerminalHost) part, new ContainerUltimateCraftingTerminal(player.inventory, (PartUltimateCraftingTerminal) part));
            default:
                return null;
        }
    }
}
