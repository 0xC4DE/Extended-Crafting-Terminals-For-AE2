package com._0xc4de.ae2exttable.items.terminals;

import appeng.api.AEApi;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartItem;
import com._0xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com._0xc4de.ae2exttable.Tags;
import com._0xc4de.ae2exttable.part.PartAdvancedCraftingTerminal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemAdvancedCraftingTerminal extends Item implements IPartItem {

    public ItemAdvancedCraftingTerminal(String name) {
        this.setRegistryName(name);
        this.setTranslationKey(Tags.MODID+"."+name);
        this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);
    }

    @Override
    public @Nullable IPart createPartFromItemStack(ItemStack stack) {
        return new PartAdvancedCraftingTerminal(stack);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return AEApi.instance().partHelper().placeBus(player.getHeldItem(hand), pos, facing, player, hand, world);
    }

    public void initModel() {
        AEApi.instance().registries().partModels().registerModels(PartAdvancedCraftingTerminal.resources);
    }
}
