package com.bespectacled.modernbeta.biome.beta;

import com.bespectacled.modernbeta.carver.BetaCarver;
import com.bespectacled.modernbeta.feature.BetaConfiguredFeature;
import com.bespectacled.modernbeta.structure.BetaStructure;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class WarmOcean {
    public static final Biome BIOME = create();
    
    private static Biome create() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addWarmOceanMobs(spawnSettings, 10, 4);
        
        spawnSettings.spawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.PUFFERFISH, 15, 1, 3));
        
        GenerationSettings.Builder genSettings = new GenerationSettings.Builder();
        genSettings.surfaceBuilder(ConfiguredSurfaceBuilders.FULL_SAND);
        
        DefaultBiomeFeatures.addOceanStructures(genSettings);
        DefaultBiomeFeatures.addDefaultLakes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultMushrooms(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        
        genSettings.structureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
        genSettings.structureFeature(ConfiguredStructureFeatures.OCEAN_RUIN_WARM);
        genSettings.structureFeature(ConfiguredStructureFeatures.RUINED_PORTAL_OCEAN);
        genSettings.structureFeature(BetaStructure.CONF_OCEAN_SHRINE_STRUCTURE);
        
        genSettings.feature(Feature.UNDERGROUND_ORES, BetaConfiguredFeature.ORE_CLAY);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_SUGAR_CANE);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.PATCH_PUMPKIN);
        
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.WARM_OCEAN_VEGETATION);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEAGRASS_WARM);
        genSettings.feature(Feature.VEGETAL_DECORATION, ConfiguredFeatures.SEA_PICKLE);
        
        genSettings.feature(Feature.TOP_LAYER_MODIFICATION, BetaConfiguredFeature.BETA_FREEZE_TOP_LAYER);
        
        genSettings.carver(GenerationStep.Carver.AIR, BetaCarver.CONF_BETA_CAVE_CARVER);
        genSettings.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CANYON);
        genSettings.carver(GenerationStep.Carver.LIQUID, ConfiguredCarvers.UNDERWATER_CAVE);
        genSettings.carver(GenerationStep.Carver.LIQUID, ConfiguredCarvers.UNDERWATER_CANYON);
        
        return (new Biome.Builder())
            .precipitation(Biome.Precipitation.RAIN)
            .category(Biome.Category.OCEAN)
            .depth(-1.0F)
            .scale(0.1F)
            .temperature(0.8F)
            .downfall(0.9F)
            .effects((new BiomeEffects.Builder())
                .skyColor(7254527)
                .fogColor(12638463)
                .waterColor(4445678)
                .waterFogColor(270131)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}