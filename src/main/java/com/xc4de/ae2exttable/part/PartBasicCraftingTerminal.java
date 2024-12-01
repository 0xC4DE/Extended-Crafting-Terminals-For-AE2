package com.xc4de.ae2exttable.part;

import appeng.api.AEApi;
import appeng.api.parts.IPartModel;
import appeng.core.AppEng;
import appeng.helpers.Reflected;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import appeng.util.Platform;
import com.xc4de.ae2exttable.Tags;
import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;
import com.xc4de.ae2exttable.client.gui.PartGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class PartBasicCraftingTerminal extends PartSharedCraftingTerminal {

    public static ResourceLocation[] resources = new ResourceLocation[] {
        new ResourceLocation(Tags.MODID, "part/display_base"), // MODEL_BASE
        new ResourceLocation(Tags.MODID, "part/basic_on"), // MODEL_ON
        new ResourceLocation(Tags.MODID, "part/basic_off"), // MODEL_OFF
        new ResourceLocation("appliedenergistics2", "part/display_status_has_channel"), // 3
        new ResourceLocation("appliedenergistics2", "part/display_status_on"), // 4
        new ResourceLocation("appliedenergistics2", "part/display_status_off") // 5
    };

    private static final IPartModel MODELS_ON = new PartModel(resources[0], resources[1], resources[4]);
    private static final IPartModel MODELS_OFF = new PartModel(resources[0], resources[2], resources[5]);
    private static final IPartModel MODELS_HAS_CHANNEL = new PartModel(resources[0], resources[1], resources[3]);

    @Reflected
    public PartBasicCraftingTerminal(final ItemStack is) {
        super(is, AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL.getGridSize());
    }

    public boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos) {
        if (Platform.isServer()) {
            PartGuiHandler.openGUI(AE2ExtendedGUIs.BASIC_CRAFTING_TERMINAL, player, this.getHost().getTile().getPos(), this.getSide());
        }

        return true;
    }

    @Override
    public @NotNull IPartModel getStaticModels() {
        if (this.isPowered() && this.isActive()) {
            return MODELS_HAS_CHANNEL;
        } else if (this.isPowered()) {
            return MODELS_ON;
        }
        return MODELS_OFF;
    }

}
