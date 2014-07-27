package com.github.itechbear.macroexpansion.formatter;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.lang.Language;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiFileFactoryImpl;

import java.lang.reflect.Field;

/**
 * Created by nicholas on 7/27/14.
 */
public class Formatter {
    public static String format(PsiElement psiElement, String text) {
        Project project = psiElement.getProject();
        Language language = psiElement.getLanguage();
        return format(project, language, text);
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
}
