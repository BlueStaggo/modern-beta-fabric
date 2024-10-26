package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record CustomButtonCallbacks(Text text, Runnable onPress) implements SimpleOption.Callbacks<Void> {
    @Override
    public Function<SimpleOption<Void>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<Void> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<Void> changeCallback) {
        return option ->
            ButtonWidget.builder(text, onPress -> this.onPress.run())
            .dimensions(x, y, width, 20).build();
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
