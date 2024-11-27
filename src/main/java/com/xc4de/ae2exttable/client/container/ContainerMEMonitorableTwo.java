package com.xc4de.ae2exttable.client.container;

import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com.xc4de.ae2exttable.client.gui.terminals.GuiMEMonitorableTwo;
import com.xc4de.ae2exttable.part.PartBasicCraftingTerminal;
import com.xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.List;

public class ContainerMEMonitorableTwo extends ContainerMEMonitorable implements IAEAppEngInventory, IContainerCraftingPacket {

    private IRecipe currentRecipe;
    protected final int slotWidth;
    protected final int slotHeight;
    protected final SlotCraftingMatrix[] craftingSlots;
    private final PartSharedCraftingTerminal ct;
    private final AppEngInternalInventory output;
    private final SlotCraftingTerm outputSlot;
    private final ExtendedCraftingGUIConstants guiConst;

    public ContainerMEMonitorableTwo(final InventoryPlayer ip, final ITerminalHost monitorable, final boolean isWireless,
                                     final int slotWidth, final int slotHeight, ExtendedCraftingGUIConstants guiConst) {
        super(ip, monitorable, isWireless);
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.output =  new AppEngInternalInventory(this, 1);
        this.craftingSlots = new SlotCraftingMatrix[this.slotWidth * this.slotHeight];
        this.ct = (PartSharedCraftingTerminal) monitorable;
        this.guiConst = guiConst;

        final IItemHandler crafting = this.ct.getInventoryByName("craftingGrid");

        for (int y = 0; y < this.slotHeight; y++) {
            for (int x = 0; x < this.slotWidth; x++) {
                this.addSlotToContainer(this.craftingSlots[x + y * this.slotHeight] = new SlotCraftingMatrix(this, crafting, x + y * this.slotHeight, 37 + x * 18, -72 + y * 18));
            }
        }

        this.addSlotToContainer(this.outputSlot = new SlotCraftingTerm(this.getPlayerInv().player, this.getActionSource(), this
                .getPowerSource(), monitorable, crafting, crafting, this.output, 131, -72 + 18, this));

        // Player Inventory for Tables, offsetX is the distance from the left edge of the GUI to the left edge of the player inventory
        // where the players inventory left edge is the first "inner" pixel of the leftmost slot minus 9 pixels (because why not)
        // some of my GUIs shift this. so. you know. it's a thing.
        this.bindPlayerInventory(ip, this.guiConst.inventoryOffset.x, this.guiConst.inventoryOffset.y);

        this.onCraftMatrixChanged(new WrapperInvItemHandler(crafting));
    }

    // TODO: This is where crafting matrix can be adapted to Extended Crafting
    public void onCraftMatrixChanged(IInventory inventory) {
        final ContainerNull cn = new ContainerNull();
        final InventoryCrafting ic = new InventoryCrafting(cn, this.slotWidth, this.slotHeight);

        for (int x = 0; x < this.slotWidth*this.slotHeight; x++) {
            ic.setInventorySlotContents(x, this.craftingSlots[x].getStack());
        }

        if (this.currentRecipe == null || !this.currentRecipe.matches(ic, this.getPlayerInv().player.world)) {
            this.currentRecipe = CraftingManager.findMatchingRecipe(ic, this.getPlayerInv().player.world);
        }

        if (this.currentRecipe == null) {
            this.outputSlot.putStack(ItemStack.EMPTY);
        } else {
            final ItemStack craftingResult = this.currentRecipe.getCraftingResult(ic);

            this.outputSlot.putStack(craftingResult);
        }
    }

    public void postUpdate(final List<IAEItemStack> list) {
        for (final IAEItemStack is : list) {
            this.items.add(is);
        }
        ((GuiMEMonitorableTwo) this.getGui()).postUpdate(list);
    }

    public void saveChanges() {

    }

    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack) {

    }

    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("player")) {
            return new PlayerInvWrapper(this.getInventoryPlayer());
        }
        return this.ct.getInventoryByName(name);
    }

    public boolean useRealItems() {
        return true;
    }

    public IRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
}