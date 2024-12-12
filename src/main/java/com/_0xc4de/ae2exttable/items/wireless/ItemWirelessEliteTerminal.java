package com._0xc4de.ae2exttable.items.wireless;

import appeng.api.AEApi;
import appeng.items.tools.powered.ToolWirelessTerminal;
import com._0xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com._0xc4de.ae2exttable.Tags;
import com._0xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com._0xc4de.ae2exttable.client.gui.PartGuiHandler;
import com._0xc4de.ae2exttable.interfaces.ITerminalGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWirelessEliteTerminal extends ToolWirelessTerminal implements
    ITerminalGui {
  public static AE2ExtendedGUIs GUI = AE2ExtendedGUIs.WIRELESS_ELITE_CRAFTING_TERMINAL;

  public ItemWirelessEliteTerminal() {
    super();
    this.setRegistryName("wireless_elite_crafting_terminal");
    this.setTranslationKey(
        Tags.MODID + "." + "wireless_elite_crafting_terminal");
    this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);

    // This is registered so that "canHandle" and "getGuiHandler" are actually called
    AEApi.instance().registries().wireless().registerWirelessHandler(this);
  }

  @Override
  public boolean canHandle(ItemStack is) {
    return is.getItem() instanceof ItemWirelessEliteTerminal;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World w,
                                                  EntityPlayer player,
                                                  EnumHand hand) {
    PartGuiHandler.openWirelessTerminalGui(player.getHeldItem(hand),
        hand == EnumHand.MAIN_HAND ? player.inventory.currentItem : 40,
        false,
        w, player, AE2ExtendedGUIs.WIRELESS_ELITE_CRAFTING_TERMINAL);
    return ActionResult.newResult(EnumActionResult.SUCCESS,
        player.getHeldItem(hand));
  }

  @Override
  public AE2ExtendedGUIs getGuiType() {
    return GUI;
  }
}
