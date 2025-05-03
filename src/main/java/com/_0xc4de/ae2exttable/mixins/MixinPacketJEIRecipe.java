/*
This file is brought to you by NotMyWing,

I have stolen all the code, credit to @NotMyWing  :)
 */

package com._0xc4de.ae2exttable.mixins;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.crafting.ICraftingJob;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerCraftConfirm;
import appeng.container.implementations.ContainerPatternEncoder;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.core.AELog;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.INetworkInfo;
import appeng.core.sync.packets.PacketJEIRecipe;
import appeng.helpers.IContainerCraftingPacket;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import appeng.util.item.ItemStackHashStrategy;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import co.neeve.nae2.common.helpers.VirtualPatternDetails;
import co.neeve.nae2.common.interfaces.IExtendedCraftingGridCache;
import net.minecraftforge.fml.common.Optional;


import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Mixin(value = PacketJEIRecipe.class, remap = false)
public class MixinPacketJEIRecipe {
    @Unique
    private boolean ae2exttable$craft;

    @Unique
    private boolean ae2exttable$autoStart;
    @Shadow
    private List<ItemStack[]> recipe;

    @Shadow
    private List<ItemStack> output;

    /**
     * Finds the optimal ingredients for a given recipe.
     *
     * @param recipe   The crafting recipe.
     * @param strategy The strategy for comparing ItemStacks.
     * @return A map of optimal ingredients and their counts.
     */
    @Unique
    private static <T extends ItemStack> Object2IntMap<T> aE2ExtendedCraftingTable$findOptimalIngredients(List<Set<T>> recipe,
                                                                                                          Hash.Strategy<T> strategy) {
        Object2IntMap<T> bestIngredients = new Object2IntOpenCustomHashMap<>(strategy);
        var bestIngredientCount = new int[]{ Integer.MAX_VALUE };
        aE2ExtendedCraftingTable$backtrack(new Object2IntOpenCustomHashMap<>(strategy), recipe, 0, bestIngredients, bestIngredientCount);
        return bestIngredients;
    }

    /**
     * Backtracks to find the optimal set of ingredients.
     *
     * @param currentIngredients  The current set of ingredients.
     * @param recipe              The crafting recipe.
     * @param recipeIndex         The current index in the recipe.
     * @param bestIngredients     The best set of ingredients found so far.
     * @param bestIngredientCount The smallest number of unique ingredients found so far.
     */
    @Unique
    private static <T> void aE2ExtendedCraftingTable$backtrack(Object2IntMap<T> currentIngredients, List<Set<T>> recipe,
                                                               int recipeIndex, Object2IntMap<T> bestIngredients, int[] bestIngredientCount) {
        if (recipeIndex == recipe.size()) {
            var currentIngredientCount = currentIngredients.size();
            if (currentIngredientCount < bestIngredientCount[0]) {
                bestIngredientCount[0] = currentIngredientCount;
                bestIngredients.clear();
                bestIngredients.putAll(currentIngredients);
            }
            return;
        }

        for (var ingredient : recipe.get(recipeIndex)) {
            currentIngredients.put(ingredient, currentIngredients.getOrDefault(ingredient, 0) + 1);
            aE2ExtendedCraftingTable$backtrack(currentIngredients, recipe, recipeIndex + 1, bestIngredients, bestIngredientCount);
            var count = currentIngredients.getInt(ingredient) - 1;
            if (count == 0) {
                currentIngredients.removeInt(ingredient);
            } else {
                currentIngredients.put(ingredient, count);
            }
        }
    }

    @Inject(method = "<init>(Lio/netty/buffer/ByteBuf;)V", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NBTTagCompound;getKeySet()Ljava/util/Set;",
            remap = true
    ))
    private void ctor(ByteBuf stream, CallbackInfo ci, @Local NBTTagCompound comp) {
        if (comp == null) return;
        var tag = comp.getCompoundTag("ae2exttable");
        this.ae2exttable$craft = tag.getBoolean("craft");
        this.ae2exttable$autoStart = tag.getBoolean("autoStart");
    }


    @Optional.Method(modid="nae2")
    @Inject(method = "serverPacketData", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Container;onCraftMatrixChanged(Lnet/minecraft/inventory/IInventory;)V",
            remap = true
    ))
    public void serverPacketData(INetworkInfo manager, AppEngPacket packet, EntityPlayer player, CallbackInfo ci,
                                 @Local(name = "craftMatrix") IItemHandler craftMatrix,
                                 @Local ICraftingGrid crafting,
                                 @Local IGrid grid,
                                 @Local IContainerCraftingPacket cct) {

        // Not asked. No do.
        if (!this.ae2exttable$craft) return;

        if (!((player.openContainer instanceof ContainerSharedWirelessTerminals) | (player.openContainer instanceof ContainerMEMonitorableTwo))) {
            return;
        }

        final var container = (ContainerMEMonitorableTwo) player.openContainer;

        // Not a valid context. No do.
        final var context = ((AEBaseContainer) cct).getOpenContext();
        if (context == null) return;

        // Not a crafting matchingRecipe. No do.
        if (this.output.size() != 1) return;

        // Huh? No output?
        var outputIS = this.output.get(0);
        if (outputIS == null) return;
        if (outputIS.isEmpty()) return;

        var outputAIS = AEItemStack.fromItemStack(outputIS);

        // Invalid output stack. No do.
        if (outputAIS == null) return;

        // Verify the validity of the recipe. We can be sent anything after all!
        var testRecipe = new InventoryCrafting(container, container.getWidth(), container.getHeight());
        for (var i = 0; i < craftMatrix.getSlots(); i++) {
            final var slotID = i;
            Arrays.stream(this.recipe.get(i)).findFirst().ifPresent((itemStack ->
                    testRecipe.setInventorySlotContents(slotID, itemStack)));
        }
        final var recipes = TableRecipeManager.getInstance().getRecipes();
        IRecipe matchedRecipe = null;
        for(Object recipe: recipes) {
            if (((IRecipe) recipe).matches(testRecipe, player.world)) {
                matchedRecipe = (IRecipe) recipe;
                break;
            }
        }
        if (matchedRecipe == null) {return;}
        if (!ItemStack.areItemStacksEqual(matchedRecipe.getRecipeOutput(), outputIS)) return;

        // Collect all items from the matchingRecipe and compare.
        // Fill in whatever we're missing from the grid that's present in the matchingRecipe.
        var strategy = ItemStackHashStrategy.comparingAllButCount();

        var missingItems = new ArrayList<Set<ItemStack>>();
        for (var i = 0; i < craftMatrix.getSlots(); i++) {
            var stackInSlot = craftMatrix.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                var recipe = new ObjectOpenCustomHashSet<>(strategy);
                var stacksInRecipeSlot = this.recipe.get(i);
                for (var stackInRecipeSlot : stacksInRecipeSlot) {
                    if (!stackInRecipeSlot.isEmpty() &&
                            !crafting.getCraftingFor(AEItemStack.fromItemStack(stackInRecipeSlot),
                                    null,
                                    0,
                                    null).isEmpty()) {
                        recipe.add(stackInRecipeSlot);
                    }
                }

                if (!recipe.isEmpty()) missingItems.add(recipe);
            }
        }

        // Nothing is missing? No do.
        if (missingItems.isEmpty()) return;

        // Find the optimal way of satisfying the matchingRecipe, minimizing the number of different
        // items while maximizing the stack size.
        // Upcast to AEItemStacks. Nothing should be null here, but who knows what AE2 may do.
        var optimal = aE2ExtendedCraftingTable$findOptimalIngredients(missingItems, strategy)
                .entrySet().stream()
                .map(entry -> Objects.requireNonNull(AEItemStack.fromItemStack(entry.getKey()))
                        .setStackSize(entry.getValue()))
                .collect(Collectors.toList());

        // Create a Virtual Pattern, despite AE2 telling us not to.
        var pattern = new VirtualPatternDetails(optimal,
                this.output.stream().map(AEItemStack::fromItemStack).collect(Collectors.toList()));

        // Try firing the crafting job.
        Future<ICraftingJob> futureJob = null;
        try {
            final ICraftingGrid cg = grid.getCache(ICraftingGrid.class);
            futureJob = ((IExtendedCraftingGridCache) cg).beginCraftingJobFromDetails(player.world, grid,
                    cct.getActionSource(), outputAIS, pattern, null);

            final var te = context.getTile();
            if (te != null) {
                Platform.openGUI(player, te, context.getSide(), GuiBridge.GUI_CRAFTING_CONFIRM);
            } else {
                if (player.openContainer instanceof IInventorySlotAware i) {
                    Platform.openGUI(player, i.getInventorySlot(), GuiBridge.GUI_CRAFTING_CONFIRM,
                            i.isBaubleSlot());
                }
            }

            if (player.openContainer instanceof ContainerCraftConfirm ccc) {
                ccc.setAutoStart(this.ae2exttable$autoStart);
                ccc.setJob(futureJob);
            } else {
                futureJob.cancel(true);
            }
        } catch (final Throwable e) {
            if (futureJob != null) {
                futureJob.cancel(true);
            }
            AELog.debug(e);
        }
    }
}
