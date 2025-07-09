package com._0xc4de.ae2exttable.network;

import appeng.core.sync.network.INetworkInfo;
import appeng.core.sync.network.IPacketHandler;
import appeng.me.GridAccessException;
import com._0xc4de.ae2exttable.AE2ExtendedCraftingTable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketThreadUtil;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ServerPacketHandler extends PacketHandler implements IPacketHandler {
    public ServerPacketHandler() {
        super();
    }

    @Override
    public void onPacketData(final INetworkInfo manager, final INetHandler handler, final FMLProxyPacket packet, final EntityPlayer player) {
        final ByteBuf data = packet.payload();
        final PacketTypes type = PacketTypes.getPacket(data.readInt());
        if (type != null) {
            try {
               final ExtendedTerminalPacket realPacket = type.parsePacket(data);

               final PacketCallState state = new PacketCallState() {
                     @Override
                     public void call(final ExtendedTerminalPacket pack) throws GridAccessException {
                         pack.serverPacketData(manager, pack, player);
                     }
               };

               realPacket.setCallParam(state);
               PacketThreadUtil.checkThreadAndEnqueue(realPacket, handler, Objects.requireNonNull(player.getServer()));
                try {
                    state.call(realPacket);
                } catch (GridAccessException e) {
                    throw new RuntimeException(e);
                }

            } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException |
                           InvocationTargetException e) {
                AE2ExtendedCraftingTable.LOGGER.error("Failed to handle packet", e);
            }
        }
    }
}
