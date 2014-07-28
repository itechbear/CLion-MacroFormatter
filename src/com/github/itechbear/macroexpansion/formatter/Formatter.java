package com.github.itechbear.macroexpansion.formatter;

import com.intellij.codeEditor.printing.FileSeparatorProvider;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.ide.highlighter.HighlighterFactory;
import com.intellij.lang.Language;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicholas on 7/27/14.
 */
public class Formatter {
    public static String format(PsiElement psiElement, String text) {
        Project project = psiElement.getProject();
        Language language = psiElement.getLanguage();
        String formatted = format(project, language, text);
        return wrap(formatted);
    }

    public static String format(final Project project, Language language, String text) {
        final PsiFile psiFile = PsiFileFactoryImpl.getInstance(project).createFileFromText("virtual", language, text);
        WriteCommandAction.Simple<String> command = new WriteCommandAction.Simple<String>(project, psiFile) {

            @Override
            protected void run() throws Throwable {
                CodeStyleManager.getInstance(project).reformat(psiFile, false);
            }
        };
        command.execute();
        String formatted = psiFile.getText();
        if (formatted == null) {
            return text;
        }
        return formatted.replace(" ", "&nbsp;");
    }

    public static String getFontname() {
        return "monospace";
    }

    public static String wrap(String text) {
        return String.format("<div style=\"font-family:%s\">%s</div>", getFontname(), text);
    }
}
