package com.xc4de.ae2exttable.proxy;

import appeng.core.sync.GuiWrapper;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.ItemColors;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import com.xc4de.ae2exttable.crafting.RecipeRegister;
import com.xc4de.ae2exttable.items.ItemRegistry;
import com.xc4de.ae2exttable.network.ExtendedTerminalNetworkHandler;
import net.minecraft.util.ResourceLocation;
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

        GuiWrapper.Opener wrapper = new GuiBasicCraftingTerminalWrapper();
        GuiWrapper.INSTANCE.registerExternalGuiHandler(new ResourceLocation("ae2extgui","gui_basic_crafting_terminal"), wrapper);

        MinecraftForge.EVENT_BUS.register(AE2ExtendedCraftingTable.REGISTRY);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLInitializationEvent event) {
        //RecipeRegister.post();
        //NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new GuiHandler());
        NetworkRegistry.INSTANCE.registerGuiHandler(AE2ExtendedCraftingTable.instance, new PartGuiHandler());

        if (FMLCommonHandler.instance().getSide().isClient())  {
            ItemColors.registerItemColors();
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        RecipeRegister.init();
        ExtendedTerminalNetworkHandler.init(Tags.MODID);
    }
}
