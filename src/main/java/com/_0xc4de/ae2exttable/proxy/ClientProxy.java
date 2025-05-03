package com._0xc4de.ae2exttable.proxy;

import com._0xc4de.ae2exttable.client.KeyBindings;
import com._0xc4de.ae2exttable.items.ItemRegistry;
import com._0xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com._0xc4de.ae2exttable.network.packets.PacketOpenWirelessGui;
import com._0xc4de.ae2exttable.network.packets.PacketSwitchGui;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs.*;
import static com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs.ULTIMATE_CRAFTING_TERMINAL;

public class ClientProxy extends CommonProxy {
    public static String KEY_CATEGORY = "key.ae2exttable.category";

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ItemRegistry.initModels();
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        for (KeyBindings binding : KeyBindings.values()) {
            ClientRegistry.registerKeyBinding(binding.get());
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onInputEvent(final InputEvent.KeyInputEvent event) {
        for (KeyBindings k : KeyBindings.values()) {
            if (k.get().isPressed()) {
                if (k.get() == KeyBindings.basic.get()) {
                    ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketOpenWirelessGui(WIRELESS_BASIC_CRAFTING_TERMINAL));
                }
                else if (k.get() == KeyBindings.advanced.get()) {
                    ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketOpenWirelessGui(WIRELESS_ADVANCED_CRAFTING_TERMINAL));
                } else if (k.get() == KeyBindings.elite.get()) {
                    ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketOpenWirelessGui(WIRELESS_ELITE_CRAFTING_TERMINAL));
                } else if (k.get() == KeyBindings.ultimate.get()) {
                    ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketOpenWirelessGui(WIRELESS_ULTIMATE_CRAFTING_TERMINAL));
                }
            }
        }
    }
}
