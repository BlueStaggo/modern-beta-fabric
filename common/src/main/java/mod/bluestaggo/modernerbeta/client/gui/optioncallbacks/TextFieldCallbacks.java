package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public record TextFieldCallbacks(Predicate<String> validationFunction, Predicate<String> inputValidationFunction) implements SimpleOption.Callbacks<String> {
    public static final TextFieldCallbacks NO_VALIDATION = new TextFieldCallbacks(Objects::nonNull, Objects::nonNull);

    public TextFieldCallbacks(Predicate<String> validationFunction) {
        this(validationFunction, Objects::nonNull);
    }

    @Override
    public Function<SimpleOption<String>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<String> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<String> changeCallback) {
        return option -> {
            var widget = new TextFieldWidget(
                MinecraftClient.getInstance().advanceValidatingTextRenderer,
                x, y, width, 20, Text.of(option.toString())
            );
            widget.setMaxLength(256);
            widget.setChangedListener(option::setValue);
            widget.setTextPredicate(inputValidationFunction);
            widget.setText(option.getValue());
            return widget;
        };
    }

    @Override
    public Optional<String> validate(String value) {
        return validationFunction.test(value) ? Optional.of(value) : Optional.empty();
    }

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }
}
