package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevType;
import mod.bluestaggo.modernerbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModernBetaGraphicalChunkSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    public ModernBetaGraphicalChunkSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "chunk", settings, onDone);
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        String chunkProvider = this.settings.getString(NbtTags.CHUNK_PROVIDER);
        boolean isNoiseProvider = ModernBetaBuiltInTypes.Chunk.CHUNK_PROVIDER_NOISE.contains(chunkProvider);
        boolean isForcedHeightProvider = ModernBetaBuiltInTypes.Chunk.CHUNK_PROVIDER_FORCED_HEIGHT.contains(chunkProvider);
        boolean isFiniteProvider = ModernBetaBuiltInTypes.Chunk.CHUNK_PROVIDER_FINITE.contains(chunkProvider);
        boolean isIndevProvider = ModernBetaBuiltInTypes.Chunk.INDEV.id.equals(chunkProvider);

        int minY = -64;
        int maxY = 320;
        int seaLevel = switch (settings.getString(chunkProvider)) {
            case "early_release", "major_release" -> 63;
            default -> 64;
        };

        list.addSingleOptionEntry(this.primarySelectionOption(NbtTags.CHUNK_PROVIDER,
            Arrays.stream(ModernBetaBuiltInTypes.Chunk.values())
                .map(chunk -> chunk.id)
                .toArray(String[]::new)));

        if (isFiniteProvider) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.level").formatted(Formatting.BOLD)));

            var levelOptionList = new ArrayList<SimpleOption<?>>();

            if (isIndevProvider) {
                levelOptionList.add(this.selectionOption(NbtTags.INDEV_LEVEL_TYPE,
                    Arrays.stream(IndevType.values())
                        .map(IndevType::getId)
                        .toArray(String[]::new)));
                levelOptionList.add(this.selectionOption(NbtTags.INDEV_LEVEL_THEME,
                    Arrays.stream(IndevTheme.values())
                        .map(IndevTheme::getId)
                        .toArray(String[]::new)));
            }

            levelOptionList.addAll(List.of(
                this.intRangeOption(NbtTags.INDEV_LEVEL_WIDTH, 64, 1024, 64),
                this.intRangeOption(NbtTags.INDEV_LEVEL_LENGTH, 64, 1024, 64),
                this.intRangeOption(NbtTags.INDEV_LEVEL_HEIGHT, 64, maxY - minY, 16)
            ));

            list.addAll(levelOptionList.toArray(SimpleOption[]::new));
        }

        list.addSingleOptionEntry(this.headerOption(this.getText("header.generic").formatted(Formatting.BOLD)));

        list.addAll(
            this.booleanOption(NbtTags.USE_DEEPSLATE),
            this.intRangeOption(NbtTags.DEEPSLATE_MIN_Y, minY, maxY),
            this.intRangeOption(NbtTags.DEEPSLATE_MAX_Y, minY, maxY),
            this.booleanOption(NbtTags.USE_SURFACE_RULES),
            this.intRangeOption(
                NbtTags.SEA_LEVEL_OFFSET,
                new SimpleOption.ValidatingIntSliderCallbacks(minY - seaLevel, maxY - seaLevel),
                (optionText, value) -> GameOptions.getGenericValueText(this.getText(NbtTags.SEA_LEVEL_OFFSET), value + seaLevel)
            )
        );
        list.addAll(this.headerOption(this.getText(NbtTags.DEEPSLATE_BLOCK)), this.blockOption(NbtTags.DEEPSLATE_BLOCK));

        var structureOptionList = new ArrayList<SimpleOption<?>>();

        if (ModernBetaBuiltInTypes.Chunk.INFDEV_227.id.equals(chunkProvider)) {
            structureOptionList.add(this.booleanOption(NbtTags.INFDEV_USE_PYRAMID));
            structureOptionList.add(this.booleanOption(NbtTags.INFDEV_USE_WALL));
        }

        if (isIndevProvider) {
            structureOptionList.add(this.booleanOption(NbtTags.INDEV_SPAWN_HOUSE));
        }

        if (!structureOptionList.isEmpty()) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.structure").formatted(Formatting.BOLD)));
            list.addAll(structureOptionList.toArray(SimpleOption[]::new));
        }

        list.addSingleOptionEntry(this.headerOption(this.getText("header.cave").formatted(Formatting.BOLD)));
        list.addAll(
            isFiniteProvider ? new SimpleOption[] {
                this.booleanOption(NbtTags.INDEV_USE_CAVES),
                this.intRangeOption(NbtTags.INDEV_CAVE_RARITY, 1024, 40960, 1024),
                this.floatRangeOption(NbtTags.INDEV_CAVE_RADIUS, 0.01F, 5.0F),
            }
            : new SimpleOption[] {
                this.booleanOption(NbtTags.USE_CAVES),
                this.booleanOption(NbtTags.USE_FIXED_CAVES),
                this.booleanOption(NbtTags.FORCE_BETA_CAVES),
            }
        );

        if (isNoiseProvider) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.noise").formatted(Formatting.BOLD)));

            if (isForcedHeightProvider) {
                list.addSingleOptionEntry(this.mapEditButton(
                    this.getText(NbtTags.RELEASE_HEIGHT_OVERRIDES),
                    NbtTags.RELEASE_HEIGHT_OVERRIDES,
                    BiomeInfoToHeightConfigMapScreen::new
                ));
            }

            var noiseOptionList = new ArrayList<SimpleOption<?>>(List.of(
                this.floatRangeOption(NbtTags.NOISE_MAIN_NOISE_SCALE_X, 1.0F, 5000.0F),
                this.floatRangeOption(NbtTags.NOISE_MAIN_NOISE_SCALE_Y, 1.0F, 5000.0F),
                this.floatRangeOption(NbtTags.NOISE_MAIN_NOISE_SCALE_Z, 1.0F, 5000.0F),
                this.floatRangeOption(NbtTags.NOISE_DEPTH_NOISE_SCALE_X, 1.0F, 2000.0F),
                this.floatRangeOption(NbtTags.NOISE_DEPTH_NOISE_SCALE_Z, 1.0F, 2000.0F),
                this.floatRangeOption(NbtTags.NOISE_BASE_SIZE, 1.0F, 25.0F),
                this.floatRangeOption(NbtTags.NOISE_COORDINATE_SCALE, 1.0F, 6000.0F),
                this.floatRangeOption(NbtTags.NOISE_HEIGHT_SCALE, 1.0F, 6000.0F),
                this.floatRangeOption(NbtTags.NOISE_STRETCH_Y, 0.01F, 50.0F),
                this.floatRangeOption(NbtTags.NOISE_UPPER_LIMIT_SCALE, 1.0F, 5000.0F),
                this.floatRangeOption(NbtTags.NOISE_LOWER_LIMIT_SCALE, 1.0F, 5000.0F),
                this.intRangeOption(NbtTags.NOISE_TOP_SLIDE_TARGET, -50, 0),
                this.intRangeOption(NbtTags.NOISE_TOP_SLIDE_SIZE, 1, 50),
                this.intRangeOption(NbtTags.NOISE_TOP_SLIDE_OFFSET, -10, 10),
                this.intRangeOption(NbtTags.NOISE_BOTTOM_SLIDE_TARGET, 0, 50),
                this.intRangeOption(NbtTags.NOISE_BOTTOM_SLIDE_SIZE, 1, 50),
                this.intRangeOption(NbtTags.NOISE_BOTTOM_SLIDE_OFFSET, -10, 10)
            ));

            if (isForcedHeightProvider) {
                noiseOptionList.addAll(List.of(
                    this.floatRangeOption(NbtTags.RELEASE_BIOME_DEPTH_WEIGHT, 1.0F, 20.0F),
                    this.floatRangeOption(NbtTags.RELEASE_BIOME_DEPTH_OFFSET, 0.0F, 20.0F),
                    this.floatRangeOption(NbtTags.RELEASE_BIOME_SCALE_WEIGHT, 1.0F, 20.0F),
                    this.floatRangeOption(NbtTags.RELEASE_BIOME_SCALE_OFFSET, 0.0F, 20.0F)
                ));
            }

            list.addAll(noiseOptionList.toArray(SimpleOption[]::new));
        }

        if (isFiniteProvider) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.feature").formatted(Formatting.BOLD)));
            list.addAll(
                this.floatRangeOption(NbtTags.INDEV_SAND_BEACH_THRESHOLD, -32.0F, 32.0F),
                this.booleanOption(NbtTags.INDEV_SAND_BEACH_UNDER_AIR),
                this.booleanOption(NbtTags.INDEV_SAND_BEACH_UNDER_FLUID),
                this.floatRangeOption(NbtTags.INDEV_GRAVEL_BEACH_THRESHOLD, -32.0F, 32.0F),
                this.booleanOption(NbtTags.INDEV_GRAVEL_BEACH_UNDER_AIR),
                this.booleanOption(NbtTags.INDEV_GRAVEL_BEACH_UNDER_FLUID),
                this.intRangeOption(NbtTags.INDEV_WATER_RARITY, 1000, 50000, 1000),
                this.intRangeOption(NbtTags.INDEV_LAVA_RARITY, 1000, 50000, 1000)
            );

            list.addSingleOptionEntry(this.headerOption(this.getText("header.noise").formatted(Formatting.BOLD)));
            list.addAll(
                this.floatRangeOption(NbtTags.INDEV_NOISE_SCALE, 0.01F, 10.0F),
                this.floatRangeOption(NbtTags.INDEV_SELECTOR_SCALE, 0.01F, 10.0F),
                this.floatRangeOption(NbtTags.INDEV_MIN_HEIGHT_DAMP, 0.01F, 25.0F),
                this.floatRangeOption(NbtTags.INDEV_MIN_HEIGHT_BOOST, -50.0F, 50.0F),
                this.floatRangeOption(NbtTags.INDEV_MAX_HEIGHT_DAMP, 0.01F, 25.0F),
                this.floatRangeOption(NbtTags.INDEV_MAX_HEIGHT_BOOST, -50.0F, 50.0F),
                this.floatRangeOption(NbtTags.INDEV_HEIGHT_UNDER_DAMP, 0.01F, 5.0F)
            );
        }

        if (isNoiseProvider) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.isles").formatted(Formatting.BOLD)));
            list.addAll(
                this.booleanOption(NbtTags.ISLES_USE_ISLANDS),
                this.booleanOption(NbtTags.ISLES_USE_OUTER_ISLANDS),
                this.floatRangeOption(NbtTags.ISLES_OCEAN_SLIDE_TARGET, -1000.0F, 0.0F),
                this.selectionOption(NbtTags.ISLES_CENTER_ISLAND_SHAPE,
                    Arrays.stream(IslandShape.values())
                        .map(IslandShape::getId)
                        .toArray(String[]::new)),
                this.intRangeOption(NbtTags.ISLES_CENTER_ISLAND_RADIUS, 1, 100),
                this.intRangeOption(NbtTags.ISLES_CENTER_ISLAND_FALLOFF_DIST, 1, 100),
                this.intRangeOption(NbtTags.ISLES_CENTER_OCEAN_RADIUS, 1, 100),
                this.intRangeOption(NbtTags.ISLES_CENTER_OCEAN_FALLOFF_DIST, 1, 100),
                this.floatRangeOption(NbtTags.ISLES_OUTER_ISLAND_NOISE_SCALE, 0.01F, 5000.0F),
                this.floatRangeOption(NbtTags.ISLES_OUTER_ISLAND_NOISE_OFFSET, -1.0F, 1.0F)
            );
        }
    }

    private static class BiomeInfoToHeightConfigMapScreen extends ModernBetaGraphicalMapSettingsScreen {
        public BiomeInfoToHeightConfigMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            ArrayList<SimpleOption<?>> options = new ArrayList<>();
            options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
            options.add(null);
            options.addAll(this.biomeInfoOption(KEY + i));
            options.addAll(this.heightConfigOption(VALUE + i));
            return options;
        }

        @Override
        protected String getDefaultKey() {
            return BiomeKeys.PLAINS.getValue().toString();
        }

        @Override
        protected NbtElement getDefaultValue() {
            return NbtString.of(HeightConfig.DEFAULT.makeString());
        }
    }
}
