package com._0xc4de.ae2exttable.interfaces;

import com._0xc4de.ae2exttable.part.ExtInternalInventory;

/**
 * This is used os that we can access the craftingGrid inside my crafting mixin
 */
public interface ICraftingClass {
  ExtInternalInventory craftingGrid = null;
  public int getWidth();
  public int getHeight();

}
