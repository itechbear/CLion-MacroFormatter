package com.github.itechbear.macroexpansion;

import javax.swing.*;
import java.awt.*;

/**
 * Created by itechbear on 2015/1/30.
 */
public class JLabeledCombox extends JPanel {
    private JLabel label;
    private com.intellij.openapi.ui.ComboBox box;

    public JLabeledCombox(String text) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // creates the GUI
        label = new JLabel(text);

        box = new com.intellij.openapi.ui.ComboBox();

        box.addItem("Chromium");
        box.addItem("Google");
        box.addItem("LLVM");
        box.addItem("Mozilla");
        box.addItem("WebKit");

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
