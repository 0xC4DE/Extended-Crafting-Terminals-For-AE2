package com.xc4de.ae2exttable.crafting;

import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class RecipeRegister {
    public static void init() {
        ArrayList<ItemStack> dirt = new ArrayList<ItemStack>();
        dirt.add(new ItemStack(Blocks.DIRT, 1, 0));
        TableRecipeManager.getInstance().addShapeless(0, new ItemStack(Items.DIAMOND, 1, 0), dirt.toArray());
    }
}
