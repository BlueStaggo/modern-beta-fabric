package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.util.NbtCompoundBuilder;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.FractalSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModernBetaGraphicalBiomeSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    private static final String[] CLIMATE_MAPPINGS = {
        "desert", "forest", "ice_desert", "plains", "rainforest", "savanna", "shrubland", "seasonal_forest",
        "swampland", "taiga", "tundra"
    };

    public ModernBetaGraphicalBiomeSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "biome", settings, onDone);
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        String biomeProvider = this.settings.getString(NbtTags.BIOME_PROVIDER);
        boolean usesNoise = ModernBetaBuiltInTypes.Biome.BIOME_PROVIDER_USES_NOISE.contains(biomeProvider);
        boolean isVoronoiProvider = ModernBetaBuiltInTypes.Biome.VORONOI.id.equals(biomeProvider);

        list.addSingleOptionEntry(this.primarySelectionOption(NbtTags.BIOME_PROVIDER,
            Arrays.stream(ModernBetaBuiltInTypes.Biome.values())
                .map(chunk -> chunk.id)
                .toArray(String[]::new)));

        if (ModernBetaBuiltInTypes.Biome.SINGLE.id.equals(biomeProvider)) {
            list.addOptionEntry(this.headerOption(this.getText(NbtTags.SINGLE_BIOME)), this.biomeOption(NbtTags.SINGLE_BIOME));
        } else if (usesNoise) {
            var noiseOptionList = new ArrayList<SimpleOption<?>>(List.of(
                this.booleanOption(NbtTags.USE_OCEAN_BIOMES),
                this.floatRangeOption(NbtTags.CLIMATE_TEMP_NOISE_SCALE, 0.001F, 1.0F),
                this.floatRangeOption(NbtTags.CLIMATE_RAIN_NOISE_SCALE, 0.001F, 1.0F),
                this.floatRangeOption(NbtTags.CLIMATE_DETAIL_NOISE_SCALE, 0.001F, 1.0F)
            ));

            if (isVoronoiProvider) {
                noiseOptionList.add(this.floatRangeOption(NbtTags.CLIMATE_WEIRD_NOISE_SCALE, 0.001F, 1.0F));
            }

            list.addAll(noiseOptionList.toArray(SimpleOption[]::new));

            if (isVoronoiProvider) {
                list.addSingleOptionEntry(this.listEditButton(
                    this.getText(NbtTags.VORONOI_POINTS),
                    NbtTags.VORONOI_POINTS,
                    NbtElement.COMPOUND_TYPE,
                    VoronoiPointBiomeListScreen::new
                ));
            } else {
                for (String climateMapping : CLIMATE_MAPPINGS) {
                    String prefix = NbtTags.CLIMATE_MAPPINGS + ".";
                    list.addSingleOptionEntry(this.headerOption(this.getText(prefix + climateMapping).formatted(Formatting.BOLD)));
                    list.addAll(new SimpleOption[]{
                        this.headerOption(this.getText(prefix + NbtTags.BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.BIOME),
                        this.headerOption(this.getText(prefix + NbtTags.OCEAN_BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.OCEAN_BIOME),
                        this.headerOption(this.getText(prefix + NbtTags.DEEP_OCEAN_BIOME)),
                        this.biomeOption(prefix + climateMapping + "." + NbtTags.DEEP_OCEAN_BIOME)
                    });
                }
            }
        } else if (ModernBetaBuiltInTypes.Biome.FRACTAL.id.equals(biomeProvider)) {
            list.addSingleOptionEntry(this.headerOption(this.getText("header.scale").formatted(Formatting.BOLD)));
            list.addAll(new SimpleOption[]{
                this.intRangeOption(NbtTags.FRACTAL_BIOME_SCALE, 1, 10),
                this.intRangeOption(NbtTags.FRACTAL_HILL_SCALE, 1, 10),
                this.intRangeOption(NbtTags.FRACTAL_BEACH_SHRINK, -5, 5),
                this.intRangeOption(NbtTags.FRACTAL_OCEAN_SHRINK, -5, 5),
                this.selectionOption(NbtTags.FRACTAL_TERRAIN_TYPE,
                    Arrays.stream(FractalSettings.TerrainType.values())
                        .map(FractalSettings.TerrainType::getId)
                        .toArray(String[]::new))
            });

            list.addSingleOptionEntry(this.headerOption(this.getText("header.features").formatted(Formatting.BOLD)));
            list.addAll(new SimpleOption[] {
                this.booleanOption(NbtTags.FRACTAL_OCEANS),
                this.booleanOption(NbtTags.FRACTAL_ADD_RIVERS),
                this.booleanOption(NbtTags.FRACTAL_ADD_BEACHES),
                this.booleanOption(NbtTags.FRACTAL_ADD_MUSHROOM_ISLANDS),
                this.booleanOption(NbtTags.FRACTAL_ADD_SWAMP_RIVERS),
                this.booleanOption(NbtTags.FRACTAL_ADD_DEEP_OCEANS),
                this.booleanOption(NbtTags.FRACTAL_ADD_SNOW),
                this.booleanOption(NbtTags.FRACTAL_ADD_HILLS),
                this.booleanOption(NbtTags.FRACTAL_ADD_MUTATIONS),
            });

            list.addSingleOptionEntry(this.headerOption(this.getText("header.biomeLists").formatted(Formatting.BOLD)));
            list.addAll(new SimpleOption[] {
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_BIOMES),
                    NbtTags.FRACTAL_BIOMES,
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.mapEditButton(
                    this.getText(NbtTags.FRACTAL_HILL_VARIANTS),
                    NbtTags.FRACTAL_HILL_VARIANTS,
                    BiomeInfoToBiomeInfoMapScreen::new
                ),
                this.mapEditButton(
                    this.getText(NbtTags.FRACTAL_EDGE_VARIANTS),
                    NbtTags.FRACTAL_EDGE_VARIANTS,
                    BiomeInfoToBiomeInfoMapScreen::new
                ),
                this.mapEditButton(
                    this.getText(NbtTags.FRACTAL_MUTATED_VARIANTS),
                    NbtTags.FRACTAL_MUTATED_VARIANTS,
                    BiomeInfoToBiomeInfoMapScreen::new
                ),
                this.mapEditButton(
                    this.getText(NbtTags.FRACTAL_VERY_RARE_VARIANTS),
                    NbtTags.FRACTAL_VERY_RARE_VARIANTS,
                    BiomeInfoToBiomeInfoMapScreen::new
                ),
                this.mapEditButton(
                    this.getText(NbtTags.FRACTAL_SUB_VARIANTS),
                    NbtTags.FRACTAL_SUB_VARIANTS,
                    IntegerToMapMapScreen::new
                ),
            });

            list.addSingleOptionEntry(this.headerOption(this.getText("header.climaticBiomes").formatted(Formatting.BOLD)));
            list.addAll(new SimpleOption[] {
                this.booleanOption(NbtTags.FRACTAL_USE_CLIMATIC_BIOMES),
                this.booleanOption(NbtTags.FRACTAL_ADD_CLIMATIC_OCEANS),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".hot"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".0.normal",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".hot.rare"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".0.rare",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".temperate"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".1.normal",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".temperate.rare"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".1.rare",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".cool"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".2.normal",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".cool.rare"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".2.rare",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".snowy"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".3.normal",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
                this.listEditButton(
                    this.getText(NbtTags.FRACTAL_CLIMATIC_BIOMES + ".snowy.rare"),
                    NbtTags.FRACTAL_CLIMATIC_BIOMES + ".3.rare",
                    NbtElement.STRING_TYPE,
                    BiomeInfoListScreen::new
                ),
            });

            list.addSingleOptionEntry(this.headerOption(this.getText("header.individualBiomes").formatted(Formatting.BOLD)));
            list.addAll(new SimpleOption[] {
                this.headerOption(this.getText(NbtTags.FRACTAL_PLAINS)),
                this.biomeOption(NbtTags.FRACTAL_PLAINS),
                this.headerOption(this.getText(NbtTags.FRACTAL_ICE_PLAINS)),
                this.biomeOption(NbtTags.FRACTAL_ICE_PLAINS),
            });
        }
    }

    private static class BiomeInfoListScreen extends ModernBetaGraphicalListSettingsScreen {
        public BiomeInfoListScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtList settings, Consumer<NbtList> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            return this.biomeInfoOption(i);
        }

        @Override
        protected NbtElement getDefaultElement() {
            return NbtString.of(BiomeKeys.PLAINS.getValue().toString());
        }
    }

    private static class BiomeInfoToBiomeInfoMapScreen extends ModernBetaGraphicalMapSettingsScreen {
        public BiomeInfoToBiomeInfoMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            ArrayList<SimpleOption<?>> options = new ArrayList<>();
            options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
            options.add(null);
            options.addAll(this.biomeInfoOption(KEY + i));
            options.addAll(this.biomeInfoOption(VALUE + i));
            return options;
        }

        @Override
        protected String getDefaultKey() {
            return BiomeKeys.PLAINS.getValue().toString();
        }

        @Override
        protected NbtElement getDefaultValue() {
            return NbtString.of(BiomeKeys.PLAINS.getValue().toString());
        }
    }

    private static class IntegerToMapMapScreen extends ModernBetaGraphicalMapSettingsScreen {
        public IntegerToMapMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            ArrayList<SimpleOption<?>> options = new ArrayList<>();
            options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
            options.add(null);
            options.add(this.intFieldOptionFromString(KEY + i, this.getText("subVariantScale").getString()));
            options.add(this.mapEditButton(
                this.getText("biomes"),
                VALUE + i,
                BiomeInfoToListMapScreen::new
            ));
            return options;
        }

        @Override
        protected String getDefaultKey() {
            return "0";
        }

        @Override
        protected NbtElement getDefaultValue() {
            return new NbtCompound();
        }
    }

    private static class BiomeInfoToListMapScreen extends ModernBetaGraphicalMapSettingsScreen {
        public BiomeInfoToListMapScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtCompound settings, Consumer<NbtCompound> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            ArrayList<SimpleOption<?>> options = new ArrayList<>();
            options.add(this.headerOption(Text.translatable(this.getTextKey("item"), i).formatted(Formatting.BOLD)));
            options.add(null);
            options.addAll(this.biomeInfoOption(KEY + i));
            options.add(this.listEditButton(
                this.getText("biomeReplacements"),
                VALUE + i,
                NbtElement.STRING_TYPE,
                BiomeInfoListScreen::new
            ));
            options.add(null);
            return options;
        }

        @Override
        protected String getDefaultKey() {
            return BiomeKeys.PLAINS.getValue().toString();
        }

        @Override
        protected NbtElement getDefaultValue() {
            return NbtString.of(BiomeKeys.PLAINS.getValue().toString());
        }
    }

    private static class VoronoiPointBiomeListScreen extends ModernBetaGraphicalListSettingsScreen {
        public VoronoiPointBiomeListScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtList settings, Consumer<NbtList> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            return this.voronoiPointBiomeOption(i);
        }

        @Override
        protected NbtElement getDefaultElement() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, BiomeKeys.PLAINS.getValue().toString())
                .putString(NbtTags.OCEAN_BIOME, BiomeKeys.OCEAN.getValue().toString())
                .putString(NbtTags.DEEP_OCEAN_BIOME, BiomeKeys.DEEP_OCEAN.getValue().toString())
                .putFloat(NbtTags.TEMP, 0.5F)
                .putFloat(NbtTags.RAIN, 0.5F)
                .putFloat(NbtTags.WEIRD, 0.5F)
                .build();
        }
    }
}
