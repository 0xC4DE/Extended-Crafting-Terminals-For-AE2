/**
 * This file has been DE-CURSED, however it now includes 100% more plagiarism, as most of it is shamelessly copied
 * from the original AE2-UEL code (which is shamelessly copied from AE2)
 *
 * Original Author: @shartte (AE2 Contributor)
 * Modified by: @0xC4DE to do typing for AE2 Extended Crafting Table(Terminals)
 *
 * Again I would like to stress, I DIDNT make this file, I copied it, made minimal changes, and it worked for my mod
 */
package com._0xc4de.ae2exttable.integration;


import appeng.container.implementations.ContainerPatternEncoder;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketJEIRecipe;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.util.Platform;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerBasicCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerEliteCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.transfer.RecipeTransferErrorInternal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

import static appeng.helpers.ItemStackHelper.stackToNBT;


class RecipeTransferHandler<T extends Container> implements IRecipeTransferHandler<T> {

    private final Class<T> containerClass;

    RecipeTransferHandler(Class<T> containerClass) {
        this.containerClass = containerClass;
    }

    @Override
    public Class<T> getContainerClass() {
        return this.containerClass;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(@Nonnull T container, IRecipeLayout recipeLayout, @Nonnull EntityPlayer player, boolean maxTransfer, boolean doTransfer) {
        final String recipeType = recipeLayout.getRecipeCategory().getUid();

        if (recipeType.equals(VanillaRecipeCategoryUid.INFORMATION) || recipeType.equals(VanillaRecipeCategoryUid.FUEL)) {
            return RecipeTransferErrorInternal.INSTANCE;
        }

        if (!doTransfer) {
            // Slightly easier way for me to detect when these are
            final String[] arrTableUIDs = new String[] {VanillaRecipeCategoryUid.CRAFTING, BasicTableCategory.UID, AdvancedTableCategory.UID, EliteTableCategory.UID, UltimateTableCategory.UID };
            final Set<String> tableUIDs = new HashSet<String>(Arrays.asList(arrTableUIDs));
            if ((container instanceof ContainerMEMonitorableTwo) && tableUIDs.contains(recipeType)) {
                JEIMissingItem error = new JEIMissingItem(container, recipeLayout);
                if (error.errored())
                    return error;
            }
            return null;
        }

        Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();

        final NBTTagCompound recipe = new NBTTagCompound();
        final NBTTagList outputs = new NBTTagList();

        // pre-fill each slot with air, just to pad.
        for (final Slot slot : container.inventorySlots) {
            NBTTagCompound tag = stackToNBT(ItemStack.EMPTY);
            NBTTagList tags = new NBTTagList();
            tags.appendTag(tag);
            recipe.setTag("#" + slot.slotNumber, tags);
        }

        int slotIndex = 0;
        for (Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> ingredientEntry : ingredients.entrySet()) {
            IGuiIngredient<ItemStack> ingredient = ingredientEntry.getValue();
            if (!ingredient.isInput()) {
                ItemStack output = ingredient.getDisplayedIngredient();
                if (output != null) {
                    final NBTTagCompound tag = stackToNBT(output);
                    outputs.appendTag(tag);
                }
                continue;
            }

            // For every slot in current container
            for (final Slot slot : container.inventorySlots) {

                // If the slot happens to be a crafting input slot
                if (slot instanceof SlotCraftingMatrix || slot instanceof SlotFakeCraftingMatrix) {

                    // If the slot index matches the current slotIndex
                    if (slot.getSlotIndex() == slotIndex) {
                        // This means we're in the right place
                        final NBTTagList new_tags = new NBTTagList();
                        final List<ItemStack> list = new ArrayList<>();
                        final ItemStack displayed = ingredient.getDisplayedIngredient();

                        // prefer currently displayed item
                        if (displayed != null && !displayed.isEmpty()) {
                            list.add(displayed);
                        }

                        for (final ItemStack is : list) {
                            final NBTTagCompound _tag = stackToNBT(is);
                            new_tags.appendTag(_tag);
                        }

                        // Item belongs in this slot's index
                        int idx = getIndex(container, recipeType, slotIndex);
                        recipe.removeTag("#" + idx);
                        recipe.setTag("#" + idx, new_tags);
                        break;
                    }
                }
            }

            slotIndex++;
        }

        recipe.setTag("outputs", outputs);

        try {
            NetworkHandler.instance().sendToServer(new PacketJEIRecipe(recipe));
        } catch (IOException e) {
            AELog.debug(e);
        }

        return null;
    }


    /*
     Modifying slot index symbolically lets me control the recipe transfer depending on what container is being accessed

     Correct mappings are as follows:
     Ultimate: Ultimate, Elite, Advanced, Basic
     Elite: Elite, Advanced, Basic
     Advanced: Advanced, Basic
     Basic: Basic, Vanilla Recipes

     For some reason the bigger tables cannot accept vanilla recipes.

     This means if I have an ordered mapping of each container, in increasing order (Basic:0, Advanced:1, ...) then each
     respective Recipe Layout can be calculated from current backwards, basically List[:index] inclusive, using known
     Layouts of each, the "slotIndex" can be adjusted to treat higher level slots as
     */
    private int getIndex(@Nonnull T container, String recipeType, int currentIndex) {
        // I know this pattern is bad, I just don't know how to do it better
        int ultimateSize = 9;
        int eliteSize = 7;
        int advancedSize = 5;
        int basicSize = 3;
        if (container instanceof ContainerUltimateCraftingTerminal) {

            if (recipeType.equals(EliteTableCategory.UID)) {
                int size = ((ultimateSize-eliteSize)/2);
                // These matrixes are always square, so rowSize=colSize => size
                return ((currentIndex/eliteSize + size) * ultimateSize) + ((currentIndex % eliteSize) + size);
            };
            if (recipeType.equals(AdvancedTableCategory.UID)) {
                int size = ((ultimateSize-eliteSize)/2);
                return (size + currentIndex) * 5 + (size + currentIndex);
            };
            if (recipeType.equals(BasicTableCategory.UID)) {

            };
        } else if (container instanceof ContainerEliteCraftingTerminal) {
            if (recipeType.equals(AdvancedTableCategory.UID)) {

            };
            if (recipeType.equals(BasicTableCategory.UID)) {

            };
        } else if (container instanceof ContainerAdvancedCraftingTerminal) {
            if (recipeType.equals(BasicTableCategory.UID)) {

            };
        }

        return currentIndex;
    }
}
