package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public record IntegerFieldCallbacks(String prefix) implements SimpleOption.Callbacks<Integer> {
    @Override
    public Function<SimpleOption<Integer>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Integer> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Integer> changeCallback) {
        return option -> {
            var widget = new TextFieldWidget(
                MinecraftClient.getInstance().advanceValidatingTextRenderer,
                x, y, width, 20, Text.of(option.toString())
            );
            widget.setMaxLength(256);
            widget.setChangedListener(value -> {
                if (value.length() <= this.prefix.length()
                        || value.length() == this.prefix.length() + 1 && value.charAt(this.prefix.length()) == '-') {
                    option.setValue(0);
                } else {
                    try {
                        option.setValue(Integer.parseInt(value.substring(this.prefix.length())));
                    } catch (NumberFormatException ignored) {
                    }
                }
            });
            widget.setTextPredicate(string ->
                string.startsWith(this.prefix) && string.substring(this.prefix.length()).matches("-?[0-9]*"));
            widget.setText(this.prefix + option.getValue());
            return widget;
        };
    }

    @Override
    public Optional<Integer> validate(Integer value) {
        return Optional.of(value);
    }

    @Override
    public Codec<Integer> codec() {
        return Codec.INT;
    }
}
