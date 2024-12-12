package com._0xc4de.ae2exttable.mixins;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.implementations.GuiCraftingStatus;
import appeng.helpers.WirelessTerminalGuiObject;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.interfaces.ITerminalGui;
import com._0xc4de.ae2exttable.items.ItemRegistry;
import com._0xc4de.ae2exttable.items.wireless.ItemWirelessBasicTerminal;
import com._0xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com._0xc4de.ae2exttable.network.packets.PacketSwitchGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(value=GuiCraftingStatus.class, remap=false)
public class GuiCraftingStatusMixin {

    @Shadow(remap=false)
    private ItemStack myIcon;

    private AE2ExtendedGUIs extendedOriginalGui;

    @SuppressWarnings("InjectIntoConstructor")
    @Inject(method="<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lappeng/api/storage/ITerminalHost;)V",
            at= @At(value = "INVOKE", target = "Lappeng/api/definitions/IDefinitions;parts()Lappeng/api/definitions/IParts;", shift = At.Shift.AFTER))
    private void onInit(final InventoryPlayer inventoryPlayer, final ITerminalHost te, CallbackInfo ci) {
        if (te instanceof WirelessTerminalGuiObject wt) {
            ItemStack item = wt.getItemStack();
            if (item.getItem() instanceof ItemWirelessBasicTerminal bt) {
                this.extendedOriginalGui = bt.getGuiType();
                this.myIcon = item;
                return;
            }
        }
        if (te instanceof ITerminalGui term) {
            AE2ExtendedGUIs guiType = term.getGuiType();
            this.extendedOriginalGui = guiType;
            this.myIcon = new ItemStack(ItemRegistry.partByGuiType(guiType));
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
