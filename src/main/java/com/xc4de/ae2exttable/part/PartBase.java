package com.xc4de.ae2exttable.part;

import appeng.api.config.Upgrades;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.implementations.IUpgradeableHost;
import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.IConfigManager;
import appeng.util.helpers.ItemHandlerUtil;
import com.blakebr0.cucumber.item.ItemBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class PartBase implements IPart, IGridHost, IUpgradeableHost, IActionHost, IPowerChannelState {

    public PartBase(ItemBase item) {

    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public int getInstalledUpgrades(Upgrades upgrades) {
        return 0;
    }

    @Override
    public TileEntity getTile() {
        return null;
    }

    @Override
    public IItemHandler getInventoryByName(String s) {
        return null;
    }

    @Override
    public @Nullable IGridNode getGridNode(@NotNull AEPartLocation aePartLocation) {
        return null;
    }

    @Override
    public @NotNull AECableType getCableConnectionType(@NotNull AEPartLocation aePartLocation) {
        return null;
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public @NotNull IGridNode getActionableNode() {
        return null;
    }

    @Override
    public ItemStack getItemStack(PartItemStack partItemStack) {
        return null;
    }

    @Override
    public boolean requireDynamicRender() {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean canConnectRedstone() {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public boolean isLadder(EntityLivingBase entityLivingBase) {
        return false;
    }

    @Override
    public void onNeighborChanged(IBlockAccess iBlockAccess, BlockPos blockPos, BlockPos blockPos1) {

    }

    @Override
    public int isProvidingStrongPower() {
        return 0;
    }

    @Override
    public int isProvidingWeakPower() {
        return 0;
    }

    @Override
    public void writeToStream(ByteBuf byteBuf) throws IOException {

    }

    @Override
    public boolean readFromStream(ByteBuf byteBuf) throws IOException {
        return false;
    }

    @Override
    public IGridNode getGridNode() {
        return null;
    }

    @Override
    public void onEntityCollision(Entity entity) {

    }

    @Override
    public void removeFromWorld() {

    }

    @Override
    public void addToWorld() {
    }

    @Override
    public IGridNode getExternalFacingNode() {
        return null;
    }

    @Override
    public void setPartHostInfo(AEPartLocation aePartLocation, IPartHost iPartHost, TileEntity tileEntity) {
        //this.side = aePartLocation;
        //this.host = iPartHost;
        //this.hostTile = tileEntity;
    }

    @Override
    public boolean onActivate(EntityPlayer entityPlayer, EnumHand enumHand, Vec3d vec3d) {
        return false;
    }

    @Override
    public boolean onShiftActivate(EntityPlayer entityPlayer, EnumHand enumHand, Vec3d vec3d) {
        return false;
    }

    @Override
    public void getDrops(List<ItemStack> list, boolean b) {
    }

    @Override
    public float getCableConnectionLength(AECableType aeCableType) {
        return 0;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, Random random) {

    }

    @Override
    public void onPlacement(EntityPlayer entityPlayer, EnumHand enumHand, ItemStack itemStack, AEPartLocation aePartLocation) {

    }

    @Override
    public boolean canBePlacedOn(BusSupport busSupport) {
        return false;
    }

    @Override
    public void getBoxes(IPartCollisionHelper iPartCollisionHelper) {

    }

    @Override
    public IConfigManager getConfigManager() {
        return null;
    }
}
