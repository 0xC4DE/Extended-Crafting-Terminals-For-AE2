package com._0xc4de.ae2exttable.network;

import appeng.me.GridAccessException;

public abstract class PacketCallState {
    public PacketCallState() {
    }

    public abstract void call(ExtendedTerminalPacket var1) throws GridAccessException;
}
