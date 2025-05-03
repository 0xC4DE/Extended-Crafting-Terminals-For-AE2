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


import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketJEIRecipe;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
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
import net.minecraft.client.gui.GuiScreen;
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

    RecipeTransferHandler(Class<?> containerClass) {
        this.containerClass = (Class<T>) containerClass;
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

        if (GuiScreen.isCtrlKeyDown()) {
            var compound = new NBTTagCompound();
            compound.setBoolean("craft", true);
            compound.setBoolean("autoStart", maxTransfer);
            recipe.setTag("ae2exttable", compound);
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

     Also, for some reason the bigger tables cannot accept vanilla recipes, partially my fault but it's in Blake's code too
     */
    private int getIndex(@Nonnull T container, String recipeType, int currentIndex) {
        final int ultimateSize = 9;
        final int eliteSize = 7;
        final int advancedSize = 5;
        final int basicSize = 3;
        final int thisSize = ((ContainerMEMonitorableTwo)container).getWidth();

        final boolean basicRecipe = recipeType.equals(BasicTableCategory.UID) | recipeType.equals(VanillaRecipeCategoryUid.CRAFTING);
        if (recipeType.equals(EliteTableCategory.UID)) {
            int size = ((thisSize-eliteSize)/2);
            // These matrices are always square, so rowSize=colSize => size
            return ((currentIndex/eliteSize + size) * thisSize) + ((currentIndex % eliteSize) + size);
        }
        
        if (recipeType.equals(AdvancedTableCategory.UID)) {
            int size = ((thisSize-advancedSize)/2);
            return ((currentIndex/advancedSize + size) * thisSize) + ((currentIndex % advancedSize) + size);
        }

        if (basicRecipe) {
            int size = ((thisSize-basicSize)/2);
            return ((currentIndex/basicSize + size) * thisSize) + ((currentIndex % basicSize) + size);
        }


        return currentIndex;
    }
}
