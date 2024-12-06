package com.xc4de.ae2exttable.items;

import appeng.api.AEApi;
import appeng.items.tools.powered.ToolWirelessTerminal;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWirelessBasicTerminal extends ToolWirelessTerminal {
  public static AE2ExtendedGUIs GUI = AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL;

  public ItemWirelessBasicTerminal() {
    super();
    this.setRegistryName("wireless_basic_crafting_terminal");
    this.setTranslationKey(
        Tags.MODID + "." + "wireless_basic_crafting_terminal");
    this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);

    // This is registered so that "canHandle" and "getGuiHandler" are actually called
    AEApi.instance().registries().wireless().registerWirelessHandler(this);
  }

  @Override
  public boolean canHandle(ItemStack is) {
    return is.getItem() instanceof ItemWirelessBasicTerminal;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World w,
                                                  EntityPlayer player,
                                                  EnumHand hand) {
    PartGuiHandler.openWirelessTerminalGui(player.getHeldItem(hand),
        hand == EnumHand.MAIN_HAND ? player.inventory.currentItem : 40,
        false,
        w, player, AE2ExtendedGUIs.WIRELESS_BASIC_TERMINAL);
    return ActionResult.newResult(EnumActionResult.SUCCESS,
        player.getHeldItem(hand));
  }
}
