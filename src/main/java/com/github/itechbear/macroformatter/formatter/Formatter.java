package com.github.itechbear.macroformatter.formatter;

import com.intellij.psi.PsiElement;

public interface Formatter {
    String format(PsiElement originalMacroPsiElement, String text);
}
