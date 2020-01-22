package com.github.itechbear.macroformatter.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by HD on 2015/1/1.
 */

public class JLabeledTextField extends JPanel {
    private JLabel label;
    private JTextField textField;

    public JLabeledTextField(String textFieldLabel) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // creates the GUI
        label = new JLabel(textFieldLabel);

        textField = new JTextField(30);

        add(label);
        add(textField);
    }

    public JTextField getTextField() {
        return textField;
    }
}