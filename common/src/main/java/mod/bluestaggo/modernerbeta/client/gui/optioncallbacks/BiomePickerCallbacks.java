package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaSelectBiomeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record BiomePickerCallbacks(Consumer<Screen> screenChangeHandler, Screen parentScreen, GeneratorOptionsHolder generatorOptionsHolder, boolean allowNone) implements SimpleOption.Callbacks<String> {
    @Override
    public Function<SimpleOption<String>, ClickableWidget> getWidgetCreator(SimpleOption.TooltipFactory<String> tooltipFactory, GameOptions gameOptions, int x, int y, int width, Consumer<String> changeCallback) {
        return option -> {
            Identifier biomeIdentifier = Identifier.tryParse(option.getValue());
            if (biomeIdentifier == null) {
                biomeIdentifier = BiomeKeys.PLAINS.getValue();
            }
            String biomeTranslationKey = biomeIdentifier.toTranslationKey("biome");

            return ButtonWidget.builder(
                "".equals(option.getValue())
                    ? Text.translatable("gui.none").formatted(Formatting.ITALIC)
                    : Language.getInstance().hasTranslation(biomeTranslationKey)
                        ? Text.translatable(biomeTranslationKey)
                        : Text.literal(biomeIdentifier.toString()),
                onPress -> {
                    screenChangeHandler.accept(new ModernBetaSelectBiomeScreen(
                        parentScreen,
                        generatorOptionsHolder,
                        biome -> {
                            if (biome != null) {
                                RegistryKey<Biome> key = biome.getKey().orElse(BiomeKeys.PLAINS);
                                option.setValue(key.getValue().toString());
                            } else {
                                if (allowNone) {
                                    option.setValue("");
                                } else {
                                    option.setValue(BiomeKeys.PLAINS.getValue().toString());
                                }
                            }
                        },
                        allowNone
                    ));
                }
            ).dimensions(x, y, width, 20).build();
        };
    }

    @Override
    public Optional<String> validate(String value) {
        return (allowNone && "".equals(value)) || generatorOptionsHolder.getCombinedRegistryManager()
            .getOrThrow(RegistryKeys.BIOME).containsId(Identifier.tryParse(value))
            ? Optional.of(value) : Optional.empty();
    }

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }
}
