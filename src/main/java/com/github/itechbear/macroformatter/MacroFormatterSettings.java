package com.github.itechbear.macroformatter;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Created by itechbear on 2015/1/30.
 */
public class MacroFormatterSettings {
    private static final PropertiesComponent INSTANCE = PropertiesComponent.getInstance();

    public static void set(String key, String value) {
        INSTANCE.setValue(key, value);
    }

    public static String get(String key) {
        return INSTANCE.getValue(key);
    }
}
