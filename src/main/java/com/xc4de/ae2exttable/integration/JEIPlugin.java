package com.xc4de.ae2exttable.integration;

import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import com.xc4de.ae2exttable.client.container.terminals.ContainerAdvancedCraftingTerminal;
import com.xc4de.ae2exttable.client.container.terminals.ContainerBasicCraftingTerminal;
import com.xc4de.ae2exttable.client.container.terminals.ContainerEliteCraftingTerminal;
import com.xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com.xc4de.ae2exttable.client.container.wireless.ContainerAdvancedWirelessTerminal;
import com.xc4de.ae2exttable.client.container.wireless.ContainerBasicWirelessTerminal;
import com.xc4de.ae2exttable.client.container.wireless.ContainerEliteWirelessTerminal;
import com.xc4de.ae2exttable.client.container.wireless.ContainerUltimateWirelessTerminal;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import mezz.jei.api.IModPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry transfer = registry.getRecipeTransferRegistry();
        try {
            Constructor constructor = new RecipeTransferHandlerWrapper().constructor;
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerBasicCraftingTerminal.class), VanillaRecipeCategoryUid.CRAFTING);
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerBasicCraftingTerminal.class), BasicTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(
                ContainerBasicWirelessTerminal.class), VanillaRecipeCategoryUid.CRAFTING);
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerBasicWirelessTerminal.class), BasicTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerAdvancedCraftingTerminal.class), AdvancedTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(
                ContainerAdvancedWirelessTerminal.class), AdvancedTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerEliteCraftingTerminal.class), EliteTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(
                ContainerEliteWirelessTerminal.class), EliteTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(ContainerUltimateCraftingTerminal.class), UltimateTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler((IRecipeTransferHandler<?>) constructor.newInstance(
                ContainerUltimateWirelessTerminal.class), UltimateTableCategory.UID);//, 1, 9, 10, 36);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
