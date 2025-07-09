package com._0xc4de.ae2exttable.network.packets;

import appeng.api.networking.security.IActionHost;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.core.sync.network.INetworkInfo;
import appeng.me.GridAccessException;
import appeng.util.Platform;
import baubles.api.BaublesApi;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.PartGuiHandler;
import com._0xc4de.ae2exttable.items.ItemRegistry;
import com._0xc4de.ae2exttable.network.ExtendedTerminalPacket;
import com._0xc4de.ae2exttable.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;


public class PacketOpenWirelessGui extends ExtendedTerminalPacket {

  private final AE2ExtendedGUIs gui;

  public PacketOpenWirelessGui(final ByteBuf stream) {
    this.gui = AE2ExtendedGUIs.values()[stream.readInt()];
  }

  public PacketOpenWirelessGui(final AE2ExtendedGUIs gui) {
    this.gui = gui;
    final ByteBuf data = Unpooled.buffer();
    data.writeInt(this.getGuiPacketID());
    data.writeInt(gui.ordinal());

    this.configureWrite(data);
  }

  public int getGuiPacketID() {
    return PacketHandler.PacketTypes.getID(this.getClass()).ordinal();
  }

  @Override
  public void serverPacketData(final INetworkInfo manager,
                               final ExtendedTerminalPacket packet,
                               final EntityPlayer player) throws GridAccessException {
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        ItemStack guiItem = new ItemStack(ItemRegistry.partByGuiType(this.gui));
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack is = inventory.get(i);
            if (Platform.itemComparisons().isEqualItemType(is, guiItem)) {
                openGui(is, i, player, false);
                return;
            }
        }
        if (Platform.isModLoaded("baubles")) {
            for (int i = 0; i< BaublesApi.getBaublesHandler(player).getSlots(); i++) {
                ItemStack is = BaublesApi.getBaublesHandler(player).getStackInSlot(i);
                if (Platform.itemComparisons().isEqualItemType(is, guiItem)) {
                  openGui(is, i, player, true);
                  return;
                }
            }
        }
    }

    private void openGui(ItemStack itemStack, int slotIndex, EntityPlayer player, boolean isBauble) throws GridAccessException {
        PartGuiHandler.openWirelessTerminalGui(itemStack, slotIndex, isBauble, player.world, player, this.gui);
    }
}
