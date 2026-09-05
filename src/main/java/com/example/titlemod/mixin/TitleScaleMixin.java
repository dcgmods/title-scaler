package com.example.titlemod.mixin;

import com.example.titlemod.config.TitleConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class TitleScaleMixin {

    @Unique
    private static TitleConfig config;

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"
        )
    )
    private int scaleTitleText(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (config == null) {
            config = TitleConfig.load();
        }

        float scale = config.titleScale;
        if (scale <= 0.0f) scale = 1.0f;

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, 1.0f);

        int adjustedX = (int) (x / scale);
        int adjustedY = (int) (y / scale);

        int result = context.drawTextWithShadow(textRenderer, text, adjustedX, adjustedY, color);

        context.getMatrices().pop();
        return result;
    }
}
