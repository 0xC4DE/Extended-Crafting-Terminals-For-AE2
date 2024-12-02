package com.xc4de.ae2exttable.network;

import appeng.core.sync.AppEngPacket;
import appeng.core.sync.network.IPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ExtendedTerminalNetworkHandler {
    public static ExtendedTerminalNetworkHandler instance;
    private final FMLEventChannel ec;
    private final String myChannelName;

    //private final IPacketHandler clientHandler;
    private final IPacketHandler serveHandler;

    public ExtendedTerminalNetworkHandler(final String channelName) {
        FMLCommonHandler.instance().bus().register(this);
        this.ec = NetworkRegistry.INSTANCE.newEventDrivenChannel(this.myChannelName = channelName);
        this.ec.register(this);

        //this.clientHandler = this.createClientSide();
        this.serveHandler = this.createServerSide();
    }

    public static void init(final String channelName) {
        instance = new ExtendedTerminalNetworkHandler(channelName);
    }

    public static ExtendedTerminalNetworkHandler instance() {
        return instance;
    }

    /*
    private IPacketHandler createClientSide() {
        try {
            return new AppEngClientPacketHandler();
        } catch (final Throwable t) {
            return null;
        }
    }
     */

    private IPacketHandler createServerSide() {
        try {
            return new ServerPacketHandler();
        } catch (final Throwable t) {
            return null;
        }
    }

    @SubscribeEvent
    public void serverPacket(final FMLNetworkEvent.ServerCustomPacketEvent ev) {
        final NetHandlerPlayServer srv = (NetHandlerPlayServer) ev.getPacket().handler();
        if (this.serveHandler != null) {
            try {
                this.serveHandler.onPacketData(null, ev.getHandler(), ev.getPacket(), srv.player);
            } catch (final ThreadQuickExitException ignored) {

            }
        }
    }

    /*
    @SubscribeEvent
    public void clientPacket(final ClientCustomPacketEvent ev) {
        if (this.clientHandler != null) {
            try {
                this.clientHandler.onPacketData(null, ev.getHandler(), ev.getPacket(), null);
            } catch (final ThreadQuickExitException ignored) {

            }
        }
    } */

    public String getChannel() {
        return this.myChannelName;
    }

    public void sendToAll(final AppEngPacket message) {
        this.ec.sendToAll(message.getProxy());
    }

    public void sendTo(final AppEngPacket message, final EntityPlayerMP player) {
        this.ec.sendTo(message.getProxy(), player);
    }

    public void sendToAllAround(final AppEngPacket message, final NetworkRegistry.TargetPoint point) {
        this.ec.sendToAllAround(message.getProxy(), point);
    }

    public void sendToAllTracking(final AppEngPacket message, final NetworkRegistry.TargetPoint point) {
        this.ec.sendToAllTracking(message.getProxy(), point);
    }

    public void sendToDimension(final AppEngPacket message, final int dimensionId) {
        this.ec.sendToDimension(message.getProxy(), dimensionId);
    }

    public void sendToServer(final ExtendedTerminalPacket message) {
        this.ec.sendToServer(message.getProxy());
    }
}
