package com.xc4de.ae2exttable.items;

import appeng.api.AEApi;
import appeng.api.features.IWirelessTermHandler;
import appeng.core.sync.GuiWrapper;
import appeng.items.tools.powered.ToolWirelessTerminal;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ItemWirelessBasicTerminal extends ToolWirelessTerminal implements IWirelessTermHandler {
    public ItemWirelessBasicTerminal() {
        super();
        this.setRegistryName("wireless_basic_crafting_terminal");
        this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);
        AEApi.instance().registries().wireless().registerWirelessHandler(this);
    }

    @Override
    public boolean canHandle(ItemStack is) {
        return true;
    }

    @Override
    public IGuiHandler getGuiHandler(ItemStack is) {
    }
}
