package com._0xc4de.ae2exttable.mixins;

import appeng.container.ContainerNull;
import appeng.container.slot.AppEngCraftingSlot;
import appeng.container.slot.AppEngSlot;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = AppEngCraftingSlot.class, remap = false)
public abstract class AppEngCraftingSlotMixin extends AppEngSlot {

  public AppEngCraftingSlotMixin(IItemHandler inv, int idx, int x, int y) {
    super(inv, idx, x, y);
  }

  // More sketchy BS to help appeng realize that there's not only 3x3 crafting terms :(
  @ModifyVariable(method = "onTake", at = @At(ordinal = 0, value = "STORE", target = "Lnet/minecraft/inventory/InventoryCrafting;<init>(Lnet/minecraft/inventory/Container;II)V"))
  private InventoryCrafting inventoryCraftingOnTake(InventoryCrafting ic) {
    if (this.getContainer() instanceof ContainerMEMonitorableTwo container) {
      ic = new InventoryCrafting(new ContainerNull(), container.getWidth(),
          container.getHeight());
      return ic;
    }
    return ic;
  }
}
