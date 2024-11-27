package com.xc4de.ae2exttable.client.gui.terminals;

import appeng.api.config.ActionItems;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketInventoryAction;
import appeng.helpers.InventoryAction;
import com.blakebr0.cucumber.util.Utils;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.container.ContainerBasicCraftingTerminal;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.ExtendedCraftingGUIConstants;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class GuiBasicCraftingTerminal extends GuiMEMonitorableTwo {

    private GuiImgButton clearBtn;

    public GuiBasicCraftingTerminal(InventoryPlayer inventoryPlayer, final ITerminalHost te, ContainerBasicCraftingTerminal container) {
        super(inventoryPlayer, te, container, ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
        setGuiType(AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL);
    }

    @Override
    protected void actionPerformed(final GuiButton btn) {
        super.actionPerformed(btn);

        if (this.clearBtn == btn) {
            Slot s = null;
            final Container c = this.inventorySlots;
            for (final Object j : c.inventorySlots) {
                if (j instanceof SlotCraftingMatrix) {
                    s = (Slot) j;
                }
            }

            if (s != null) {
                final PacketInventoryAction p = new PacketInventoryAction(InventoryAction.MOVE_REGION, s.slotNumber, 0);
                NetworkHandler.instance().sendToServer(p);
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        // guiLeft is the offset from the left side of the screen
        // guiTop is the offset from the top of the screen (often small)
        // ySize is how tall it is (like a yMAX) It can be simplified
        // This means the offset for the button is essentially an x,y pair
        this.buttonList.add(this.clearBtn = new GuiImgButton(this.guiLeft + this.getGuiConst().clearButtonOffset.x, this.guiTop + this.ySize - this.getGuiConst().clearButtonOffset.y, Settings.ACTIONS, ActionItems.STASH));
        this.clearBtn.setHalfSize(true);
    }

    @Override
    public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        super.drawFG(offsetX, offsetY, mouseX, mouseY);
        // TODO: Change text

        String displayName = Utils.localize(Tags.MODID + "." + this.getGuiType().toString().toLowerCase() + ".crafting");
        // TODO: 96 is a crafting table head offset, which doesn't work when tables are bigger than normal
        this.fontRenderer.drawString(displayName, 8, this.ySize - 96 + 1 - this.getReservedSpace(), 4210752);
    }

    @Override
    protected String getBackground() {
        return "textures/gui/basic_extended_crafting_terminal.png";
    }
}

