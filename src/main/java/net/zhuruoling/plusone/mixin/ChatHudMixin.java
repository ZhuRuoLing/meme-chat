package net.zhuruoling.plusone.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    private final Pattern pattern = Pattern.compile("<[A-Za-z0-9_]+> (.+)");

    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    protected abstract int getMessageLineIndex(double chatLineX, double chatLineY);

    @Shadow
    public abstract @Nullable Style getTextStyleAt(double x, double y);

    @Shadow
    protected abstract double toChatLineX(double x);

    @Shadow
    protected abstract double toChatLineY(double y);

    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    void inj(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {
        var style = this.getTextStyleAt(mouseX, mouseY);
        if (style == null || style.getClickEvent() == null) {
            var index = this.getMessageLineIndex(toChatLineX(mouseX), toChatLineY(mouseY));
            if (index != -1) {
                var textLine = this.visibleMessages.get(index).content();
                StringBuilder s = new StringBuilder();
                textLine.accept(((index1, style1, codePoint) -> {
                    s.append((char) codePoint);
                    return true;
                }));
                Screen screen = MinecraftClient.getInstance().currentScreen;
                String str = s.toString();
                if (screen instanceof ChatScreen) {
                    var matcher = pattern.matcher(str);
                    if (matcher.matches()) {
                        ((ChatScreen) screen).chatField.setText(matcher.group(1));
                    }
                }
            }
        }
    }
}

