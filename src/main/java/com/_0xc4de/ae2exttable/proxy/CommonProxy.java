package com._0xc4de.ae2exttable.proxy;

import com._0xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com._0xc4de.ae2exttable.Tags;
import com._0xc4de.ae2exttable.client.ItemColors;
import com._0xc4de.ae2exttable.client.gui.PartGuiHandler;
import com._0xc4de.ae2exttable.items.ItemRegistry;
import com._0xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        //BlockRegistry.init();
        //TileRegistry.init();
        ItemRegistry.init();

        MinecraftForge.EVENT_BUS.register(AE2ExtendedCraftingTable.REGISTRY);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event) {
        //RecipeRegister.post();
        //NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new GuiHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new PartGuiHandler());

        // TEST RECIPE
        //TableRecipeManager.getInstance().addShaped(new ItemStack(Blocks.DIRT),
        //        "DDD", "ADA", "DDD", 'D', new ItemStack(Items.DIAMOND), 'A', new ItemStack(Items.AIR));

        if (FMLCommonHandler.instance().getSide().isClient())  {
            ItemColors.registerItemColors();
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        //RecipeRegister.init();
        ExtendedTerminalNetworkHandler.init(Tags.MODID);
    }
}
