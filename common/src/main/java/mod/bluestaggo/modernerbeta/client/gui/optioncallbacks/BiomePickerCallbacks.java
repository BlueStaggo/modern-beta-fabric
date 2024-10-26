package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.screen.world.CustomizeBuffetLevelScreen;
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

public record BiomePickerCallbacks(Consumer<Screen> screenChangeHandler, Screen parentScreen, GeneratorOptionsHolder generatorOptionsHolder) implements SimpleOption.Callbacks<String> {
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
                    ? Text.translatable("createWorld.customize.modern_beta.settings.emptyBiome").formatted(Formatting.ITALIC)
                    : Language.getInstance().hasTranslation(biomeTranslationKey)
                        ? Text.translatable(biomeTranslationKey)
                        : Text.literal(biomeIdentifier.toString()),
                onPress -> {
                    screenChangeHandler.accept(new CustomizeBuffetLevelScreen(
                        parentScreen,
                        generatorOptionsHolder,
                        biome -> {
                            if (biome != null) {
                                RegistryKey<Biome> key = biome.getKey().orElse(BiomeKeys.PLAINS);
                                if (BiomeKeys.THE_VOID.equals(key)) {
                                    option.setValue("");
                                } else {
                                    option.setValue(key.getValue().toString());
                                }
                            }
                        }
                    ));
                }
            ).dimensions(x, y, width, 20).build();
        };
    }

    @Override
    public Optional<String> validate(String value) {
        return "".equals(value) || generatorOptionsHolder.getCombinedRegistryManager()
            .get(RegistryKeys.BIOME).containsId(Identifier.tryParse(value))
            ? Optional.of(value) : Optional.empty();
    }

    @Override
    public Codec<String> codec() {
        return Codec.STRING;
    }
}
