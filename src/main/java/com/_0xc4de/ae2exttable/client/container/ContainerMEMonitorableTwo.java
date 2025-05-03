/**
 * Original file from AE2-UEL, modified to work with Extended Crafting.
 * Modified by: 0XC4DE
 */

package com._0xc4de.ae2exttable.client.container;

import appeng.api.implementations.guiobjects.IGuiItemObject;
import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.container.ContainerNull;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotCraftingTerm;
import appeng.container.slot.SlotRestrictedInput;
import appeng.helpers.IContainerCraftingPacket;
import appeng.parts.automation.StackUpgradeInventory;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.WrapperInvItemHandler;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com._0xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import com._0xc4de.ae2exttable.client.gui.GuiMEMonitorableTwo;
import com._0xc4de.ae2exttable.client.gui.WirelessTerminalGuiObjectTwo;
import com._0xc4de.ae2exttable.part.PartSharedCraftingTerminal;
import java.util.List;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public abstract class ContainerMEMonitorableTwo extends ContainerMEMonitorable
    implements IAEAppEngInventory, IContainerCraftingPacket {

  private IRecipe currentRecipe;
  protected final int slotWidth;
  protected final int slotHeight;
  private final SlotCraftingTerm outputSlot;
  final ExtendedCraftingGUIConstants guiConst;
  private PartSharedCraftingTerminal ct;
  protected WirelessTerminalGuiObjectTwo wt;

  protected final SlotCraftingMatrix[] craftingSlots;
  private final AppEngInternalInventory output;
  protected AppEngInternalInventory upgrades;

  private static final int craftingSlotWidth = 18;

  public ContainerMEMonitorableTwo(final InventoryPlayer ip,
                                   final ITerminalHost monitorable,
                                   final int slotWidth, final int slotHeight,
                                   final ExtendedCraftingGUIConstants guiConst) {
    this(ip, monitorable, (IGuiItemObject) null, slotWidth, slotHeight,
        guiConst);
  }

  public ContainerMEMonitorableTwo(final InventoryPlayer ip,
                                   final ITerminalHost monitorable,
                                   IGuiItemObject guiItemObject,
                                   final int slotWidth, final int slotHeight,
                                   ExtendedCraftingGUIConstants guiConst) {
    // False on bind Inventory because I will bind it manually specifically for my terminals later.
    super(ip, monitorable, guiItemObject, false);
    // I bind inventory manually because of custom offsets, passing false ensures it doesn't bind it again

    if (guiItemObject != null) {
      this.upgrades =
          new StackUpgradeInventory(guiItemObject.getItemStack(),
              this, 2);
    }

    // This check is here to debug easier
    for (int i = 0; i < 5; i++) {
      // Hacky. Removes old ViewCell slots.
      this.inventorySlots.remove(this.inventorySlots.size() - 1);
    }
    for (int y = 0; y < 5; ++y) {
      this.cellView[y] = new SlotRestrictedInput(
          SlotRestrictedInput.PlacableItemType.VIEW_CELL,
          ((IViewCellStorage) monitorable).getViewCellStorage(), y, 206,
          y * 18 + 8, this.getInventoryPlayer());
      this.cellView[y].setAllowEdit(this.canAccessViewCells);
      super.addSlotToContainer(this.cellView[y]);
    }

    this.slotWidth = slotWidth;
    this.slotHeight = slotHeight;
    this.output = new AppEngInternalInventory(this, 1);
    this.craftingSlots =
        new SlotCraftingMatrix[this.slotWidth * this.slotHeight];
    final IItemHandler crafting;
    if (monitorable instanceof PartSharedCraftingTerminal) {
      this.ct = (PartSharedCraftingTerminal) monitorable;
      crafting = this.ct.getInventoryByName("crafting");
    } else {
      this.wt = (WirelessTerminalGuiObjectTwo) monitorable;
      crafting = this.wt.getInventoryByName("crafting");
    }
    this.guiConst = guiConst;

    int craftingTableXOffset = guiConst.craftingGridOffset.x;
    int craftingTableYOffset = guiConst.craftingGridOffset.y;
    for (int y = 0; y < this.slotHeight; y++) {
      for (int x = 0; x < this.slotWidth; x++) {
        this.addSlotToContainer(this.craftingSlots[x + y * this.slotHeight] =
            new SlotCraftingMatrix(this, crafting, x + y * this.slotHeight,
                craftingTableXOffset + x * craftingSlotWidth,
                craftingTableYOffset + y * craftingSlotWidth));
      }
    }

    int outputX = guiConst.outputSlotOffset.x;
    int outputY = guiConst.outputSlotOffset.y;
    this.addSlotToContainer(this.outputSlot =
        new SlotCraftingTerm(this.getPlayerInv().player, this.getActionSource(),
            this
                .getPowerSource(), monitorable, crafting, crafting, this.output,
            outputX, outputY, this));

    // This is specifically for the slots, not the gui portion
    // Player Inventory for Tables, offsetX is the distance from the left edge of the GUI to the left edge of the player inventory
    // where the players inventory left edge is the first "inner" pixel of the leftmost slot minus 9 pixels (because why not)
    // some of my GUIs shift this. so. you know. it's a thing.
    this.bindPlayerInventory(ip, this.guiConst.inventoryOffset.x,
        this.guiConst.inventoryOffset.y);

    // Wireless terminals init this later because upgrades
    if (this.ct != null) {
      this.onCraftMatrixChanged(
          new WrapperInvItemHandler(this.getInventoryByName("crafting")));
    }
  }

  public void onCraftMatrixChanged(IInventory inventory) {
    final ContainerNull cn = new ContainerNull();
    final InventoryCrafting ic =
        new InventoryCrafting(cn, this.slotWidth, this.slotHeight);

    for (int x = 0; x < this.slotWidth * this.slotHeight; x++) {
      ic.setInventorySlotContents(x, this.craftingSlots[x].getStack());
    }

    IRecipe recipe = this.getRecipeMatchingGrid(ic);
    this.currentRecipe = recipe;

    if (recipe == null) {
      this.outputSlot.putStack(new ItemStack(Blocks.AIR));
    } else {
      this.outputSlot.putStack(recipe.getCraftingResult(ic));
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

  protected abstract void loadFromNBT();

  public void onChangeInventory(final IItemHandler inv, final int slot,
                                final InvOperation mc,
                                final ItemStack removedStack,
                                final ItemStack newStack) {
    if (this.wt != null) {
      this.wt.saveChanges();
    } else if (this.ct != null) {
      this.ct.saveChanges();
    }
  }

  public IItemHandler getInventoryByName(final String name) {
    if (name.equals("player")) {
      return new PlayerInvWrapper(this.getInventoryPlayer());
    }
    if (this.ct != null) {
      return this.ct.getInventoryByName(name);
    } else if (this.wt != null) {
      return this.wt.getInventoryByName(name);
    }
    return null;
  }

  public boolean useRealItems() {
    return true;
  }

  public IRecipe getCurrentRecipe() {
    return this.currentRecipe;
  }

  public IRecipe getRecipeMatchingGrid(InventoryCrafting ic) {
    World world = this.getPlayerInv().player.getEntityWorld();

    // This is the Blakebr0 Extended Tables recipe handler :)
    // I will not be using that directly :))
    final var recipes = TableRecipeManager.getInstance().getRecipes();
      for (Object recipe : recipes) {
          if (((IRecipe)recipe).matches(ic, world)) {
              return (IRecipe) recipe;
          }
      }

    // Vanilla recipes work for 3x3, these aren't 3x3 (except for basic),
    // so need to make a "fake" intermediary table that hold specifically the 3x3 inner-most grid depending on table size.
    // TODO: This
    if (ModConfig.confTableUseRecipes && ic.getWidth() == 3 && ic.getHeight() == 3) {
      for(IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
        if (recipe.matches(ic, world)) {
          return recipe;
        }
      }
    }

    return null;
  }

  public int getWidth() {
    return this.slotWidth;
  }

  public int getHeight() {
    return this.slotHeight;
  }
}
