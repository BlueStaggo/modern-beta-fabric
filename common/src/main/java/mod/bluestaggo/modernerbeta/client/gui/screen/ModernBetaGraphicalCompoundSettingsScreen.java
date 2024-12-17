package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.mojang.serialization.Codec;
import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public abstract class ModernBetaGraphicalCompoundSettingsScreen extends ModernBetaGraphicalSettingsScreen<NbtCompound> {
    public ModernBetaGraphicalCompoundSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        String type,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, type, settings, onDone);
    }

    protected Pair<NbtCompound, String> resolveSettings(String key) {
        String[] subKeys = key.split("\\.");
        var defaultPair = new Pair<>(this.settings, key);
        if (subKeys.length <= 1) {
            return defaultPair;
        }

        NbtElement element = this.settings;
        for (int i = 0; i < subKeys.length - 1; i++) {
            if (element instanceof NbtList list) {
                try {
                    element = list.get(Integer.parseInt(subKeys[i]));
                } catch (NumberFormatException ignored) {
                    return defaultPair;
                }
            } else if (element instanceof NbtCompound compound) {
                element = compound.get(subKeys[i]);
            } else {
                return defaultPair;
            }
        }

        if (element instanceof NbtCompound compound) {
            return new Pair<>(compound, subKeys[subKeys.length - 1]);
        }
        return defaultPair;
    }

    protected SimpleOption<String> primarySelectionOption(String key, String... options) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> this.getText(key, value),
            new SimpleOption.LazyCyclingCallbacks<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                Codec.STRING
            ),
            settings.getString(subKey),
            value -> {
                settings.putString(subKey, value);
                this.clearAndInit();
            }
        );
    }

    protected SimpleOption<String> selectionOption(String key, String... options) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> this.getText(key, value),
            new SimpleOption.LazyCyclingCallbacks<>(
                () -> Arrays.stream(options).toList(),
                value -> Arrays.stream(options).filter(value::equals).findFirst(),
                Codec.STRING
            ),
            settings.getString(subKey),
            value -> settings.putString(subKey, value)
        );
    }

    protected SimpleOption<Boolean> booleanOption(String key) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return SimpleOption.ofBoolean(
            this.getTextKey(key),
            settings.getBoolean(subKey),
            value -> settings.putBoolean(subKey, value)
        );
    }

    protected SimpleOption<Integer> intRangeOption(String key, int min, int max) {
        return this.intRangeOption(key, new SimpleOption.ValidatingIntSliderCallbacks(min, max), (optionText, value) -> GameOptions.getGenericValueText(this.getText(key), value));
    }

    protected SimpleOption<Integer> intRangeOption(String key, int min, int max, int multiple) {
        return this.intRangeOption(key, new ValidatingIntMultipleSliderCallbacks(min, max, multiple), (optionText, value) -> GameOptions.getGenericValueText(this.getText(key), value));
    }

    protected SimpleOption<Integer> intRangeOption(String key, SimpleOption.IntSliderCallbacks intSliderCallbacks) {
        return this.intRangeOption(key, intSliderCallbacks, (optionText, value) -> GameOptions.getGenericValueText(this.getText(key), value));
    }

    protected SimpleOption<Integer> intRangeOption(String key, SimpleOption.IntSliderCallbacks intSliderCallbacks, SimpleOption.ValueTextGetter<Integer> valueTextGetter) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            valueTextGetter,
            intSliderCallbacks,
            settings.getInt(subKey),
            value -> settings.putInt(subKey, value)
        );
    }

    protected SimpleOption<Integer> intFieldOption(String key) {
        return this.intFieldOption(key, "");
    }

    protected SimpleOption<Integer> intFieldOption(String key, String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(Integer.toString(settings.getInt(subKey))),
            new IntegerFieldCallbacks(prefix + ": "),
            settings.getInt(subKey),
            value -> settings.putInt(subKey, value)
        );
    }

    protected SimpleOption<Integer> intFieldOptionFromString(String key, String prefix) {
        if (prefix == null) {
            prefix = "";
        }

        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        int defaultValue = 0;
        try {
            defaultValue = Integer.parseInt(settings.getString(subKey));
        } catch (NumberFormatException ignored) {
        }

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(settings.getString(subKey)),
            new IntegerFieldCallbacks(prefix + ": "),
            defaultValue,
            value -> settings.putString(subKey, Integer.toString(value))
        );
    }

    protected SimpleOption<Float> floatRangeOption(String key, float min, float max) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> GameOptions.getGenericValueText(this.getText(key), Text.literal("%.3f".formatted(value))),
            new FloatSliderCallbacks(min, max),
            settings.getFloat(subKey),
            value -> settings.putFloat(subKey, value)
        );
    }

    protected SimpleOption<String> stringOption(String key) {
        return this.stringOption(key, TextFieldCallbacks.NO_VALIDATION);
    }

    protected SimpleOption<String> stringOption(String key, TextFieldCallbacks callbacks) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(settings.getString(subKey)),
            callbacks,
            settings.getString(subKey),
            value -> settings.putString(subKey, value)
        );
    }

    protected SimpleOption<String> blockOption(String key) {
        return this.stringOption(key, new TextFieldCallbacks(
            value -> Registries.BLOCK.containsId(Identifier.tryParse(value)),
            value -> Identifier.validate(value).isSuccess()
        ));
    }

    protected SimpleOption<String> biomeOption(String key, boolean allowNone) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return new SimpleOption<>(
            this.getTextKey(key),
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.of(settings.getString(subKey)),
            new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
            settings.getString(subKey),
            value -> {
                settings.putString(subKey, value);
                this.clearAndInit();
            }
        );
    }

    protected List<SimpleOption<?>> biomeInfoOption(String key, boolean allowNone) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return List.of(
            new SimpleOption<>(
                "",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> Text.of(settings.getString(subKey)),
                new IntegerFieldCallbacks(Text.translatable("createWorld.customize.modern_beta.settings.biomeInfo.type").getString() + ": "),
                BiomeInfo.parse(settings.getString(subKey)).getRight(),
                value -> {
                    NbtElement replacedElement = settings.get(subKey);
                    String replacedString = replacedElement != null ? replacedElement.asString() : "";
                    String replacedBiome = BiomeInfo.parse(replacedString).getLeft();
                    settings.putString(subKey, BiomeInfo.makeString(replacedBiome, value));
                }
            ),
            new SimpleOption<>(
                "",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> Text.of(settings.getString(subKey)),
                new BiomePickerCallbacks(this.client::setScreen, this, this.generatorOptionsHolder, allowNone),
                BiomeInfo.parse(settings.getString(subKey)).getLeft(),
                value -> {
                    NbtElement replacedElement = settings.get(subKey);
                    String replacedString = replacedElement != null ? replacedElement.asString() : "";
                    int replacedType = BiomeInfo.parse(replacedString).getRight();
                    settings.putString(subKey, BiomeInfo.makeString(value, replacedType));
                    this.clearAndInit();
                }
            )
        );
    }

    protected List<SimpleOption<?>> heightConfigOption(String key) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return List.of(
            new SimpleOption<>(
                "createWorld.customize.modern_beta.settings.heightConfig.depth",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> GameOptions.getGenericValueText(
                    Text.translatable("createWorld.customize.modern_beta.settings.heightConfig.depth"),
                    Text.literal(String.format("%.2f", HeightConfig.parse(settings.getString(subKey), HeightConfig.DEFAULT).depth()))
                ),
                new FloatSliderCallbacks(-2.0F, 2.0F),
                HeightConfig.parse(settings.getString(subKey), HeightConfig.DEFAULT).depth(),
                value -> {
                    NbtElement replacedElement = settings.get(subKey);
                    String replacedString = replacedElement != null ? replacedElement.asString() : "";
                    float replacedScale = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).scale();
                    settings.putString(subKey, HeightConfig.makeString(MathHelper.floor(value * 100.0F) / 100.0F, replacedScale));
                }
            ),
            new SimpleOption<>(
                "createWorld.customize.modern_beta.settings.heightConfig.scale",
                SimpleOption.emptyTooltip(),
                (optionText, value) -> GameOptions.getGenericValueText(
                    Text.translatable("createWorld.customize.modern_beta.settings.heightConfig.scale"),
                    Text.literal(String.format("%.2f", HeightConfig.parse(settings.getString(subKey), HeightConfig.DEFAULT).scale()))
                ),
                new FloatSliderCallbacks(0.0F, 5.0F),
                HeightConfig.parse(settings.getString(subKey), HeightConfig.DEFAULT).scale(),
                value -> {
                    NbtElement replacedElement = settings.get(subKey);
                    String replacedString = replacedElement != null ? replacedElement.asString() : "";
                    float replacedDepth = HeightConfig.parse(replacedString, HeightConfig.DEFAULT).depth();
                    settings.putString(subKey, HeightConfig.makeString(replacedDepth, MathHelper.floor(value * 100.0F) / 100.0F));
                }
            )
        );
    }

    protected SimpleOption<Void> listEditButton(
        Text text, String key, int type,
        ModernBetaGraphicalListSettingsScreen.Constructor listSettingsScreenConstructor
    ) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return this.customButton(
            Text.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.client.setScreen(listSettingsScreenConstructor.create(
                Text.translatable(STRING_PREFIX + "list.title", text.getString()).getString(),
                this,
                this.generatorOptionsHolder,
                settings.getList(subKey, type).copy(),
                list -> settings.put(subKey, list)
            ))
        );
    }

    protected SimpleOption<Void> mapEditButton(
        Text text, String key,
        ModernBetaGraphicalMapSettingsScreen.Constructor mapSettingsScreenConstructor
    ) {
        Pair<NbtCompound, String> resolvedSettings = this.resolveSettings(key);
        NbtCompound settings = resolvedSettings.getLeft();
        String subKey = resolvedSettings.getRight();

        return this.customButton(
            Text.translatable(STRING_PREFIX + "list.button", text.getString()),
            () -> this.client.setScreen(mapSettingsScreenConstructor.create(
                Text.translatable(STRING_PREFIX + "list.titleMap", text.getString()).getString(),
                this,
                this.generatorOptionsHolder,
                settings.getCompound(subKey),
                compound -> settings.put(subKey, compound)
            ))
        );
    }
}
