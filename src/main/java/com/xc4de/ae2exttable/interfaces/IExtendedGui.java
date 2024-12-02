package com.xc4de.ae2exttable.interfaces;

import com.xc4de.ae2exttable.client.gui.AE2ExtendedGUIs;

public interface IExtendedGui {
    public AE2ExtendedGUIs guiType = null;

    public AE2ExtendedGUIs getGuiType();

    public void setGuiType(AE2ExtendedGUIs guiType);
}
