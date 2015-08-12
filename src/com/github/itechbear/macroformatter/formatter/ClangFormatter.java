package com.github.itechbear.macroformatter.formatter;

import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.github.itechbear.macroformatter.option.ConfigurationPanel;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ShowSettingsUtil;

import java.io.*;

/**
 * Created by itechbear on 2015/1/30.
 */
public class ClangFormatter {
    public static String format(String text) {
        String clang_format_path = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_CLANG);
        if (clang_format_path == null || clang_format_path.isEmpty()) {
            clang_format_path = "/usr/bin/clang-format";
        }

        String code_style_name = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_STYLE);
        if (code_style_name != null && !code_style_name.isEmpty()) {
            code_style_name = "-style=" + code_style_name;
        } else {
            code_style_name = "-style=Google";
        }

        boolean is_auto_check = false;
        String auto_check = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_CHECK);
        if (auto_check == null || auto_check.isEmpty() || Boolean.valueOf(auto_check)) {
            is_auto_check = true;
        }

        final Process process;
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(new String[]{clang_format_path, code_style_name});
        } catch (IOException e) {
            e.printStackTrace();
            if (is_auto_check) {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ShowSettingsUtil.getInstance().showSettingsDialog(null, ConfigurationPanel.class);
                    }
                });
            } else {
                String message = "Failed to execute clang-format! Please check your settings (Settings -> Other Settings -> Macro Expansion)" +
                        "<br /> Current clang-format path: " + clang_format_path;
                Notifications.Bus.notify(new Notification("ApplicationName", "CLion-MacroFormatter", message, NotificationType.WARNING));
            }
            return text;
        }

        Thread outThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        outThread.start();

        Thread errorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = process.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        errorThread.start();

        OutputStream outputStream = process.getOutputStream();
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return text;
        }

        try {
            errorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
