package mod.bluestaggo.modernerbeta.world.biome.biomes.pe;

import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BiomePELukewarmOcean {
    public static Biome create(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        ModernBetaBiomeMobs.addLukewarmOceanMobs(spawnSettings);
        
        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        ModernBetaBiomeFeatures.addLukewarmOceanFeatures(genSettings, true);
        
        return (new Biome.Builder())
            .precipitation(true)
            .temperature(1.0F)
            .downfall(0.7F)
            .effects((new BiomeEffects.Builder())
                .skyColor(ModernBetaBiomeColors.PE_SKY_COLOR)
                .fogColor(ModernBetaBiomeColors.PE_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
                .grassColor(ModernBetaBiomeColors.PE_GRASS_COLOR)
                .foliageColor(ModernBetaBiomeColors.PE_FOLIAGE_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}