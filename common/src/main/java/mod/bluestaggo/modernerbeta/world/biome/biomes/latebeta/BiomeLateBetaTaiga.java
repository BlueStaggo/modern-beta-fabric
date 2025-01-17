package mod.bluestaggo.modernerbeta.world.biome.biomes.latebeta;

import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public class BiomeLateBetaTaiga {
    public static Biome create(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawnSettings);
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 8, 4, 4)).spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.RABBIT, 4, 2, 3)).spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.FOX, 8, 2, 4));
        DefaultBiomeFeatures.addBatsAndMonsters(spawnSettings);

        GenerationSettings.LookupBackedBuilder genSettings = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);
        DefaultBiomeFeatures.addLandCarvers(genSettings);
        DefaultBiomeFeatures.addAmethystGeodes(genSettings);
        DefaultBiomeFeatures.addDungeons(genSettings);
        DefaultBiomeFeatures.addMineables(genSettings);
        DefaultBiomeFeatures.addSprings(genSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(genSettings);
        DefaultBiomeFeatures.addDefaultOres(genSettings);
        DefaultBiomeFeatures.addDefaultDisks(genSettings);
        DefaultBiomeFeatures.addTaigaTrees(genSettings);
        DefaultBiomeFeatures.addDefaultFlowers(genSettings);
        DefaultBiomeFeatures.addDefaultGrass(genSettings);
        DefaultBiomeFeatures.addDefaultVegetation(genSettings);
        DefaultBiomeFeatures.addSweetBerryBushes(genSettings);

        return (new Biome.Builder())
            .precipitation(true)
            .temperature(0.2F)
            .downfall(0.8F)
            .effects((new BiomeEffects.Builder())
                .skyColor(OverworldBiomeCreator.getSkyColor(0.2F))
                .fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
                .waterColor(ModernBetaBiomeColors.VANILLA_WATER_COLOR)
                .waterFogColor(ModernBetaBiomeColors.VANILLA_WATER_FOG_COLOR)
                .build())
            .spawnSettings(spawnSettings.build())
            .generationSettings(genSettings.build())
            .build();
    }
}
