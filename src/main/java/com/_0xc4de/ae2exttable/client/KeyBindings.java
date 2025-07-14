package com._0xc4de.ae2exttable.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.input.Keyboard;

import static com._0xc4de.ae2exttable.proxy.ClientProxy.KEY_CATEGORY;

public enum KeyBindings {
    basic(new KeyBinding("key.open_basic_extended_wireless_crafting_terminal", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, Keyboard.KEY_NONE, KEY_CATEGORY)),
    advanced(new KeyBinding("key.open_advanced_extended_wireless_crafting_terminal", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, Keyboard.KEY_NONE, KEY_CATEGORY)),
    elite(new KeyBinding("key.open_elite_extended_wireless_crafting_terminal", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, Keyboard.KEY_NONE, KEY_CATEGORY)),
    ultimate(new KeyBinding("key.open_ultimate_extended_wireless_crafting_terminal", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, Keyboard.KEY_NONE, KEY_CATEGORY));

    private final KeyBinding binding;

    KeyBindings(KeyBinding keyBinding) {
        this.binding = keyBinding;
    }

    public KeyBinding get() {
        return this.binding;
    }
}
