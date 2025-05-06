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
import java.util.List;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry transfer = registry.getRecipeTransferRegistry();

        // Basic term gets vanilla too
        transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(ContainerBasicCraftingTerminal.class), VanillaRecipeCategoryUid.CRAFTING);

        String[] UIDs = new String[]{UltimateTableCategory.UID, EliteTableCategory.UID, AdvancedTableCategory.UID, BasicTableCategory.UID};
        Class<?>[] termMappings = new Class[]{ContainerUltimateCraftingTerminal.class, ContainerEliteCraftingTerminal.class, ContainerAdvancedCraftingTerminal.class, ContainerBasicCraftingTerminal.class};
        Class<?>[] wirelessTermMappings = new Class[]{ContainerUltimateWirelessTerminal.class, ContainerEliteWirelessTerminal.class, ContainerAdvancedWirelessTerminal.class,  ContainerBasicWirelessTerminal.class};

        for (int i=0; i < termMappings.length; i++){
            Class<?> term = termMappings[i];
            Class<?> wirelessTerm = wirelessTermMappings[i];

            for (int j = i; j < UIDs.length; j++){
                transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(term), UIDs[j]);
                transfer.addRecipeTransferHandler(new RecipeTransferHandler<>(wirelessTerm), UIDs[j]);
            }
        }
    }
}