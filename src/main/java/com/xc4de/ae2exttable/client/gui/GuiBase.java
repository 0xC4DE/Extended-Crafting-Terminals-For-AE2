package com.xc4de.ae2exttable.client.gui;

import com.xc4de.ae2exttable.client.container.ContainerBase;
import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class GuiBase extends GuiContainer {
   public GuiBase(ContainerBase container) {
       super(container);
   };
}
