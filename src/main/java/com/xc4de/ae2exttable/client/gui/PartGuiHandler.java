package com.xc4de.ae2exttable.client.gui;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.storage.ITerminalHost;
import appeng.api.util.AEPartLocation;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import appeng.helpers.WirelessTerminalGuiObject;
import baubles.api.BaublesApi;
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
import net.minecraft.item.ItemStack;
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
        final boolean usingItemOnTile = ((ID >> 3) & 1) == 1;
        if (guiID.ordinal() > 4) { // idk is item
           ItemStack it = ItemStack.EMPTY;
           if (usingItemOnTile) {
               it = player.inventory.getCurrentItem();
           } else if (y == 0) {
               if (x >= 0 && x < player.inventory.mainInventory.size()) {
                   it = player.inventory.getStackInSlot(x);
               }
           } else if (y == 1 && z == Integer.MIN_VALUE) {
               it = BaublesApi.getBaublesHandler(player).getStackInSlot(x);
           }
           final Object item = this.getGuiObject(guiID, it, player, world, x, y, z, side);
           return this.updateGui(item, world, x, y, z, side, it);
        }
        final TileEntity TE = world.getTileEntity(new BlockPos(x,y,z));
        if (TE instanceof IPartHost) {
            final IPart part = ((IPartHost) TE).getPart(side);
            if (part == null) {
                return null;
            }
            Object gui = this.getGuiObject(guiID, ItemStack.EMPTY, player, world, x, y, z, side);
            return this.updateGui(gui, world, x, y, z, side, part);
        }

        Object gui = this.getGuiObject(guiID, ItemStack.EMPTY, player, world, x, y, z, side);
        return this.updateGui(gui, world, x, y, z, side, ItemStack.EMPTY);
    }

    private Object getGuiObject(final AE2ExtendedGUIs guiID, final ItemStack myItem, final EntityPlayer player, final World world, final int x, final int y, final int z, final AEPartLocation side) {
        // final IWirelessTermHandler wh = AEApi.instance().registries().wireless().getWirelessTerminalHandler(it);
        //if (wh != null) {
        //   return new WirelessTerminalGuiObject(wh, myItem, player, world, x, y, z, side);
        //}
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

    private Object updateGui(final Object newContainer, final World w, final int x, final int y, final int z, final AEPartLocation side, final Object myItem) {
        if (newContainer instanceof AEBaseContainer) {
            final AEBaseContainer bc = (AEBaseContainer) newContainer;
            bc.setOpenContext(new ContainerOpenContext(myItem));
            bc.getOpenContext().setWorld(w);
            bc.getOpenContext().setX(x);
            bc.getOpenContext().setY(y);
            bc.getOpenContext().setZ(z);
            bc.getOpenContext().setSide(side);
        }

        return newContainer;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        AE2ExtendedGUIs guiID = PartGuiHandler.getGUIFromOrdinal(ID);
        AEPartLocation side = PartGuiHandler.getSideFromOrdinal(ID);
        IPart part = PartGuiHandler.getPartFromWorld(world, new BlockPos(x,y,z), side);

        switch(guiID) {
            case BASIC_CRAFTING_TERMINAL:
                GuiBasicCraftingTerminal term =  new GuiBasicCraftingTerminal(player.inventory, (ITerminalHost) part, new ContainerBasicCraftingTerminal(player.inventory, (PartBasicCraftingTerminal) part));
                return term;
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
