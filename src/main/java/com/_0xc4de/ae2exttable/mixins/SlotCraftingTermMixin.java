package com._0xc4de.ae2exttable.mixins;

import appeng.api.storage.IStorageMonitorable;
import appeng.container.ContainerNull;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.interfaces.ICraftingClass;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(value = SlotCraftingTerm.class, remap = false)
public class SlotCraftingTermMixin {

  @Shadow
  private IItemHandler craftInv;

  @Shadow
  private IStorageMonitorable storage;

  @Shadow
  private IItemHandler pattern;

  @Shadow
  private IContainerCraftingPacket container;

  // Go Go Gadget random incantation that works
  @ModifyVariable(method = "craftItem", at= @At(ordinal=0, value = "STORE", target = "Lnet/minecraft/inventory/InventoryCrafting;<init>(Lnet/minecraft/inventory/Container;II)V"))
  private InventoryCrafting inventoryCraftingCraftItem(InventoryCrafting ic) {
    if (this.storage instanceof ICraftingClass strg) {
      ic = new InventoryCrafting(new ContainerNull(), strg.getWidth(), strg.getHeight());
      for(int x = 0; x < strg.getWidth()*strg.getHeight(); ++x) {
        ic.setInventorySlotContents(x, this.pattern.getStackInSlot(x));
      }
    }
    return ic;
  };


  // Random bullshit go
  @ModifyVariable(method = "craftItem", at= @At(ordinal=0, value = "STORE", target = "Lappeng/container/slot/SlotCraftingTerm;findRecipe(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/crafting/IRecipe;"))
  private IRecipe craftItem(IRecipe r) {
    if (this.storage instanceof ICraftingClass strg) {

      // all recipes, as List<IRecipe>, either a TableRecipeShaped or TableRecipeShapeless
      List recipes = TableRecipeManager.getInstance().getRecipes();

      // Just confirm that it's one of the recipes in this list, make r = that recipe

      for (Object recipe : recipes) {
        IRecipe rec = (IRecipe) recipe;
        // For casting purposes
        if (this.container instanceof ContainerMEMonitorableTwo cont) {
          IRecipe currentRec = cont.getCurrentRecipe();
          if (rec == currentRec) {
            return rec;
          }
        }
      }
    }
    return r;
  }
}
