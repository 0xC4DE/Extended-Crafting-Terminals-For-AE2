package com.xc4de.ae2exttable.network.packets;

import appeng.api.networking.security.IActionHost;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.network.INetworkInfo;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import com.xc4de.ae2exttable.network.ExtendedTerminalPacket;
import com.xc4de.ae2exttable.network.PacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class PacketSwitchGui extends ExtendedTerminalPacket {

    private final AE2ExtendedGUIs gui;

    public PacketSwitchGui(final ByteBuf stream) {
        this.gui = AE2ExtendedGUIs.values()[stream.readInt()];
    }

    public PacketSwitchGui(final AE2ExtendedGUIs gui) {
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
    public void serverPacketData(final INetworkInfo manager, final ExtendedTerminalPacket packet, final EntityPlayer player) {
        final Container c = player.openContainer;
        if (c instanceof AEBaseContainer bc) {
            final ContainerOpenContext context = bc.getOpenContext();
            if (context != null) {
                final Object target = bc.getTarget();
                if (target instanceof IActionHost ah) {

                    final TileEntity te = context.getTile();

                    if (te != null) {
                        PartGuiHandler.openGUI(this.gui, player, te.getPos(), context.getSide());
                    } else {
                        if (ah instanceof IInventorySlotAware) {
                            return;
                            //IInventorySlotAware i = ((IInventorySlotAware) ah);
                            // TODO: BAUBLES
                            //PartGuiHandler.openGUI(player, i.getInventorySlot(), this.gui, i.isBaubleSlot());
                        }
                    }
                }
            }
        }
    }


}
