package com.xc4de.ae2exttable.mixins;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiCraftingStatus;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com.xc4de.ae2exttable.network.packets.PacketSwitchGui;
import com.xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.lang.reflect.Field;

@Mixin(value=GuiCraftingStatus.class, remap=false)
public class GuiCraftingStatusMixin {

    @Shadow(remap=false)
    private ItemStack myIcon;

    @Unique
    private AE2ExtendedGUIs extendedOriginalGui;

    @SuppressWarnings("InjectIntoConstructor")
    @Inject(method="<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lappeng/api/storage/ITerminalHost;)V",
            at= @At(value = "INVOKE", target = "Lappeng/api/definitions/IDefinitions;parts()Lappeng/api/definitions/IParts;", shift = At.Shift.AFTER))
    private void onInit(final InventoryPlayer inventoryPlayer, final ITerminalHost te, CallbackInfo ci) {
        // First grab the class of the terminal host
        Class<? extends ITerminalHost> clazz = te.getClass();
        // Figure out if the terminal host is one of my crafting terminals
        if (PartSharedCraftingTerminal.class.isAssignableFrom(clazz)) {
            try {
                // This field exists on all PartSharedCraftingTerminal instances
                Field field = clazz.getDeclaredField("guiType");

                // Get guiType and assign it to original constructor
                AE2ExtendedGUIs guiType = (AE2ExtendedGUIs) field.get(AE2ExtendedGUIs.class);
                this.extendedOriginalGui = guiType;
                this.myIcon = new ItemStack(ItemRegistry.partByGuiType(guiType));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Inject(method = "actionPerformed", at = @At(value="INVOKE", target="Lorg/lwjgl/input/Mouse;isButtonDown(I)Z", shift = At.Shift.AFTER), cancellable = true)
    protected void actionPerformed(GuiButton btn, CallbackInfo ci) throws IOException {
        // Defined if the terminal host is one of my crafting terminals
        if (this.extendedOriginalGui != null) {
            ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketSwitchGui(this.extendedOriginalGui));
            ci.cancel();
        }
    }
}
