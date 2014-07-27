package com.github.itechbear.macroexpansion.formatter;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
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

    public static String format(Project project, Language language, String text) {
        DocumentImpl document2 = new DocumentImpl(text, true, true);
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document2);
//        PsiFile psiFile = PsiDocumentManager.getInstance(project).getCachedPsiFile(document);
//        if (psiFile == null) {
//            return text;
//        }
        PsiFile psiFile = PsiFileFactoryImpl.getInstance(project).createFileFromText("virtual", language, text);
        DocumentImpl document = (DocumentImpl) FileDocumentManager.getInstance().getDocument(psiFile.getVirtualFile());
        Field field = null;
        try {
            field = DocumentImpl.class.getDeclaredField("myAssertThreading");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return text;
        }
        if (field == null) {
            return text;
        }
        field.setAccessible(true);
        try {
            field.set(document, false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return text;
        }
//        CodeStyleSettings codeStyleSettings = ProjectCodeStyleSettingsManager.getSettings(project);
//        FormattingModelBuilder formattingModel = LanguageFormatting.INSTANCE.forContext(psiFile);
//        if (formattingModel == null) {
//            return text;
//        }
//        FormattingModel model = formattingModel.createModel(psiFile, codeStyleSettings);
//        model.commitChanges();
//        return psiFile.getText();
//        ReformatCodeProcessor reformatCodeProcessor = new ReformatCodeProcessor(project, psiFile, psiFile.getTextRange(), false);
//        FutureTask<Boolean> task = reformatCodeProcessor.preprocessFile(psiFile, false);
//        CommandProcessor commandProcessor = CommandProcessor.getInstance();
//        task.run();
        CodeStyleManager.getInstance(project).reformat(psiFile, false);
        return psiFile.getText();
    }
}
