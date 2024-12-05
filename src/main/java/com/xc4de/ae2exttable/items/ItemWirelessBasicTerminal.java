package com.xc4de.ae2exttable.items;

import appeng.api.AEApi;
import appeng.api.features.IWirelessTermHandler;
import appeng.core.sync.GuiWrapper;
import appeng.items.tools.powered.ToolWirelessTerminal;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import com.xc4de.ae2exttable.interfaces.IExtendedGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ItemWirelessBasicTerminal extends ToolWirelessTerminal {
    public static AE2ExtendedGUIs GUI = AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL;
    public ItemWirelessBasicTerminal() {
        super();
        this.setRegistryName("wireless_basic_crafting_terminal");
        this.setTranslationKey(Tags.MODID+"."+"wireless_basic_crafting_terminal");
        this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);

        // This is registered so that "canHandle" and "getGuiHandler" are actually called
        AEApi.instance().registries().wireless().registerWirelessHandler(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World w,
                                                    EntityPlayer player,
                                                    EnumHand hand) {
        return null;
    }
}
