package com.xc4de.ae2exttable.mixins;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiCraftConfirm;
import appeng.core.localization.GuiText;
import appeng.core.sync.GuiBridge;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.interfaces.ITerminalGui;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com.xc4de.ae2exttable.network.packets.PacketSwitchGui;
import com.xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(value= GuiCraftConfirm.class, remap=false)
public class GuiCraftConfirmMixin extends AEBaseGui {

    @Shadow
    private GuiButton start;

    @Shadow
    private GuiBridge OriginalGui;

    @Shadow
    private GuiButton cancel;

    private AE2ExtendedGUIs extendedOriginalGui;

    public GuiCraftConfirmMixin(Container container) {
        super(container);
    }

    @SuppressWarnings("InjectIntoConstructor")
    @Inject(method="<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lappeng/api/storage/ITerminalHost;)V",
            at= @At(value = "INVOKE", target = "Lappeng/container/implementations/ContainerCraftConfirm;setGui(Lappeng/client/gui/implementations/GuiCraftConfirm;)V", shift = At.Shift.AFTER))
    private void onInit(final InventoryPlayer inventoryPlayer, final ITerminalHost te, CallbackInfo ci) {
        if (te instanceof ITerminalGui t) {
            AE2ExtendedGUIs guiType = t.getGuiType();
            this.extendedOriginalGui = guiType;
            ci.cancel();
        }
    };

    // Fixes an error with the Cancel button being null, because of my mixin. idk.
    @Inject(method="initGui", at=@At(value= "RETURN"))
    private void onInitGui(CallbackInfo ci) {
        if (this.extendedOriginalGui != null) {
            this.buttonList.remove(null);
            this.cancel = new GuiButton(0, this.guiLeft + 6, this.guiTop + this.ySize - 25, 50, 20, GuiText.Cancel.getLocal());
            this.buttonList.add(this.cancel);
        }
    }

    // Should actually just switch back to my gui after invoking the start methods
    @Inject(method="actionPerformed", at = @At(value="INVOKE", target="Lorg/lwjgl/input/Mouse;isButtonDown(I)Z", shift = At.Shift.AFTER), cancellable = true)
    protected void actionPerformed(GuiButton btn, CallbackInfo ci) {
        if (btn == this.start) {
            if (this.extendedOriginalGui != null) {
                //ci.cancel();
            }
        }
        if (btn == this.cancel) {
            if (this.extendedOriginalGui != null) {
                ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketSwitchGui(this.extendedOriginalGui));
                ci.cancel();
            }
        }
    }

    @Shadow
    public void drawFG(int i, int i1, int i2, int i3) {

    }

    @Shadow
    public void drawBG(int i, int i1, int i2, int i3) {

    }
}
