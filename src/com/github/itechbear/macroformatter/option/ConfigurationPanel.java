package com.github.itechbear.macroformatter.option;

import com.github.itechbear.macroformatter.JFilePicker;
import com.github.itechbear.macroformatter.JLabeledCombox;
import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.panels.VerticalLayout;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by itechbear on 2015/1/30.
 */
public class ConfigurationPanel implements Configurable {
    private boolean modified = false;
    private JFilePicker jFilePickerClang;
    private JLabeledCombox jLabeledCombox;
    private JCheckBox jCheckBox;
    private OptionModifiedListener listener = new OptionModifiedListener(this);
    private ComboxItemListener combox_listener = new ComboxItemListener(this);
    private CheckboxListener checkbox_listener = new CheckboxListener(this);

    public static final String OPTION_KEY_CLANG = "macroformatter.clang";
    public static final String OPTION_KEY_STYLE = "macroformatter.style";
    public static final String OPTION_KEY_CHECK = "macroformatter.check";

    @Nls
    @Override
    public String getDisplayName() {
        return "Macro Formatter";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel jPanel = new JPanel();

        VerticalLayout verticalLayout = new VerticalLayout(1, 2);
        jPanel.setLayout(verticalLayout);

        jFilePickerClang = new JFilePicker("clang-format Path:", "...");
        jLabeledCombox = new JLabeledCombox("Code Style: ");
        jCheckBox = new JCheckBox("Automatically show this page if clang-format is not found.");

        reset();

        jFilePickerClang.getTextField().getDocument().addDocumentListener(listener);
        jLabeledCombox.getCombobox().addItemListener(combox_listener);
        jCheckBox.addActionListener(checkbox_listener);

        jPanel.add(jCheckBox);
        jPanel.add(jFilePickerClang);
        jPanel.add(jLabeledCombox);

        return jPanel;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        MacroFormatterSettings.set(OPTION_KEY_CLANG, jFilePickerClang.getTextField().getText());
        MacroFormatterSettings.set(OPTION_KEY_STYLE, String.valueOf(jLabeledCombox.getCombobox().getSelectedItem()));
        MacroFormatterSettings.set(OPTION_KEY_CHECK, String.valueOf(jCheckBox.isSelected()));
        modified = false;
    }

    @Override
    public void reset() {
        String clang_path = MacroFormatterSettings.get(OPTION_KEY_CLANG);
        if (clang_path == null || clang_path.isEmpty()) {
            jFilePickerClang.getTextField().setText("/usr/bin/clang-format");
        } else {
            jFilePickerClang.getTextField().setText(clang_path);
        }

        String code_style = MacroFormatterSettings.get(OPTION_KEY_STYLE);
        if (code_style != null && !code_style.isEmpty()) {
            jLabeledCombox.getCombobox().setSelectedItem(code_style);
        }

        String auto_check = MacroFormatterSettings.get(OPTION_KEY_CHECK);
        if (auto_check != null && !auto_check.isEmpty()) {
            jCheckBox.setSelected(Boolean.valueOf(auto_check));
        } else {
            jCheckBox.setSelected(true);
        }

        modified = false;
    }

    @Override
    public void disposeUIResources() {
        jFilePickerClang.getTextField().getDocument().removeDocumentListener(listener);
        jLabeledCombox.getCombobox().removeItemListener(combox_listener);
        jCheckBox.removeActionListener(checkbox_listener);
    }

    private static class OptionModifiedListener implements DocumentListener {
        private final ConfigurationPanel configuration;

        public OptionModifiedListener(ConfigurationPanel configuration) {
            this.configuration = configuration;
        }
        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            configuration.setModified(true);
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            configuration.setModified(true);
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            configuration.setModified(true);
        }
    }

    private static class ComboxItemListener implements ItemListener {
        private final ConfigurationPanel configuration;

        ComboxItemListener(ConfigurationPanel configuration) {
            this.configuration = configuration;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            configuration.setModified(true);
        }
    }

    private static class CheckboxListener implements ActionListener {
        private final ConfigurationPanel configuration;

        CheckboxListener(ConfigurationPanel configuration) {
            this.configuration = configuration;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            configuration.setModified(true);
        }
    }
}
