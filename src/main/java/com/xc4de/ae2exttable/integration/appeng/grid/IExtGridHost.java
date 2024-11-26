package com.xc4de.ae2exttable.integration.appeng.grid;

import appeng.api.networking.IGridHost;
import appeng.api.util.DimensionalCoord;

public interface IExtGridHost extends IGridHost {

    DimensionalCoord getLocation();

    void gridChanged();
}
