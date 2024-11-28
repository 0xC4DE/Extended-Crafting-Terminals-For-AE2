package com.xc4de.ae2exttable.blocks;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.util.Utils;
import com.xc4de.ae2exttable.client.gui.GuiHandler;
import com.xc4de.ae2exttable.AE2ExtendedCraftingTable;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.tile.TileCustomExtendedTable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import java.util.List;

public class CustomExtendedTable extends BlockBase implements ITileEntityProvider {
    public CustomExtendedTable() {
        super(Tags.MODID+".custom_extended_table", Material.WOOD, SoundType.ANVIL, 5.0f, 10.0f);
        this.setCreativeTab(AE2ExtendedCraftingTable.EXTENDED_TABLE_TAB);
    }

    // Needed?
    public void initModels() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
                0, new ModelResourceLocation(Tags.MODID+":custom_extended_table"));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileCustomExtendedTable();
        //return new TileCustomExtendedTable();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileCustomExtendedTable) {
                player.openGui(AE2ExtendedCraftingTable.instance, GuiHandler.CUSTOM_EXTENDED_TABLE, world, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileCustomExtendedTable tile = (TileCustomExtendedTable) world.getTileEntity(pos);
        if (tile != null) {
            NonNullList<ItemStack> matrix = tile.getMatrix();
            for (int i = 0; i < matrix.size(); i++) {
                ItemStack stack = matrix.get(i);
                spawnAsEntity(world, pos, stack);
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return facing == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(Utils.localize("tooltip."+ Tags.MODID + ".custom_extended_table", 1));
    }
}
