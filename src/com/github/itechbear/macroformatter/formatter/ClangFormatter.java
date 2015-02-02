package com.github.itechbear.macroformatter.formatter;

import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.github.itechbear.macroformatter.option.ConfigurationPanel;

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
        }

        final Process process;
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(new String[] {clang_format_path, code_style_name});
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("Failed to execute clang-format! Please check your settings (Settings -> Other Settings -> Macro Expansion)");
            System.out.println("Current clang-format path: " + clang_format_path);
            return text;
        }

        Thread outThread = new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = process.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader (inputStream));
                String line;
                try {
                    while ((line = reader.readLine ()) != null) {
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
                BufferedReader reader = new BufferedReader(new InputStreamReader (inputStream));
                String line;
                try {
                    while ((line = reader.readLine ()) != null) {
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
            outputStream.write((text).getBytes());
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
