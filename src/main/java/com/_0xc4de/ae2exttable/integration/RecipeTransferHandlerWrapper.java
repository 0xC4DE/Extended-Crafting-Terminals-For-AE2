/**
 * This file is CURSED. DON'T think about it too hard. It's a wrapper for a class that is not exported by default.
 * Original is here:
 * https://github.com/AE2-UEL/Applied-Energistics-2/blob/master/src/main/java/appeng/integration/modules/jei/RecipeTransferHandler.java
 */
package com._0xc4de.ae2exttable.integration;
import java.lang.reflect.Constructor;

public class RecipeTransferHandlerWrapper {
    public final Class<?> transferHandler ;
    public final Constructor<?> constructor;

    public RecipeTransferHandlerWrapper() throws ClassNotFoundException, NoSuchMethodException {
        transferHandler = Class.forName("appeng.integration.modules.jei.RecipeTransferHandler");
        constructor = transferHandler.getDeclaredConstructor(transferHandler.getClass());
        constructor.setAccessible(true);
    }
}