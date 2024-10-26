package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.mixin.client.AccessorTextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record TextLabelCallbacks(Text text, float alignment) implements SimpleOption.Callbacks<Void> {
    public TextLabelCallbacks(Text text) {
        this(text, 0.5F);
    }

    @Override
    public Function<SimpleOption<Void>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Void> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Void> changeCallback) {
        return option -> {
            TextWidget textWidget = new TextWidget(x, y, width, 20, text, MinecraftClient.getInstance().advanceValidatingTextRenderer);
            ((AccessorTextWidget)textWidget).invokeAlign(alignment);
            return textWidget;
        };
    }

    @Override
    public Optional<Void> validate(Void value) {
        return Optional.empty();
    }

    @Override
    public Codec<Void> codec() {
        return null;
    }
}
