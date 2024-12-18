package com._0xc4de.ae2exttable.mixins;

import appeng.api.parts.IPart;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerCraftConfirm;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.PartGuiHandler;
import com._0xc4de.ae2exttable.interfaces.ITerminalGui;
import com._0xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com._0xc4de.ae2exttable.network.packets.PacketSwitchGui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value= ContainerCraftConfirm.class, remap=false)
public class ContainerCraftConfirmMixin extends AEBaseContainer {

    @Unique
    public AE2ExtendedGUIs aE2ExtendedCraftingTable$extendedOriginalGui;

    public ContainerCraftConfirmMixin(InventoryPlayer ip, TileEntity myTile, IPart myPart) {
      super(ip, myTile, myPart);
    }

    @Inject(method="startJob", at=@At(value="INVOKE", target="Lappeng/container/implementations/ContainerCraftConfirm;setAutoStart(Z)V", shift=At.Shift.AFTER), cancellable=true)
    public void startJobMixin(CallbackInfo ci) {
        TileEntity te = this.getOpenContext().getTile();
        if (te != null) {
            IPart terminal = PartGuiHandler.getPartFromWorld(te.getWorld(), te.getPos(), this.getOpenContext().getSide());
            if (terminal instanceof ITerminalGui t) {
                aE2ExtendedCraftingTable$extendedOriginalGui = t.getGuiType();
                ExtendedTerminalNetworkHandler.instance()
                    .sendToServer(new PacketSwitchGui(
                        aE2ExtendedCraftingTable$extendedOriginalGui));
                ci.cancel();
            }
        }
        if (this.obj != null) {
            if (this.obj.getItemStack().getItem() instanceof ITerminalGui t) {
                aE2ExtendedCraftingTable$extendedOriginalGui = t.getGuiType();
                ExtendedTerminalNetworkHandler.instance()
                    .sendToServer(new PacketSwitchGui(
                        aE2ExtendedCraftingTable$extendedOriginalGui));
                ci.cancel();
            }
        }

    }
}
