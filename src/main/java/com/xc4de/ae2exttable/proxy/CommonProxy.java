package com.xc4de.ae2exttable.proxy;

import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.AE2ExtendedTableTab;
import com.xc4de.ae2exttable.blocks.BlockRegistry;
import com.xc4de.ae2exttable.client.gui.GuiHandler;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import com.xc4de.ae2exttable.crafting.RecipeRegister;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.tile.TileRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        BlockRegistry.init();
        TileRegistry.init();
        ItemRegistry.init();

        MinecraftForge.EVENT_BUS.register(AE2ExtendedCraftingTable.REGISTRY);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event) {
        //RecipeRegister.post();
        //NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new GuiHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new PartGuiHandler());

    }

    public void postInit(FMLPostInitializationEvent event) {
        RecipeRegister.init();
    }
}
