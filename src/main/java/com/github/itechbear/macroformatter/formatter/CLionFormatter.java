package com.github.itechbear.macroformatter.formatter;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by nicholas on 7/27/14.
 */
public class CLionFormatter implements Formatter {
    public String format(PsiElement originalMacroPsiElement, String text) {
        final Project project = originalMacroPsiElement.getProject();
        final FileType fileType = originalMacroPsiElement.getContainingFile().getFileType();
        return format(project, fileType, text);
    }

    public static String format(final Project project, FileType fileType, String text) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference<String> holder = new AtomicReference<String>();
        ApplicationManager.getApplication().invokeLater(() -> {
            final PsiFile psiFile1 = PsiFileFactory.getInstance(project).createFileFromText("virtual", fileType, text);
            CodeStyleManager.getInstance(project).reformat(psiFile1);
            String formatted = psiFile1.getText();
            if (formatted == null) {
                holder.set(text);
            } else {
                holder.set(formatted);
            }
            psiFile1.delete();
            countDownLatch.countDown();
        });
        try {
            countDownLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return text;
        }
        return holder.get();
    }
}
