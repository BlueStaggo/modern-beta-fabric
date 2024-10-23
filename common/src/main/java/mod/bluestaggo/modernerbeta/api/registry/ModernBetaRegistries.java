package mod.bluestaggo.modernerbeta.api.registry;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.blocksource.BlockSource;
import mod.bluestaggo.modernerbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bluestaggo.modernerbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.math.random.RandomSplitter;
import net.minecraft.world.biome.Biome;

public final class ModernBetaRegistries {
    public static final ModernBetaRegistry<ChunkProviderCreator> CHUNK;
    public static final ModernBetaRegistry<BiomeProviderCreator> BIOME;
    public static final ModernBetaRegistry<CaveBiomeProviderCreator> CAVE_BIOME;
    public static final ModernBetaRegistry<NoisePostProcessor> NOISE_POST_PROCESSOR;
    public static final ModernBetaRegistry<SurfaceConfig> SURFACE_CONFIG;
    public static final ModernBetaRegistry<HeightConfig> HEIGHT_CONFIG;
    public static final ModernBetaRegistry<BlockSourceCreator> BLOCKSOURCE;
    public static final ModernBetaRegistry<ModernBetaSettingsPreset> SETTINGS_PRESET;
    public static final ModernBetaRegistry<ModernBetaSettingsPresetCategory> SETTINGS_PRESET_CATEGORY;

    static {
        CHUNK = new ModernBetaRegistry<>("CHUNK");
        BIOME = new ModernBetaRegistry<>("BIOME");
        CAVE_BIOME = new ModernBetaRegistry<>("CAVE_BIOME");
        NOISE_POST_PROCESSOR = new ModernBetaRegistry<>("NOISE_POST_PROCESSOR");
        SURFACE_CONFIG = new ModernBetaRegistry<>("SURFACE_CONFIG");
        HEIGHT_CONFIG = new ModernBetaRegistry<>("HEIGHT_CONFIG");
        BLOCKSOURCE = new ModernBetaRegistry<>("BLOCKSOURCE");
        SETTINGS_PRESET = new ModernBetaRegistry<>("SETTINGS_PRESET");
        SETTINGS_PRESET_CATEGORY = new ModernBetaRegistry<>("SETTINGS_PRESET_CATEGORIES");
    }
    
    @FunctionalInterface
    public static interface ChunkProviderCreator {
        ChunkProvider apply(ModernBetaChunkGenerator chunkGenerator, long seed);
    }
    
    @FunctionalInterface
    public static interface BiomeProviderCreator {
        BiomeProvider apply(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
    }
    
    @FunctionalInterface
    public static interface CaveBiomeProviderCreator {
        CaveBiomeProvider apply(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed);
    }
    
    @FunctionalInterface
    public static interface BlockSourceCreator {
        BlockSource apply(ModernBetaSettingsChunk settingsChunk, RandomSplitter randomSplitter);
    }
}
