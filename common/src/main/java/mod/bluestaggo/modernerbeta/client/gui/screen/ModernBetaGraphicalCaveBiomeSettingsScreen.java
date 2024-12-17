package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.util.NbtCompoundBuilder;
import mod.bluestaggo.modernerbeta.util.NbtTags;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.biome.BiomeKeys;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModernBetaGraphicalCaveBiomeSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    public ModernBetaGraphicalCaveBiomeSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "caveBiome", settings, onDone);
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        String biomeProvider = this.settings.getString(NbtTags.BIOME_PROVIDER);

        list.addSingleOptionEntry(this.primarySelectionOption(NbtTags.BIOME_PROVIDER,
            Arrays.stream(ModernBetaBuiltInTypes.CaveBiome.values())
                .map(caveBiome -> caveBiome.id)
                .toArray(String[]::new)));

        if (ModernBetaBuiltInTypes.CaveBiome.SINGLE.id.equals(biomeProvider)) {
            list.addAll(this.headerOption(this.getText(NbtTags.SINGLE_BIOME)), this.biomeOption(NbtTags.SINGLE_BIOME, false));
        } else if (ModernBetaBuiltInTypes.CaveBiome.VORONOI.id.equals(biomeProvider)) {
            list.addAll(
                this.floatRangeOption(NbtTags.VORONOI_HORIZONTAL_NOISE_SCALE, 0.001F, 100.0F),
                this.floatRangeOption(NbtTags.VORONOI_VERTICAL_NOISE_SCALE, 0.001F, 100.0F),
                this.intRangeOption(NbtTags.VORONOI_DEPTH_MIN_Y, -64, 320),
                this.intRangeOption(NbtTags.VORONOI_DEPTH_MAX_Y, -64, 320)
            );

            list.addSingleOptionEntry(this.listEditButton(
                this.getText(NbtTags.VORONOI_POINTS),
                NbtTags.VORONOI_POINTS,
                NbtElement.COMPOUND_TYPE,
                VoronoiPointCaveBiomeListScreen::new
            ));
        }
    }

    private static class VoronoiPointCaveBiomeListScreen extends ModernBetaGraphicalListSettingsScreen {
        public VoronoiPointCaveBiomeListScreen(String title, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, NbtList settings, Consumer<NbtList> onDone) {
            super(title, parent, generatorOptionsHolder, settings, onDone);
        }

        @Override
        protected List<SimpleOption<?>> getOptions(int i) {
            return this.voronoiPointCaveBiomeOption(i);
        }

        @Override
        protected NbtElement getDefaultElement() {
            return new NbtCompoundBuilder()
                .putString(NbtTags.BIOME, BiomeKeys.LUSH_CAVES.getValue().toString())
                .putFloat(NbtTags.TEMP, 0.5F)
                .putFloat(NbtTags.RAIN, 0.5F)
                .putFloat(NbtTags.DEPTH, 0.5F)
                .build();
        }
    }
}
