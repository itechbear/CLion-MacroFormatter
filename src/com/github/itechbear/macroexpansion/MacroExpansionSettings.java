package com.github.itechbear.macroexpansion;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Created by itechbear on 2015/1/30.
 */
public class MacroExpansionSettings {
    private static final PropertiesComponent INSTANCE = PropertiesComponent.getInstance();

    public static void set(String key, String value) {
        INSTANCE.setValue(key, value);
    }

    public static String get(String key) {
        return INSTANCE.getValue(key);
    }
}
