package com.github.itechbear.macroformatter.provider;

import com.github.itechbear.macroformatter.ui.JLabeledCombox;
import com.github.itechbear.macroformatter.MacroFormatterSettings;
import com.github.itechbear.macroformatter.formatter.CLionFormatter;
import com.github.itechbear.macroformatter.formatter.ClangFormatter;
import com.github.itechbear.macroformatter.formatter.Formatter;
import com.github.itechbear.macroformatter.ui.ConfigurationPanel;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.cidr.lang.documentation.CidrDocumentationProvider;
import com.jetbrains.cidr.lang.psi.OCDefineDirective;
import com.jetbrains.cidr.lang.psi.OCMacroCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by nicholas on 7/25/14.
 */
public class MacroDocumentationProvider extends CidrDocumentationProvider {
    @Nullable
    @Override
    public String generateDocInnerHtml(@NotNull PsiElement macroPsiElement, @Nullable PsiElement var2, boolean var3) {
        final Formatter formatter;
        final String formatterName = MacroFormatterSettings.get(ConfigurationPanel.OPTION_KEY_FORMMATER);
        if (JLabeledCombox.Formatter.Builtin.value().equals(formatterName)) {
            formatter = new ClangFormatter();
        } else {
            formatter = new CLionFormatter();
        }

        if (macroPsiElement instanceof OCDefineDirective) {
            final String doc = getDoc(formatter, var2, (OCDefineDirective) macroPsiElement);
            return doc.length() == 0 ? null : doc;
        }

        return super.generateDocInnerHtml(macroPsiElement, var2, var3);
    }

    private static String getDoc(@NotNull Formatter formatter, @Nullable PsiElement var0, @NotNull OCDefineDirective var2) {
        final StringBuilder var1 = new StringBuilder();
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
            replacement = formatter.format(var0, replacement);
            var1.append(StringUtil.escapeXml(replacement));
            var1.append("</pre></tt>");
        }
        return var1.toString();
    }
}
