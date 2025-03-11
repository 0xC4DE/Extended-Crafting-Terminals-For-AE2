package com._0xc4de.ae2exttable.integration;

import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.AdvancedTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.BasicTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.EliteTableCategory;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.UltimateTableCategory;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerAdvancedCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerBasicCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerEliteCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerAdvancedWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerBasicWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerEliteWirelessTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerUltimateWirelessTerminal;
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
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerBasicCraftingTerminal.class), VanillaRecipeCategoryUid.CRAFTING);
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerBasicCraftingTerminal.class), BasicTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerBasicWirelessTerminal.class), VanillaRecipeCategoryUid.CRAFTING);
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerBasicWirelessTerminal.class), BasicTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerAdvancedCraftingTerminal.class), AdvancedTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerAdvancedWirelessTerminal.class), AdvancedTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerEliteCraftingTerminal.class), EliteTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerEliteWirelessTerminal.class), EliteTableCategory.UID);//, 1, 9, 10, 36);

            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerUltimateCraftingTerminal.class), UltimateTableCategory.UID);//, 1, 9, 10, 36);
            transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerUltimateWirelessTerminal.class), UltimateTableCategory.UID);//, 1, 9, 10, 36);
    }
}
