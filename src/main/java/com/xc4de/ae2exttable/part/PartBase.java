package com.xc4de.ae2exttable.part;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridCache;
import appeng.api.networking.IGridHost;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.util.Platform;
import com.xc4de.ae2exttable.integration.appeng.grid.ExtGridBlock;
import com.xc4de.ae2exttable.integration.appeng.grid.IExtGridHost;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.items.IItemHandler;

import appeng.api.AEApi;
import appeng.api.config.Upgrades;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.implementations.IUpgradeableHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.parts.*;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;

import io.netty.buffer.ByteBuf;

public abstract class PartBase implements IPart, IExtGridHost, IUpgradeableHost, IActionHost, IPowerChannelState {

    protected ExtGridBlock gridBlock;
    protected IGridNode gridNode;
    protected IPartHost host;
    protected TileEntity hostTile;
    protected EntityPlayer owner;
    protected Item item;
    public AEPartLocation side;

    protected boolean isPowered;
    protected boolean isActive;

    public PartBase(Item item) {
        this.item = item;
    }

    public boolean canWork() {
        return false;
    }

    public double getIdlePowerUsage() {
        return 0;
    }

    @Override
    public DimensionalCoord getLocation() {
        if (this.hostTile != null && this.hostTile.hasWorld() && this.hostTile.getWorld().provider != null)
            return new DimensionalCoord(this.hostTile.getWorld(), this.hostTile.getPos());
        return null;
    }

    @Override
    public ItemStack getItemStack(PartItemStack type) {
        // TODO: save nbt to it
        return new ItemStack(this.item);
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
    public void writeToNBT(NBTTagCompound nbt) {
        if (gridNode != null)
            this.gridNode.saveToNBT("part", nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (gridNode != null)
            this.gridNode.loadFromNBT("part", nbt);
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
    public void writeToStream(ByteBuf buf) {
        buf.writeBoolean(this.isActive());
        buf.writeBoolean(this.isPowered());
    }

    @Override
    public boolean readFromStream(ByteBuf buf) {
        this.isActive = buf.readBoolean();
        this.isPowered = buf.readBoolean();
        return true;
    }

    @Override
    public IGridNode getGridNode() {
        return this.gridNode;
    }

    @Override
    public void onEntityCollision(Entity entity) {

    }

    @Override
    public void removeFromWorld() {
        if (this.gridNode != null)
            this.gridNode.destroy();
    }

    @Override
    public void addToWorld() {
        if (!Platform.isServer())
            return;

        this.gridBlock = new ExtGridBlock(this);
        this.gridNode = AEApi.instance().grid().createGridNode(this.gridBlock);
        if (this.owner != null) {
            this.gridNode.setPlayerID(AEApi.instance().registries().players().getID(this.owner));
        }
        this.gridNode.updateState();
        //this.setPower(null); TODO
        BlockPos pos = this.gridBlock.getLocation().getPos();
        this.onNeighborChanged(null, pos, pos.offset(this.side.getFacing()));
    }

    @Override
    public IGridNode getExternalFacingNode() {
        return null;
    }

    @Override
    public void setPartHostInfo(AEPartLocation side, IPartHost host, TileEntity te) {
        this.side = side;
        this.host = host;
        this.hostTile = te;
        // TODO this.setPower(null);
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
        //ItemHandlerUtil.getInventoryAsList(this.getInventoryByName(""), list);
        return;
    }

    @Override
    public float getCableConnectionLength(AECableType aeCableType) {
        return 3;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, Random random) {

    }

    @Override
    public void onPlacement(EntityPlayer player, EnumHand hand, ItemStack stack, AEPartLocation side) {
        this.owner = player;
    }

    @Override
    public boolean canBePlacedOn(BusSupport busSupport) {
        return busSupport == BusSupport.CABLE;
    }

    @Override
    public void getBoxes(IPartCollisionHelper box) {
        // Should be overridden and not used
        box.addBox(4, 4, 12, 12, 12, 14);
        box.addBox(6, 6, 11, 10, 10, 12);
    }

    @Nullable
    @Override
    public IGridNode getGridNode(@Nonnull AEPartLocation dir) {
        return this.gridNode;
    }

    @Nonnull
    @Override
    public AECableType getCableConnectionType(@Nonnull AEPartLocation dir) {
        return AECableType.GLASS;
    }

    @Override
    public void securityBreak() {
        // TODO
    }

    @Override
    public boolean isPowered() {
        return this.isPowered;
    }

    @Override
    public boolean isActive() {
        return this.gridNode != null ? this.gridNode.isActive() : this.isActive;
    }

    @Nonnull
    @Override
    public IGridNode getActionableNode() {
        return this.gridNode;
    }

    @MENetworkEventSubscribe
    public void updatePowerStatus(MENetworkPowerStatusChange event) {
        try {
            IGridNode node = this.getGridNode(AEPartLocation.UP);
            if (node == null) {
                throw new GridAccessException();
            }
            IGrid grid = node.getGrid();
            IEnergyGrid eGrid = grid.getCache(IEnergyGrid.class);
            this.isPowered = eGrid.isNetworkPowered();
        } catch (GridAccessException e) {
            // should ignore?
            this.isPowered = false;
        }
    }

    @Override
    public void gridChanged() {

    }

    @Override
    public int getInstalledUpgrades(Upgrades u) {
        return 0;
    }

    @Override
    public TileEntity getTile() {
        return this.hostTile;
    }

    @Override
    public IItemHandler getInventoryByName(String name) {
        return null;
    }
}