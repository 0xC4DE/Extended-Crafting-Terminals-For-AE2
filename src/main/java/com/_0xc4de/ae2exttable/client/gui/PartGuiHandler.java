/**
 * Yes this is essentially a copy of GuiBridge mixed with Platform from AE2-UEL
 * Yes it's because I didn't understand how to use External GUIs.
 */

package com._0xc4de.ae2exttable.client.gui;

import appeng.api.AEApi;
import appeng.api.features.ILocatable;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.features.IWirelessTermRegistry;
import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AEPartLocation;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import appeng.core.localization.PlayerMessages;
import appeng.me.GridAccessException;
import appeng.tile.misc.TileSecurityStation;
import appeng.util.Platform;
import baubles.api.BaublesApi;
import com._0xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerBasicCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerEliteCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerAdvancedWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerBasicWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerEliteWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerUltimateWirelessTerminal;
import com._0xc4de.ae2exttable.client.gui.terminals.GuiAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.terminals.GuiBasicCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.terminals.GuiEliteCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.terminals.GuiUltimateCraftingTerminal;
import com._0xc4de.ae2exttable.client.gui.wireless.GuiWirelessAdvancedCraftingTerm;
import com._0xc4de.ae2exttable.client.gui.wireless.GuiWirelessBasicCraftingTerm;
import com._0xc4de.ae2exttable.client.gui.wireless.GuiWirelessEliteCraftingTerm;
import com._0xc4de.ae2exttable.client.gui.wireless.GuiWirelessUltimateCraftingTerm;
import com._0xc4de.ae2exttable.part.PartAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.part.PartBasicCraftingTerminal;
import com._0xc4de.ae2exttable.part.PartEliteCraftingTerminal;
import com._0xc4de.ae2exttable.part.PartUltimateCraftingTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class PartGuiHandler implements IGuiHandler {
    public static void openGUI(AE2ExtendedGUIs gui, EntityPlayer player,
                               BlockPos pos, AEPartLocation side) {

        if (gui == null) {
            throw new IllegalArgumentException("gui cannot be null");
        } else if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }

        player.openGui(AE2ExtendedCraftingTable.instance,
                PartGuiHandler.calculateOrdinal(gui, side),
                player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
    }

    public static void openWirelessTerminalGui(ItemStack item, int invSlot,
                                               boolean isBauble, World w,
                                               EntityPlayer player,
                                               AE2ExtendedGUIs gui) throws GridAccessException {
        IWirelessTermRegistry registry = AEApi.instance().registries().wireless();
        if (Platform.isClient()) {
            return;
        }
        if (!registry.isWirelessTerminal(item)) {
            player.sendMessage(PlayerMessages.DeviceNotWirelessTerminal.get());
            return;
        }
        final IWirelessTermHandler handler =
                registry.getWirelessTerminalHandler(item);
        final String unparsedKey = handler.getEncryptionKey(item);
        if (unparsedKey.isEmpty()) {
            player.sendMessage(PlayerMessages.DeviceNotLinked.get());
            return;
        }
        final long parsedKey = Long.parseLong(unparsedKey);
        final ILocatable
                securityStation =
                AEApi.instance().registries().locatable().getLocatableBy(parsedKey);
        TileSecurityStation security_station = (TileSecurityStation) securityStation;
        IGrid grid = security_station.getProxy().getGrid();
        IStorageGrid inv = grid.getCache(IStorageGrid.class);
        final IMEMonitor<IAEItemStack> storage = inv.getInventory(AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class));
        if (securityStation == null) {
            player.sendMessage(PlayerMessages.StationCanNotBeLocated.get());
            return;
        }
        if (handler.hasPower(player, 0.5, item)) {
            openGUI(gui, player,
                    new BlockPos(invSlot, isBauble ? 1 : 0, Integer.MIN_VALUE),
                    AEPartLocation.fromFacing(EnumFacing.DOWN));
        } else {
            player.sendMessage(PlayerMessages.DeviceNotPowered.get());
        }
    }

    public static int calculateOrdinal(AE2ExtendedGUIs gui, AEPartLocation side) {
        if (guiIsWirelessTerminal(gui)) {
            return (gui.ordinal() << 4);
        }
        if (side == null) {
            side = AEPartLocation.fromOrdinal(AEPartLocation.UP.ordinal());
        }
        return (gui.ordinal() << 4) | side.ordinal();
    }

    public static AE2ExtendedGUIs getGUIFromOrdinal(int ordinal) {
        return AE2ExtendedGUIs.values()[ordinal >> 4];
    }

    public static AEPartLocation getSideFromOrdinal(int ordinal) {
        return AEPartLocation.fromOrdinal(ordinal & 7);
    }

    public static IPart getPartFromWorld(World world, BlockPos pos,
                                         AEPartLocation side) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IPartHost) {
            return ((IPartHost) te).getPart(side);
        }
        return null;
    }

    private Object updateGui(final Object newContainer, final World w,
                             final int x, final int y, final int z,
                             final AEPartLocation side, final Object myItem) {
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
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player,
                                                World world, int x, int y,
                                                int z) {
        AE2ExtendedGUIs guiID = PartGuiHandler.getGUIFromOrdinal(ID);
        AEPartLocation side = PartGuiHandler.getSideFromOrdinal(ID);
        final boolean usingItemOnTile = ((ID >> 3) & 1) == 1;

        if (guiIsWirelessTerminal(guiID)) { // idk is item
            ItemStack termItem = getWirelessTermItem(usingItemOnTile, player, world, guiID, side, x, y, z);
            final Object item =
                    this.getGuiObject(guiID, termItem, player, world, x, y, z, side);
            return this.updateGui(item, world, x, y, z, side, termItem);
        } else {

            final TileEntity TE = world.getTileEntity(new BlockPos(x, y, z));

            if (TE instanceof IPartHost) {
                final IPart part = ((IPartHost) TE).getPart(side);
                if (part == null) {
                    return null;
                }
                Object gui =
                        this.getGuiObject(guiID, ItemStack.EMPTY, player, world, x, y, z,
                                side);
                return this.updateGui(gui, world, x, y, z, side, part);
            }
        }

        Object gui =
                this.getGuiObject(guiID, ItemStack.EMPTY, player, world, x, y, z, side);
        return this.updateGui(gui, world, x, y, z, side, ItemStack.EMPTY);
    }

    private Object getGuiObject(final AE2ExtendedGUIs guiID,
                                final ItemStack myItem, final EntityPlayer player,
                                final World world, final int x, final int y,
                                final int z, final AEPartLocation side) {

        if (myItem.isEmpty()) {
            IPart part =
                    PartGuiHandler.getPartFromWorld(world, new BlockPos(x, y, z), side);

            return switch (guiID) {
                case BASIC_CRAFTING_TERMINAL -> new ContainerBasicCraftingTerminal(player.inventory,
                        (PartBasicCraftingTerminal) part);
                case ADVANCED_CRAFTING_TERMINAL -> new ContainerAdvancedCraftingTerminal(player.inventory,
                        (PartAdvancedCraftingTerminal) part);
                case ELITE_CRAFTING_TERMINAL -> new ContainerEliteCraftingTerminal(player.inventory,
                        (PartEliteCraftingTerminal) part);
                case ULTIMATE_CRAFTING_TERMINAL -> new ContainerUltimateCraftingTerminal(player.inventory,
                        (PartUltimateCraftingTerminal) part);
                default -> null;
            };
        } else {
            WirelessTerminalGuiObjectTwo wireless = null;
            if (guiIsWirelessTerminal(guiID)) {
                final IWirelessTermHandler handler =
                        AEApi.instance().registries().wireless()
                                .getWirelessTerminalHandler(myItem);

                wireless =
                        new WirelessTerminalGuiObjectTwo(handler, myItem, player, world, x,
                                y, z);
            }

            return switch (guiID) {
                // Start wireless stuffs here
                case WIRELESS_BASIC_CRAFTING_TERMINAL -> new ContainerBasicWirelessTerminal(player.inventory, wireless);
                case WIRELESS_ADVANCED_CRAFTING_TERMINAL ->
                        new ContainerAdvancedWirelessTerminal(player.inventory, wireless);
                case WIRELESS_ELITE_CRAFTING_TERMINAL -> new ContainerEliteWirelessTerminal(player.inventory, wireless);
                case WIRELESS_ULTIMATE_CRAFTING_TERMINAL ->
                        new ContainerUltimateWirelessTerminal(player.inventory, wireless);
                default -> null;
            };
        }
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player,
                                                World world, int x, int y,
                                                int z) {
        AE2ExtendedGUIs guiID = PartGuiHandler.getGUIFromOrdinal(ID);
        AEPartLocation side = PartGuiHandler.getSideFromOrdinal(ID);
        IPart part =
                PartGuiHandler.getPartFromWorld(world, new BlockPos(x, y, z), side);
        final boolean usingItemOnTile = ((ID >> 3) & 1) == 1;

        WirelessTerminalGuiObjectTwo gui = null;
        if (guiIsWirelessTerminal(guiID)) {
            ItemStack termItem = getWirelessTermItem(usingItemOnTile, player, world, guiID, side, x, y, z);
            final IWirelessTermHandler handler =
                    AEApi.instance().registries().wireless()
                            .getWirelessTerminalHandler(termItem);
            gui = new WirelessTerminalGuiObjectTwo(
                    handler, player.inventory.getCurrentItem(), player, world, x, y, z);
        }

        switch (guiID) {
            case BASIC_CRAFTING_TERMINAL:
                GuiBasicCraftingTerminal term =
                        new GuiBasicCraftingTerminal(player.inventory, (ITerminalHost) part,
                                new ContainerBasicCraftingTerminal(player.inventory,
                                        (PartBasicCraftingTerminal) part));
                return term;
            case ADVANCED_CRAFTING_TERMINAL:
                return new GuiAdvancedCraftingTerminal(player.inventory,
                        (ITerminalHost) part,
                        new ContainerAdvancedCraftingTerminal(player.inventory,
                                (PartAdvancedCraftingTerminal) part));
            case ELITE_CRAFTING_TERMINAL:
                return new GuiEliteCraftingTerminal(player.inventory,
                        (ITerminalHost) part,
                        new ContainerEliteCraftingTerminal(player.inventory,
                                (PartEliteCraftingTerminal) part));
            case ULTIMATE_CRAFTING_TERMINAL:
                return new GuiUltimateCraftingTerminal(player.inventory,
                        (ITerminalHost) part,
                        new ContainerUltimateCraftingTerminal(player.inventory,
                                (PartUltimateCraftingTerminal) part));

            case WIRELESS_BASIC_CRAFTING_TERMINAL:
                // TODO: Fix for baubles, I guess?
                return new GuiWirelessBasicCraftingTerm(player.inventory, gui,
                        new ContainerBasicWirelessTerminal(
                                player.inventory, gui));

            case WIRELESS_ADVANCED_CRAFTING_TERMINAL:
                return new GuiWirelessAdvancedCraftingTerm(player.inventory, gui,
                        new ContainerAdvancedWirelessTerminal(
                                player.inventory, gui));

            case WIRELESS_ELITE_CRAFTING_TERMINAL:
                return new GuiWirelessEliteCraftingTerm(player.inventory, gui,
                        new ContainerEliteWirelessTerminal(
                                player.inventory, gui));

            case WIRELESS_ULTIMATE_CRAFTING_TERMINAL:
                return new GuiWirelessUltimateCraftingTerm(player.inventory, gui,
                        new ContainerUltimateWirelessTerminal(
                                player.inventory, gui));
            default:
                return null;
        }
    }

    private ItemStack getWirelessTermItem(boolean usingItemOnTile, EntityPlayer player, World world, AE2ExtendedGUIs guiID, AEPartLocation side, int x, int y, int z) {
        ItemStack termItem = ItemStack.EMPTY;
        if (usingItemOnTile) {
            termItem = player.inventory.getCurrentItem();
        } else if (y == 0) {
            if (x >= 0 && x < player.inventory.mainInventory.size()) {
                termItem = player.inventory.getStackInSlot(x);
            }
        } else if (y == 1 && z == Integer.MIN_VALUE) {
            termItem = BaublesApi.getBaublesHandler(player).getStackInSlot(x);
        }
        return termItem;
    }

    ;

    public static boolean guiIsWirelessTerminal(AE2ExtendedGUIs gui) {
        return gui.ordinal() > 4;
    }
}
