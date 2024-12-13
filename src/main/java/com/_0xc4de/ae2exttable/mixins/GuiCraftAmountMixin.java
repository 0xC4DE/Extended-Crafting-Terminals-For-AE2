package com._0xc4de.ae2exttable.mixins;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiCraftAmount;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.container.AEBaseContainer;
import appeng.helpers.WirelessTerminalGuiObject;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.interfaces.ITerminalGui;
import com._0xc4de.ae2exttable.items.ItemRegistry;
import com._0xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com._0xc4de.ae2exttable.network.packets.PacketSwitchGui;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value= GuiCraftAmount.class, remap=false)
public class GuiCraftAmountMixin extends AEBaseGui {

    @Shadow
    private GuiTabButton originalGuiBtn;

    @Shadow
    private GuiButton next;

    @Unique
    private AE2ExtendedGUIs aE2ExtendedCraftingTable$extendedOriginalGui;

    public GuiCraftAmountMixin(Container container) {
        super(container);
    }

    @Inject(method="initGui", at=@At(value="RETURN"), remap=true)
    private void onInitGui(CallbackInfo ci) {
        Object target = ((AEBaseContainer) this.inventorySlots).getTarget();
        if (target instanceof ITerminalGui t) {
            this.aE2ExtendedCraftingTable$setOriginalGui(t);
        }
        if (target instanceof WirelessTerminalGuiObject term) {
            if (term.getItemStack().getItem() instanceof ITerminalGui t) {
                this.aE2ExtendedCraftingTable$setOriginalGui(t);
            }
        }
    }

    @Unique
    private void aE2ExtendedCraftingTable$setOriginalGui(ITerminalGui t) {
        for (Object btn: new ArrayList<>(this.buttonList)) {
           if (btn instanceof GuiTabButton b) {
              this.buttonList.remove(b);
           }
        }
        this.aE2ExtendedCraftingTable$extendedOriginalGui = t.getGuiType();
        ItemStack myIcon = new ItemStack(ItemRegistry.partByGuiType(this.aE2ExtendedCraftingTable$extendedOriginalGui));
        this.buttonList.add((this.originalGuiBtn = new GuiTabButton(this.guiLeft + 154, this.guiTop, myIcon, myIcon.getDisplayName(), this.itemRender)));
    }

    // Inject that handles switching back to the original of my gui
    @Inject(method="actionPerformed", at = @At(value="INVOKE", target="Lappeng/client/gui/AEBaseGui;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V", shift = At.Shift.AFTER), cancellable = true, remap=true)
    protected void actionPerformedGuiSwitch(GuiButton btn, CallbackInfo ci) {
        if (btn == this.originalGuiBtn && this.aE2ExtendedCraftingTable$extendedOriginalGui != null) {
            ExtendedTerminalNetworkHandler.instance().sendToServer(new PacketSwitchGui(this.aE2ExtendedCraftingTable$extendedOriginalGui));
            ci.cancel();
        }
    }

    @Shadow
    public void drawFG(int i, int i1, int i2, int i3) {

    }

    @Shadow
    public void drawBG(int i, int i1, int i2, int i3) {

    }
}
