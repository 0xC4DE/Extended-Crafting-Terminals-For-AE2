package com._0xc4de.ae2exttable.network;

import com._0xc4de.ae2exttable.network.packets.PacketSwitchGui;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler {
    private static final Map<Class<? extends ExtendedTerminalPacket>, PacketTypes> REVERSE_LOOKUP = new HashMap<>();
   public enum PacketTypes {
       DEFAULT,
       PACKET_SWITCH_GUI(PacketSwitchGui.class);

       private final Constructor<? extends ExtendedTerminalPacket> packetConstructor;

       PacketTypes() {
          this.packetConstructor = null;
       }

       PacketTypes(final Class<? extends ExtendedTerminalPacket> packetClass) {
           Constructor<? extends ExtendedTerminalPacket> c = null;
           try {
               c = packetClass.getConstructor(ByteBuf.class);
           } catch (NoSuchMethodException ignored) {}

           this.packetConstructor = c;
           REVERSE_LOOKUP.put(packetClass, this);
       }

       public static PacketTypes getPacket(final int id) {
           return (values())[id];
       }

        public static PacketTypes getID(final Class<? extends ExtendedTerminalPacket> packetClass) {
            return REVERSE_LOOKUP.get(packetClass);
        }

       public ExtendedTerminalPacket parsePacket(final ByteBuf in) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
           return this.packetConstructor.newInstance(in);
       }

   };
}
