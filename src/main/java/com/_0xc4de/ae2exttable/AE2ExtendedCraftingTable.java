package com._0xc4de.ae2exttable;

import com.blakebr0.cucumber.registry.ModRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.SidedProxy;

import com._0xc4de.ae2exttable.proxy.CommonProxy;

@Mod(modid = Tags.MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:appliedenergistics2;required:mixinbooter;required-after:extendedcrafting;")
public class AE2ExtendedCraftingTable {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODID);
    public static final ModRegistry REGISTRY = ModRegistry.create(Tags.MODID);
    public static final CreativeTabs EXTENDED_TABLE_TAB = new AE2ExtendedTableTab();

    @Mod.Instance(Tags.MODID)
    public static AE2ExtendedCraftingTable instance;

    @SidedProxy(clientSide="com._0xc4de.ae2exttable.proxy.ClientProxy", serverSide="com._0xc4de.ae2exttable.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("I am " + Tags.MODNAME + " + at version " + Tags.VERSION);
        proxy.preInit(event);
    }

    @EventHandler
    // load "Do your mod setup. Build whatever data structures you care about." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
