/**
 * Class shamelessly stolen and adapted from AE2-UEL
 */
package com._0xc4de.ae2exttable.network;

import appeng.core.sync.network.INetworkInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class ExtendedTerminalPacket implements Packet {
    private PacketBuffer p;
    private PacketCallState caller;

    public ExtendedTerminalPacket() {
    }

    public void serverPacketData(INetworkInfo manager, ExtendedTerminalPacket packet, EntityPlayer player) {
        throw new UnsupportedOperationException("This packet ( " + this.getPacketID() + " ) does not implement a server side handler.");
    }

    public final int getPacketID() {
        return PacketHandler.PacketTypes.getID(this.getClass()).ordinal();
    }

    public void clientPacketData(INetworkInfo network, ExtendedTerminalPacket packet, EntityPlayer player) {
        throw new UnsupportedOperationException("This packet ( " + this.getPacketID() + " does not implement a client side handler.");
    }

    protected void configureWrite(ByteBuf data) {
        data.capacity(data.readableBytes());
        this.p = new PacketBuffer(data);
    }

    public FMLProxyPacket getProxy() {
        if (this.p.array().length > 2097152) {
            throw new IllegalArgumentException("Sorry AE2 Extended Terminals made a " + this.p.array().length + " byte packet by accident!");
        } else {
            FMLProxyPacket pp = new FMLProxyPacket(this.p, ExtendedTerminalNetworkHandler.instance().getChannel());
            /*
            if (AEConfig.instance().isFeatureEnabled(AEFeature.PACKET_LOGGING)) {
                AELog.info(this.getClass().getName() + " : " + pp.payload().readableBytes(), new Object[0]);
            }
             */
            return pp;
        }
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        throw new RuntimeException("Not Implemented");
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        throw new RuntimeException("Not Implemented");
    }

    public ByteArrayInputStream getPacketByteArray(ByteBuf stream, int readerIndex, int readableBytes) {
        ByteArrayInputStream bytes;
        if (stream.hasArray()) {
            bytes = new ByteArrayInputStream(stream.array(), readerIndex, readableBytes);
        } else {
            byte[] data = new byte[stream.capacity()];
            stream.getBytes(readerIndex, data, 0, readableBytes);
            bytes = new ByteArrayInputStream(data);
        }

        return bytes;
    }

    public ByteArrayInputStream getPacketByteArray(ByteBuf stream) {
        return this.getPacketByteArray(stream, 0, stream.readableBytes());
    }

    public void setCallParam(PacketCallState call) {
        this.caller = call;
    }

    public void processPacket(INetHandler handler) {
        this.caller.call(this);
    }

}