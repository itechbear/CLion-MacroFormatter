package com.github.itechbear.macroexpansion.provider;

import com.github.itechbear.macroexpansion.formatter.Formatter;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.jetbrains.objc.psi.OCMacroCall;
import com.jetbrains.objc.psi.impl.OCMacroReferenceElementImpl;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by nicholas on 7/25/14.
 */
public class MacroDocumentationProvider implements DocumentationProvider {
    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement psiElement2) {
        if (!(psiElement2 instanceof OCMacroReferenceElementImpl)) {
            return null;
        }
        PsiElement parent = psiElement2.getParent();
        if (!(parent instanceof OCMacroCall)) {
            return null;
        }
//        OCFile file = ((OCMacroReferenceElementImpl) psiElement2).getContainingOCFile();
//        if (file == null) {
//            return null;
//        }
//        VirtualFile virtualFile = file.getVirtualFile();
        String expansion = ((OCMacroCall) parent).getReplacementText();

        return Formatter.format(psiElement2, expansion);
        // return "Macro Expansion: \n" + expansion;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement2) {
        return null;
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement psiElement, @Nullable PsiElement psiElement2) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) {
        return null;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String s, PsiElement psiElement) {
        return null;
    }
}
