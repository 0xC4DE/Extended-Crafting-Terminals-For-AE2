package com._0xc4de.ae2exttable.client.gui;

import appeng.api.config.SearchBoxMode;
import appeng.api.config.Settings;
import appeng.api.config.TerminalStyle;
import appeng.api.implementations.tiles.IViewCellStorage;
import appeng.api.storage.ITerminalHost;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.client.ActionKey;
import appeng.client.gui.AEBaseMEGui;
import appeng.client.gui.widgets.*;
import appeng.client.me.InternalSlotME;
import appeng.client.me.ItemRepo;
import appeng.client.me.SlotME;
import appeng.container.implementations.ContainerMEMonitorable;
import appeng.container.slot.AppEngSlot;
import appeng.container.slot.SlotCraftingMatrix;
import appeng.container.slot.SlotFakeCraftingMatrix;
import appeng.core.AEConfig;
import appeng.core.AELog;
import appeng.core.AppEng;
import appeng.core.localization.GuiText;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketSwitchGuis;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.integration.Integrations;
import appeng.util.IConfigManagerHost;
import appeng.util.Platform;
import com._0xc4de.ae2exttable.client.container.ContainerMEMonitorableTwo;
import com._0xc4de.ae2exttable.client.container.ContainerSharedWirelessTerminals;
import com._0xc4de.ae2exttable.client.container.terminals.ContainerUltimateCraftingTerminal;
import com._0xc4de.ae2exttable.client.container.wireless.ContainerUltimateWirelessTerminal;
import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.cucumber.util.Utils;
import com._0xc4de.ae2exttable.Tags;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiMEMonitorableTwo extends AEBaseMEGui implements ISortSource, IConfigManagerHost {

    public static AE2ExtendedGUIs guiType;
    private static int craftingGridOffsetX;
    private static int craftingGridOffsetY;

    private static String memoryText = "";
    protected final ItemRepo repo;
    private final int offsetX = 9; // XOffset for Item Rendering, won't need to change for me.
    private final int lowerTextureOffset = 0;
    private final IConfigManager configSrc;
    private final boolean viewCell;
    private final ItemStack[] myCurrentViewCells = new ItemStack[5];
    private final ContainerMEMonitorable monitorableContainer;
    private GuiTabButton craftingStatusBtn;
    private MEGuiTextField searchField;
    private int perRow = 9;
    private int reservedSpace = 0;
    private boolean customSortOrder = true;
    private int rows = 0;
    private int standardSize;
    private GuiImgButton ViewBox;
    private GuiImgButton SortByBox;
    private GuiImgButton SortDirBox;
    private GuiImgButton searchBoxSettings;
    private GuiImgButton terminalStyleBox;
    private boolean isAutoFocus = false;
    private int currentMouseX = 0;
    private int currentMouseY = 0;
    private boolean delayedUpdate;
    private final ExtendedCraftingGUIConstants guiConst;

    protected int jeiOffset = Platform.isModLoaded("jei") ? 24 : 0;

    public GuiMEMonitorableTwo(final InventoryPlayer inventoryPlayer, final ITerminalHost te) {
        this(inventoryPlayer, te, new ContainerMEMonitorable(inventoryPlayer, te), ExtendedCraftingGUIConstants.BASIC_CRAFTING_TERMINAL);
    }

    public GuiMEMonitorableTwo(final InventoryPlayer inventoryPlayer, final ITerminalHost te, final ContainerMEMonitorable c, ExtendedCraftingGUIConstants guiConst) {
        super(c);
        final GuiScrollbar scrollbar = new GuiScrollbar();
        this.setScrollBar(scrollbar);
        this.repo = new ItemRepo(scrollbar, this);
        this.configSrc = ((IConfigurableObject) this.inventorySlots).getConfigManager();
        (this.monitorableContainer = (ContainerMEMonitorable) this.inventorySlots).setGui(this);

        this.viewCell = te instanceof IViewCellStorage;

        this.guiConst = guiConst;
        this.xSize = guiConst.textureActualSize.x;
        this.ySize = guiConst.textureActualSize.y;

        this.standardSize = this.xSize;

        craftingGridOffsetX = guiConst.craftingGridOffset.x;
        craftingGridOffsetY = guiConst.craftingGridOffset.y;
        this.setReservedSpace(guiConst.reservedSpace);
    }

    public void setGuiType(AE2ExtendedGUIs gui) {
        this.guiType = gui;
    }

    public AE2ExtendedGUIs getGuiType() { return this.guiType; }

    public void postUpdate(final List<IAEItemStack> list) {
        for (final IAEItemStack is : list) {
            this.repo.postUpdate(is);
        }

        if (isShiftKeyDown()) {
            for (Slot slot : this.inventorySlots.inventorySlots) {
                if (slot instanceof SlotME) {
                    if (this.isPointInRegion(slot.xPos, slot.yPos, 18, 18, currentMouseX, currentMouseY)) {
                        this.delayedUpdate = true;
                        break;
                    }
                }
            }
        }

        if (!this.delayedUpdate) {
            this.repo.updateView();
            this.setScrollBar();
        }
    }

    private void setScrollBar() {
        this.getScrollBar().setTop(18).setLeft(175).setHeight(this.rows * 18 - 2);
        this.getScrollBar().setRange(0, (this.repo.size() + this.perRow - 1) / this.perRow - this.rows, Math.max(1, this.rows / 6));
    }

    @Override
    protected void actionPerformed(final GuiButton btn) {
        if (btn == this.craftingStatusBtn) {
            NetworkHandler.instance().sendToServer(new PacketSwitchGuis(GuiBridge.GUI_CRAFTING_STATUS));
        }

        if (btn instanceof GuiImgButton iBtn) {
            final boolean backwards = Mouse.isButtonDown(1);

            if (iBtn.getSetting() != Settings.ACTIONS) {
                final Enum cv = iBtn.getCurrentValue();
                final Enum next = Platform.rotateEnum(cv, backwards, iBtn.getSetting().getPossibleValues());

                if (btn == this.terminalStyleBox) {
                    AEConfig.instance().getConfigManager().putSetting(iBtn.getSetting(), next);
                } else if (btn == this.searchBoxSettings) {
                    AEConfig.instance().getConfigManager().putSetting(iBtn.getSetting(), next);
                } else {
                    try {
                        NetworkHandler.instance().sendToServer(new PacketValueConfig(iBtn.getSetting().name(), next.name()));
                    } catch (final IOException e) {
                        AELog.debug(e);
                    }
                }

                iBtn.set(next);

                if (next.getClass() == SearchBoxMode.class || next.getClass() == TerminalStyle.class) {
                    this.reinitalize();
                }
            }
        }
    }

    private void reinitalize() {
        this.buttonList.clear();
        this.initGui();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        final int magicNumber = 114 + 1; // ???
        final int extraSpace = this.height - magicNumber - this.reservedSpace;
        this.rows = (int) Math.floor(extraSpace/18); // Calculates rows of items

        if (this.rows < this.getMinRows()) { // Clamps the rows, cannot use Math.clamp because this isn't java 21 :(
            this.rows = 3; // Minimum defined by the GUI element itself
        } else if (this.rows > this.getMaxRows()) {
            this.rows = this.getMaxRows();
        }
        this.perRow = 9; // 9 items per row because I said so

        // Render internal ME Items
        this.getMeSlots().clear();
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.perRow; x++) {
                this.getMeSlots().add(new InternalSlotME(this.repo, x + y * this.perRow, this.offsetX + x * 18, 18 + y * 18));
            }
        }

        if (AEConfig.instance().getConfigManager().getSetting(Settings.TERMINAL_STYLE) != TerminalStyle.FULL) {
            this.xSize = this.standardSize + ((this.perRow - 9) * 18);
        } else {
            this.xSize = this.standardSize;
        }

        // Determines guiTop and guiLeft
        // ensures all player slots are slotME
        super.initGui();
        // full size : 204
        // extra slots : 72
        // slot 18

        this.ySize = magicNumber + this.rows * 18 + this.reservedSpace;
        final int unusedSpace = this.height - this.ySize;
        this.guiTop = (int) Math.floor(unusedSpace / (unusedSpace < 0 ? 3.8f : 2.0f));

        int offset = this.guiTop + 8 + jeiOffset;

        {
            if (this.customSortOrder) {
                this.buttonList
                        .add(this.SortByBox = new GuiImgButton(this.guiLeft - 18, offset, Settings.SORT_BY, this.configSrc.getSetting(Settings.SORT_BY)));
                offset += 20;
            }
        }

        // Adding the buttons for terminal styling
        if (this.viewCell) {
            this.buttonList
                    .add(this.ViewBox = new GuiImgButton(this.guiLeft - 18, offset, Settings.VIEW_MODE, this.configSrc.getSetting(Settings.VIEW_MODE)));
            offset += 20;
        }

        this.buttonList.add(this.SortDirBox = new GuiImgButton(this.guiLeft - 18, offset, Settings.SORT_DIRECTION, this.configSrc
                .getSetting(Settings.SORT_DIRECTION)));
        offset += 20;

        this.buttonList.add(
                this.searchBoxSettings = new GuiImgButton(this.guiLeft - 18, offset, Settings.SEARCH_MODE, AEConfig.instance()
                        .getConfigManager()
                        .getSetting(
                                Settings.SEARCH_MODE)));

        offset += 20;

        this.buttonList.add(this.terminalStyleBox = new GuiImgButton(this.guiLeft - 18, offset, Settings.TERMINAL_STYLE, AEConfig.instance()
                .getConfigManager()
                .getSetting(Settings.TERMINAL_STYLE)));

        // Adding Search bar
        this.searchField = new MEGuiTextField(this.fontRenderer, this.guiLeft + Math.max(80, this.offsetX), this.guiTop + 4, 90, 12);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setMaxStringLength(25);
        this.searchField.setTextColor(0xFFFFFF);
        this.searchField.setSelectionColor(0xFF008000);
        this.searchField.setVisible(true);

        // Button for Crafting status
        if (this.viewCell) {
            this.buttonList.add(this.craftingStatusBtn = new GuiTabButton(this.guiLeft + 170, this.guiTop - 4, 2 + 11 * 16, GuiText.CraftingStatus
                    .getLocal(), this.itemRender));
            this.craftingStatusBtn.setHideEdge(13);
        }

        // More search related objects
        final Enum searchModeSetting = AEConfig.instance().getConfigManager().getSetting(Settings.SEARCH_MODE);

        this.isAutoFocus = SearchBoxMode.AUTOSEARCH == searchModeSetting || SearchBoxMode.JEI_AUTOSEARCH == searchModeSetting || SearchBoxMode.AUTOSEARCH_KEEP == searchModeSetting || SearchBoxMode.JEI_AUTOSEARCH_KEEP == searchModeSetting;
        final boolean isKeepFilter = SearchBoxMode.AUTOSEARCH_KEEP == searchModeSetting || SearchBoxMode.JEI_AUTOSEARCH_KEEP == searchModeSetting || SearchBoxMode.MANUAL_SEARCH_KEEP == searchModeSetting || SearchBoxMode.JEI_MANUAL_SEARCH_KEEP == searchModeSetting;
        final boolean isJEIEnabled = SearchBoxMode.JEI_AUTOSEARCH == searchModeSetting || SearchBoxMode.JEI_MANUAL_SEARCH == searchModeSetting;

        this.searchField.setFocused(this.isAutoFocus);

        if (isJEIEnabled) {
            memoryText = Integrations.jei().getSearchText();
        }

        if (isKeepFilter && memoryText != null && !memoryText.isEmpty()) {
            this.searchField.setText(memoryText);
            this.searchField.selectAll();
            this.repo.setSearchString(memoryText);
            this.setScrollBar();
        }

        // Render the internal crafting grid slots NOT slice of GUI
        craftingGridOffsetX = Integer.MAX_VALUE;
        craftingGridOffsetY = Integer.MAX_VALUE;

        if (this.inventorySlots instanceof ContainerUltimateWirelessTerminal || this.inventorySlots instanceof ContainerUltimateCraftingTerminal) {
            AppEngSlot s = ((ContainerMEMonitorableTwo)this.inventorySlots).outputSlot;
            s.yPos = s.getY() + (this.rows - this.getMinRows()) * 18;
        }

        for (final Object s : this.inventorySlots.inventorySlots) {
            if (s instanceof AppEngSlot) {
                if (((Slot) s).xPos < 197) {
                    this.repositionSlot((AppEngSlot) s);
                }
            }

            if (s instanceof SlotCraftingMatrix || s instanceof SlotFakeCraftingMatrix) {
                final Slot g = (Slot) s;
                if (g.xPos > 0 && g.yPos > 0) {
                    craftingGridOffsetX = Math.min(craftingGridOffsetX, g.xPos);
                    craftingGridOffsetY = Math.min(craftingGridOffsetY, g.yPos);
                }
            }
        }

        // Purpose: ???
        craftingGridOffsetX -= 25;
        craftingGridOffsetY -= 6;

    }

    @Override
    public List<Rectangle> getJEIExclusionArea() {
        List<Rectangle> exclusionArea = new ArrayList<>();

        int yOffset = guiTop + 8 + jeiOffset;

        int visibleButtons = (int) this.buttonList.stream().filter(v -> v.enabled && v.x < guiLeft).count();
        Rectangle sortDir = new Rectangle(guiLeft - 18, yOffset, 20, visibleButtons * 20 + visibleButtons - 2);
        exclusionArea.add(sortDir);

        if (this.viewCell) {
            Rectangle viewMode = new Rectangle(guiLeft + 205, yOffset - 4, 24, 19 * monitorableContainer.getViewCells().length);
            exclusionArea.add(viewMode);
        }

        return exclusionArea;
    }

    @Override
    public void drawFG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        String displayName = Utils.localize(Tags.MODID + "." + this.getGuiType().toString().toLowerCase());
        this.fontRenderer.drawString(displayName, 8, 6, 4210752);
        this.fontRenderer.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 3, 4210752);

        this.currentMouseX = mouseX;
        this.currentMouseY = mouseY;
    }

    @Override
    protected void mouseClicked(final int xCoord, final int yCoord, final int btn) throws IOException {
        this.searchField.mouseClicked(xCoord, yCoord, btn);

        if (btn == 1 && this.searchField.isMouseIn(xCoord, yCoord)) {
            this.searchField.setText("");
            this.repo.setSearchString("");
            this.setScrollBar();
        }

        super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        memoryText = this.searchField.getText();
    }

    @Override
    public void drawBG(final int offsetX, final int offsetY, final int mouseX, final int mouseY) {
        int textureFullWidth, textureFullHeight;
        textureFullWidth = guiConst.textureSize.x;
        textureFullHeight = guiConst.textureSize.y;

        ResourceLocation loc = new ResourceLocation(Tags.MODID, this.getBackground());
        this.mc.getTextureManager().bindTexture(loc);

        // Thank you blake for drawTexturedMODELRect.
        //RenderHelper.drawTexturedModelRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize, textureFullWidth, textureFullHeight);
        int x_width = 197; // INITIAL x_width for inventory rendering to cut out the cells, needs to be bigger for other GUIs

        // Render area for search bar
        RenderHelper.drawTexturedModelRect(offsetX, offsetY, 0, 0, x_width, 18, textureFullWidth, textureFullHeight);

        if (this.viewCell) {
            // Renders the view cell bar away from the top right of the actual render window, away from JEI
            RenderHelper.drawTexturedModelRect(offsetX + x_width, offsetY , x_width, 0, 46, 104, textureFullWidth, textureFullHeight);
        }

        // Renders the actual rows of items inside the AE/ME system
        for (int x = 0; x < this.rows; x++) {
            RenderHelper.drawTexturedModelRect(offsetX, offsetY + 18 + x * 18, 0, 18, x_width, 18, textureFullWidth, textureFullHeight);
        }

        // Draws grid and then inventory, needs to be segmented.
        // 104 is the default height of the view cell box that is stored in the sprite.
        // 106-18-18 is the y value of the texture up to the point that the standard ME grid is 'done'. hence the starting point.
        // 37 is my magic offset to avoid all view cell texture.
        RenderHelper.drawTexturedModelRect(offsetX, offsetY + 16 + this.rows * 18 + this.lowerTextureOffset, 0, 106 - 18 - 18, x_width,
                37, textureFullWidth, textureFullHeight);

        // Swapped to full width as I am now out of the way of the view cells, probably
        // Start at oldY+37 to actually render the rest of it.
        // Also add 37 to internal starting point. then remove 37 from ending point
        RenderHelper.drawTexturedModelRect(offsetX, (offsetY + 16 + this.rows * 18 + this.lowerTextureOffset) + 37, 0, 106 - 18 - 18 + 37,
                textureFullWidth, 99 + this.reservedSpace - 37, textureFullWidth, textureFullHeight);


        if (this.viewCell) {
            boolean update = false;

            for (int i = 0; i < 5; i++) {
                if (this.myCurrentViewCells[i] != this.monitorableContainer.getCellViewSlot(i).getStack()) {
                    update = true;
                    this.myCurrentViewCells[i] = this.monitorableContainer.getCellViewSlot(i).getStack();
                }
            }

            if (update) {
                this.repo.setViewCell(this.myCurrentViewCells);
            }
        }

        if (this.searchField != null) {
            this.searchField.drawTextBox();
        }
    }

    protected String getBackground() {
        return "guis/terminal.png";
    }

    @Override
    protected boolean isPowered() {
        return this.repo.hasPower();
    }

    protected int getMaxRows() {
        TerminalStyle style = (TerminalStyle) AEConfig.instance().getConfigManager().getSetting(Settings.TERMINAL_STYLE);
        if (style == TerminalStyle.TALL)
            return guiConst.maxRows; // Probably usually maxInt
        return this.getMinRows();
    }

    protected int getMinRows() {
        return this.guiConst.minRows;
    }


    protected void repositionSlot(final AppEngSlot s) {
        s.yPos = s.getY() + this.ySize - 78 - 5;
    }

    @Override
    protected void keyTyped(final char character, final int key) throws IOException {

        if (!this.checkHotbarKeys(key)) {
            if (AppEng.proxy.isActionKey(ActionKey.TOGGLE_FOCUS, key)) {
                this.searchField.setFocused(!this.searchField.isFocused());
                return;
            }

            if (this.searchField.isFocused() && key == Keyboard.KEY_RETURN) {
                this.searchField.setFocused(false);
                return;
            }

            if (character == ' ' && this.searchField.getText().isEmpty()) {
                return;
            }

            final boolean mouseInGui = this.isPointInRegion(0, 0, this.xSize, this.ySize, this.currentMouseX, this.currentMouseY);
            final boolean wasSearchFieldFocused = this.searchField.isFocused();

            if (this.isAutoFocus && !this.searchField.isFocused() && mouseInGui) {
                this.searchField.setFocused(true);
            }

            if (this.searchField.textboxKeyTyped(character, key)) {
                this.repo.setSearchString(this.searchField.getText());
                this.setScrollBar();
                // tell forge the key event is handled and should not be sent out
                this.keyHandled = mouseInGui;
            } else {
                if (!wasSearchFieldFocused) {
                    // prevent unhandled keys (like shift) from focusing the search field
                    searchField.setFocused(false);
                }
                super.keyTyped(character, key);
            }
        }
    }

    @Override
    public void updateScreen() {
        this.repo.setPower(this.monitorableContainer.isPowered());
        if (this.delayedUpdate) {
            if (isShiftKeyDown()) {
                this.delayedUpdate = false;
                for (Slot slot : this.inventorySlots.inventorySlots) {
                    if (slot instanceof SlotME) {
                        if (this.isPointInRegion(slot.xPos, slot.yPos, 18, 18, currentMouseX, currentMouseY)) {
                            this.delayedUpdate = true;
                            break;
                        }
                    }
                }
            } else {
                this.delayedUpdate = false;
            }
        }
        if (!this.delayedUpdate) {
            this.repo.updateView();
            this.setScrollBar();
        }
        super.updateScreen();
    }

    @Override
    public Enum getSortBy() {
        return this.configSrc.getSetting(Settings.SORT_BY);
    }

    @Override
    public Enum getSortDir() {
        return this.configSrc.getSetting(Settings.SORT_DIRECTION);
    }

    @Override
    public Enum getSortDisplay() {
        return this.configSrc.getSetting(Settings.VIEW_MODE);
    }

    @Override
    public void updateSetting(final IConfigManager manager, final Enum settingName, final Enum newValue) {
        if (this.SortByBox != null) {
            this.SortByBox.set(this.configSrc.getSetting(Settings.SORT_BY));
        }

        if (this.SortDirBox != null) {
            this.SortDirBox.set(this.configSrc.getSetting(Settings.SORT_DIRECTION));
        }

        if (this.ViewBox != null) {
            this.ViewBox.set(this.configSrc.getSetting(Settings.VIEW_MODE));
        }

        this.repo.updateView();
    }

    protected int getReservedSpace() {
        return this.reservedSpace;
    }

    void setReservedSpace(final int reservedSpace) {
        this.reservedSpace = reservedSpace;
    }

    public boolean isCustomSortOrder() {
        return this.customSortOrder;
    }

    void setCustomSortOrder(final boolean customSortOrder) {
        this.customSortOrder = customSortOrder;
    }

    public int getStandardSize() {
        return this.standardSize;
    }

    void setStandardSize(final int standardSize) {
        this.standardSize = standardSize;
    }

    public ExtendedCraftingGUIConstants getGuiConst() {return this.guiConst;}
}
