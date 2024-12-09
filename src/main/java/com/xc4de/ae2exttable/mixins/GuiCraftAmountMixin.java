package com.xc4de.ae2exttable.mixins;

import appeng.api.storage.ITerminalHost;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiCraftAmount;
import appeng.client.gui.implementations.GuiCraftConfirm;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.container.AEBaseContainer;
import appeng.helpers.WirelessTerminalGuiObject;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.interfaces.ITerminalGui;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com.xc4de.ae2exttable.network.packets.PacketSwitchGui;
import com.xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(value= GuiCraftAmount.class, remap=false)
public class GuiCraftAmountMixin extends AEBaseGui {

    @Shadow
    private GuiTabButton originalGuiBtn;

    @Shadow
    private GuiButton next;

    private AE2ExtendedGUIs extendedOriginalGui;

    public GuiCraftAmountMixin(Container container) {
        super(container);
    }

    @Inject(method="initGui", at=@At(value="INVOKE", target="Lappeng/api/definitions/IDefinitions;parts()Lappeng/api/definitions/IParts;", shift=At.Shift.AFTER))
    private void onInitGui(CallbackInfo ci) {
        Object target = ((AEBaseContainer) this.inventorySlots).getTarget();
        if (target instanceof ITerminalGui t) {
            setOriginalGui(t);
        }
        if (target instanceof WirelessTerminalGuiObject term) {
            if (term.getItemStack().getItem() instanceof ITerminalGui t) {
                setOriginalGui(t);
            }
        }
    }

    private void setOriginalGui(ITerminalGui t) {
        this.buttonList.remove(null);
        this.extendedOriginalGui = t.getGuiType();
        ItemStack myIcon = new ItemStack(ItemRegistry.partByGuiType(this.extendedOriginalGui));
        this.buttonList.add((this.originalGuiBtn = new GuiTabButton(this.guiLeft + 154, this.guiTop, myIcon, myIcon.getDisplayName(), this.itemRender)));
    }

    // Inject that handles switching back to the original of my gui
    @Inject(method="actionPerformed", at = @At(value="INVOKE", target="Lappeng/client/gui/AEBaseGui;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V", shift = At.Shift.AFTER), cancellable = true)
    protected void actionPerformedGuiSwitch(GuiButton btn, CallbackInfo ci) {
        if (btn == this.originalGuiBtn && this.extendedOriginalGui != null) {
            ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketSwitchGui(this.extendedOriginalGui));
            ci.cancel();
        }
        else if (this.next == btn && this.extendedOriginalGui != null) {
            //ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketSwitchGui(this.extendedOriginalGui));
            //ci.cancel();
        }
    }

    @Shadow
    public void drawFG(int i, int i1, int i2, int i3) {

    }

    @Shadow
    public void drawBG(int i, int i1, int i2, int i3) {

    }
}
