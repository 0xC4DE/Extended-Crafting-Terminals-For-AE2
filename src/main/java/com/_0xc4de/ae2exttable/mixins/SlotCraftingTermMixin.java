package com._0xc4de.ae2exttable.mixins;

import appeng.api.storage.IStorageMonitorable;
import appeng.container.ContainerNull;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.interfaces.ICraftingClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = SlotCraftingTerm.class, remap = false)
public abstract class SlotCraftingTermMixin {

  @Shadow
  private IItemHandler craftInv;

  @Shadow
  private IStorageMonitorable storage;

  @Shadow
  private IItemHandler pattern;

  @Shadow
  private IContainerCraftingPacket container;

  @Shadow
  protected abstract IRecipe findRecipe(InventoryCrafting ic, World world,
      EntityPlayer player);

  // Go Go Gadget random incantation that works
  @ModifyVariable(method = "craftItem", at = @At(ordinal = 0, value = "STORE", target = "Lnet/minecraft/inventory/InventoryCrafting;<init>(Lnet/minecraft/inventory/Container;II)V"))
  private InventoryCrafting inventoryCraftingCraftItem(InventoryCrafting ic) {
    if (this.storage instanceof ICraftingClass strg) {
      ic = new InventoryCrafting(new ContainerNull(), strg.getWidth(),
          strg.getHeight());
      for (int x = 0; x < strg.getWidth() * strg.getHeight(); ++x) {
        ic.setInventorySlotContents(x, this.pattern.getStackInSlot(x));
      }
    }
    return ic;
  }


  /**
   * Avoid re-computing the recipe the slow way.
   * See {@link ContainerMEMonitorableTwo#matchesTerminalRecipe} for rationale.
   */
  @Redirect(method = "craftItem", at = @At(value = "INVOKE", target = "Lappeng/container/slot/SlotCraftingTerm;findRecipe(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/crafting/IRecipe;"))
  private IRecipe ae2exttable$getMatchingRecipe(SlotCraftingTerm slot, InventoryCrafting inventory,
      World world, EntityPlayer player) {
    if (this.container instanceof ContainerMEMonitorableTwo cont) {
      IRecipe recipe = cont.getCurrentRecipe();
      if (recipe != null && cont.matchesTerminalRecipe(recipe, inventory, world)) {
        return recipe;
      }

      return null;
    }

    return this.findRecipe(inventory, world, player);
  }

  /**
   * Ensure AE2 sees our extended slots as valid for returning remaining items,
   * otherwise reusable/durability items will be voided when crafting.
   */
  @Inject(method = "getRemainingItems", at = @At("HEAD"), cancellable = true)
  private void ae2exttable$getRemainingItems(InventoryCrafting inventory, World world,
      CallbackInfoReturnable<NonNullList<ItemStack>> callback) {
    if (this.container instanceof ContainerMEMonitorableTwo cont) {
      IRecipe recipe = cont.getCurrentRecipe();
      if (recipe != null && cont.matchesTerminalRecipe(recipe, inventory, world)) {
        callback.setReturnValue(recipe.getRemainingItems(inventory));
      }
    }
  }
}
