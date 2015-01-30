package com.github.itechbear.macroformatter.formatter;

import com.intellij.lang.Language;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import org.apache.commons.lang.StringEscapeUtils;


/**
 * Created by nicholas on 7/27/14.
 */
public class Formatter {
    public static String format(PsiElement psiElement, String text) {
        Project project = psiElement.getProject();
        Language language = psiElement.getLanguage();
        String formatted = format(project, language, text);
        String escaped = StringEscapeUtils.escapeHtml(formatted);
        String whitespaced = escaped.replaceAll(" ", "&nbsp;");
        return wrap(whitespaced);
    }

    public static String format(final Project project, Language language, String text) {
        final PsiFile psiFile = PsiFileFactoryImpl.getInstance(project).createFileFromText("virtual", language, text);

        new WriteCommandAction.Simple<String>(project, psiFile) {
            @Override
            protected void run() throws Throwable {
                CodeStyleManager.getInstance(project).reformat(psiFile, false);
            }
        }.execute();

        String formatted = psiFile.getText();

        new WriteCommandAction.Simple<String>(project, psiFile) {
            @Override
            protected void run() throws Throwable {
                psiFile.delete();
            }
        }.execute();

        if (formatted == null) {
            return text;
        }
        return formatted;
    }

    public static String getFontname() {
        return "monospace";
    }

    public static String wrap(String text) {
        return String.format("<div style=\"font-family:%s\">%s</div>", getFontname(), text);
    }
}
