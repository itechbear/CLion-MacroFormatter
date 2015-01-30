package com.github.itechbear.macroformatter.formatter;

import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.github.itechbear.macroformatter.option.ConfigurationPanel;

import java.io.*;

/**
 * Created by itechbear on 2015/1/30.
 */
public class ClangFormatter {
    private static void pipeStream(InputStream input, OutputStream output)
            throws IOException {
        byte buffer[] = new byte[1024];
        int numRead = 0;

        do {
            numRead = input.read(buffer);
            output.write(buffer, 0, numRead);
        } while (input.available() > 0);

        output.flush();
    }

    public static String format(String text) {
        String clang_path = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_CLANG);
        if (clang_path == null || clang_path.isEmpty()) {
            return text;
        }

        String code_style = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_STYLE);
        if (!code_style.isEmpty()) {
            code_style = "-style=" + code_style;
        }
        final Process process;
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec(new String[] {clang_path, code_style});
        } catch (IOException e) {
            e.printStackTrace();
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
