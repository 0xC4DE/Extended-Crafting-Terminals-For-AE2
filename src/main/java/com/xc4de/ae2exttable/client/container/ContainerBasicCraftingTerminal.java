package com.xc4de.ae2exttable.client.container;

import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IBaseMonitor;
import appeng.api.storage.IMEMonitorHandlerReceiver;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.container.ContainerNull;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotCraftingTerm;
import appeng.helpers.IContainerCraftingPacket;
import appeng.parts.reporting.PartCraftingTerminal;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import com.xc4de.ae2exttable.part.PartBasicCraftingTerminal;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;


public class ContainerBasicCraftingTerminal extends ContainerMEMonitorableTwo implements IAEAppEngInventory, IContainerCraftingPacket {

    private final PartBasicCraftingTerminal ct;
    private final AppEngInternalInventory output = new AppEngInternalInventory(this, 1);
    private final SlotCraftingMatrix[] craftingSlots = new SlotCraftingMatrix[9];
    private final SlotCraftingTerm outputSlot;
    private IRecipe currentRecipe;

    public ContainerBasicCraftingTerminal(final InventoryPlayer ip, final ITerminalHost monitorable) {
        super(ip, monitorable, false);
        this.ct = (PartBasicCraftingTerminal) monitorable;

        final IItemHandler crafting = this.ct.getInventoryByName("craftingGrid");

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                this.addSlotToContainer(this.craftingSlots[x + y * 3] = new SlotCraftingMatrix(this, crafting, x + y * 3, 37 + x * 18, -72 + y * 18));
            }
        }

        this.addSlotToContainer(this.outputSlot = new SlotCraftingTerm(this.getPlayerInv().player, this.getActionSource(), this
                .getPowerSource(), monitorable, crafting, crafting, this.output, 131, -72 + 18, this));

        this.bindPlayerInventory(ip, 0, 0);

        this.onCraftMatrixChanged(new WrapperInvItemHandler(crafting));
    }

    /**
     * Callback for when the crafting matrix is changed.
     */

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        final ContainerNull cn = new ContainerNull();
        final InventoryCrafting ic = new InventoryCrafting(cn, 3, 3);

        for (int x = 0; x < 9; x++) {
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

    @Override
    public void saveChanges() {

    }

    @Override
    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack) {

    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (name.equals("player")) {
            return new PlayerInvWrapper(this.getInventoryPlayer());
        }
        return this.ct.getInventoryByName(name);
    }

    @Override
    public boolean useRealItems() {
        return true;
    }

    public IRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
}