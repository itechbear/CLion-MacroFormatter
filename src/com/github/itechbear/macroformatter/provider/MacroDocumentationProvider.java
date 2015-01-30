package com.github.itechbear.macroformatter.provider;

import com.github.itechbear.macroformatter.formatter.ClangFormatter;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.objc.documentation.CidrDocumentationProvider;
import com.jetbrains.objc.psi.OCDefineDirective;
import com.jetbrains.objc.psi.OCMacroCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by nicholas on 7/25/14.
 */
public class MacroDocumentationProvider extends CidrDocumentationProvider {
    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement psiElement1) {
        String var = super.getQuickNavigateInfo(psiElement, psiElement1);
        return var;
    }

    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement psiElement1) {
        return super.getUrlFor(psiElement, psiElement1);
    }

    @Nullable
    @Override
    public String generateDoc(PsiElement psiElement, @Nullable PsiElement psiElement1) {
        String str = super.generateDoc(psiElement, psiElement1);
        return str;
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(@Nullable PsiManager psiManager, @Nullable Object o, @Nullable PsiElement psiElement) {
        return super.getDocumentationElementForLookupItem(psiManager, o, psiElement);
    }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(@Nullable PsiManager psiManager, @Nullable String s, @Nullable PsiElement psiElement) {
        return super.getDocumentationElementForLink(psiManager, s, psiElement);
    }

    @Nullable
    @Override
    public String generateDocInnerHtml(@NotNull PsiElement var1, @Nullable PsiElement var2, boolean var3) {
        StringBuilder var4 = new StringBuilder();

        if (var1 instanceof OCDefineDirective) {
            getDoc(var2, var4, (OCDefineDirective) var1);
            return var4.length() == 0 ? null : var4.toString();
        }

        return super.generateDocInnerHtml(var1, var2, var3);
    }

    private static void getDoc(@Nullable PsiElement var0, @NotNull StringBuilder var1, @NotNull OCDefineDirective var2) {
        if (var1.length() > 0) {
            var1.append("<hr/>");
        }
        if (var1.length() == 0) {
            PsiFile var3 = var2.getContainingFile();
            if (var3 != null) {
                var1.append("<b>Declared In:</b> " + var3.getName() + "<br><br>");
            }
        }

        var1.append("<b>Definition:</b><br><br> <tt><pre>");
        var1.append(StringUtil.escapeXml(String.valueOf(var2.getSymbol())));
        var1.append("</pre></tt><br>");
        OCMacroCall var4 = (OCMacroCall) PsiTreeUtil.getContextOfType(var0, new Class[]{OCMacroCall.class});
        if (var4 != null) {
            var1.append("<b>Replacement</b><br><br> <tt><pre>");
            String replacement = var4.getReplacementText();
            // replacement = Formatter.format(var0, replacement);
            replacement = ClangFormatter.format(replacement);
            var1.append(StringUtil.escapeXml(replacement));
            var1.append("</pre></tt>");
        }
    }
}
