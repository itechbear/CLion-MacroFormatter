package com.github.itechbear.macroformatter.formatter;

import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.github.itechbear.macroformatter.ui.ConfigurationPanel;
import com.intellij.execution.configurations.CommandLineTokenizer;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.psi.PsiElement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by itechbear on 2015/1/30.
 */
public class ClangFormatter implements Formatter {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static List<String> splitCommandLine(final String commandLine) {
        final ArrayList<String> list = new ArrayList<>();
        final CommandLineTokenizer commandLineTokenizer = new CommandLineTokenizer(commandLine);
        while (commandLineTokenizer.hasMoreTokens()) {
            list.add(commandLineTokenizer.nextToken());
        }
        return list;
    }

    public String format(PsiElement originalMacroPsiElement, String text) {
        String clangFormatPath = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_CLANG);
        if (null == clangFormatPath || clangFormatPath.length() == 0) {
            if (isWindows()) {
                clangFormatPath = "C:\\Program Files\\LLVM\\bin\\clang-format.exe";
            } else if (isMac()) {
                clangFormatPath = "/usr/local/bin/clang-format";
            } else {
                clangFormatPath = "/usr/bin/clang-format";
            }
        }
        final List<String> parsedArguments = splitCommandLine(clangFormatPath);
        final ProcessBuilder processBuilder = new ProcessBuilder(parsedArguments);
        final Process process;

        try {
            process = processBuilder.redirectErrorStream(true).start();
            try (
                    OutputStream stdin = process.getOutputStream();
                    InputStream stdout = process.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            ) {
                writer.write(text);
                writer.flush();

                final Scanner scanner = new Scanner(stdout);
                final StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine() + "\n");
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            Notifications.Bus.notify(new Notification("ApplicationName", "CLion-MacroFormatter", e.getMessage(), NotificationType.WARNING));
            return text;
        }
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
    }
}
