package com.github.itechbear.macroformatter.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by itechbear on 2015/1/30.
 */
public class JLabeledCombox extends JPanel {
    public enum Formatter {
        Builtin("IDE Builtin"),
        Clang("Clang-Format");

        private Formatter(final String value) {
            this.value = value;
        }

        private final String value;

        public String value() {
            return value;
        }
    }

    private JLabel label;
    private com.intellij.openapi.ui.ComboBox box;

    public JLabeledCombox(String text) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // creates the GUI
        label = new JLabel(text);

        box = new com.intellij.openapi.ui.ComboBox();

        box.addItem(Formatter.Builtin.value);
        box.addItem(Formatter.Clang.value);

        add(label);
        add(box);
    }

    public JLabel getLabel() {
        return label;
    }

    public com.intellij.openapi.ui.ComboBox getCombobox() {
        return box;
    }
}
