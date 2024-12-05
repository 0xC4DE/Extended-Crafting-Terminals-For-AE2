package com.xc4de.ae2exttable.mixins;

import appeng.core.features.registries.WirelessRegistry;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import com.xc4de.ae2exttable.items.ItemWirelessBasicTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WirelessRegistry.class, remap = false)
public class WirelessRegistryMixin {
  @Inject(method = "openWirelessTerminalGui", at = @At(value = "INVOKE",
      target = "Lappeng/api/features/IWirelessTermHandler;hasPower(Lnet/minecraft/entity/player/EntityPlayer;DLnet/minecraft/item/ItemStack;)Z", shift = At.Shift.AFTER), cancellable = true)
  private void openWirelessTerminalGuiMixin(ItemStack item, World w,
                                            EntityPlayer player,
                                            CallbackInfo ci) {
    if (item.getItem() instanceof ItemWirelessBasicTerminal it) {
      //PartGuiHandler.openGUI(it.getGuiType(), player, null,
      //    null, true);
      ci.cancel();
    }
  }
}
