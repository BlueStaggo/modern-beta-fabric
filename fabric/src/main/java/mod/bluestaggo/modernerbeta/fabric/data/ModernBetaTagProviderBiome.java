package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.concurrent.CompletableFuture;

import static mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags.*;

public class ModernBetaTagProviderBiome extends FabricTagProvider<Biome> {
    public ModernBetaTagProviderBiome(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BIOME, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
        this.configureModernBeta(lookup);
        this.configureVanilla(lookup);
        this.configureConventional(lookup);
    }
    
    private void configureModernBeta(WrapperLookup lookup) {
        /* Modern Beta Biome Tags */
        
        getOrCreateTagBuilder(IS_MODERN_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY,
                
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN,
            
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER,
            
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_325,
            ModernBetaBiomes.INFDEV_227,

            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS,

            ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA,

            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );

        getOrCreateTagBuilder(IS_EARLY_RELEASE).add(
            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );

        getOrCreateTagBuilder(IS_LATE_BETA).add(
            ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA
        );

        getOrCreateTagBuilder(IS_BETA).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.BETA_SKY
        );
        
        getOrCreateTagBuilder(IS_PE).add(
            ModernBetaBiomes.PE_FOREST,
            ModernBetaBiomes.PE_SHRUBLAND,
            ModernBetaBiomes.PE_DESERT,
            ModernBetaBiomes.PE_SAVANNA,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.PE_SEASONAL_FOREST,
            ModernBetaBiomes.PE_RAINFOREST,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        getOrCreateTagBuilder(IS_ALPHA).add(
            ModernBetaBiomes.ALPHA,
            ModernBetaBiomes.ALPHA_WINTER
        );

        getOrCreateTagBuilder(IS_INFDEV).add(
            ModernBetaBiomes.INFDEV_611,
            ModernBetaBiomes.INFDEV_420,
            ModernBetaBiomes.INFDEV_415,
            ModernBetaBiomes.INFDEV_325,
            ModernBetaBiomes.INFDEV_227
        );

        getOrCreateTagBuilder(IS_INDEV).add(
            ModernBetaBiomes.INDEV_NORMAL,
            ModernBetaBiomes.INDEV_HELL,
            ModernBetaBiomes.INDEV_PARADISE,
            ModernBetaBiomes.INDEV_WOODS
        );
        
        getOrCreateTagBuilder(IS_FOREST).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.PE_FOREST
        );
        
        getOrCreateTagBuilder(IS_SEASONAL_FOREST).add(
            ModernBetaBiomes.BETA_SEASONAL_FOREST,
            ModernBetaBiomes.PE_SEASONAL_FOREST
        );
        
        getOrCreateTagBuilder(IS_RAINFOREST).add(
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.PE_RAINFOREST
        );

        getOrCreateTagBuilder(IS_DESERT).add(
            ModernBetaBiomes.BETA_DESERT,
            ModernBetaBiomes.PE_DESERT
        );
        
        getOrCreateTagBuilder(IS_PLAINS).add(
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.PE_PLAINS,
            ModernBetaBiomes.LATE_BETA_PLAINS
        );
        
        getOrCreateTagBuilder(IS_SHRUBLAND).add(
            ModernBetaBiomes.BETA_SHRUBLAND,
            ModernBetaBiomes.PE_SHRUBLAND
        );
        
        getOrCreateTagBuilder(IS_SAVANNA).add(
            ModernBetaBiomes.BETA_SAVANNA,
            ModernBetaBiomes.PE_SAVANNA
        );
        
        getOrCreateTagBuilder(IS_SWAMP).add(
            ModernBetaBiomes.BETA_SWAMPLAND,
            ModernBetaBiomes.PE_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND
        );
        
        getOrCreateTagBuilder(IS_TAIGA).add(
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.PE_TAIGA,
            ModernBetaBiomes.LATE_BETA_TAIGA,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA
        );
        
        getOrCreateTagBuilder(IS_TUNDRA).add(
            ModernBetaBiomes.BETA_TUNDRA,
            ModernBetaBiomes.PE_TUNDRA,
            ModernBetaBiomes.BETA_ICE_DESERT,
            ModernBetaBiomes.PE_ICE_DESERT,
            ModernBetaBiomes.EARLY_RELEASE_ICE_PLAINS
        );
        
        getOrCreateTagBuilder(IS_OCEAN).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );

        getOrCreateTagBuilder(IS_RELEASE_SPAWN).add(
            ModernBetaBiomes.BETA_FOREST,
            ModernBetaBiomes.BETA_PLAINS,
            ModernBetaBiomes.BETA_TAIGA,
            ModernBetaBiomes.BETA_RAINFOREST,
            ModernBetaBiomes.LATE_BETA_SWAMPLAND,
            ModernBetaBiomes.LATE_BETA_PLAINS,
            ModernBetaBiomes.LATE_BETA_TAIGA,
            ModernBetaBiomes.EARLY_RELEASE_TAIGA,
            BiomeKeys.FOREST,
            BiomeKeys.PLAINS,
            BiomeKeys.TAIGA,
            BiomeKeys.JUNGLE
        );

        /* Modern Beta Biome Structure Tags */
        
        getOrCreateTagBuilder(INDEV_STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_INDEV);
        
        /* Modern Beta Surface Config Tags */
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SAND)
            .addOptionalTag(SURFACE_CONFIG_IS_DESERT)
            .add(
                ModernBetaBiomes.BETA_DESERT,
                ModernBetaBiomes.PE_DESERT,
                BiomeKeys.DESERT,
                BiomeKeys.BEACH,
                BiomeKeys.SNOWY_BEACH
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_RED_SAND);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_BADLANDS)
            .addOptionalTag(SURFACE_CONFIG_IS_BADLANDS)
            .add(
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS
            );
    
        getOrCreateTagBuilder(SURFACE_CONFIG_NETHER)
            .addOptionalTag(SURFACE_CONFIG_IS_NETHER)
            .add(BiomeKeys.NETHER_WASTES);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_WARPED_NYLIUM)
            .add(BiomeKeys.WARPED_FOREST);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_CRIMSON_NYLIUM)
            .add(BiomeKeys.CRIMSON_FOREST);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_BASALT)
            .add(BiomeKeys.BASALT_DELTAS);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SOUL_SOIL)
            .add(BiomeKeys.SOUL_SAND_VALLEY);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_END)
            .addOptionalTag(SURFACE_CONFIG_IS_END)
            .add(
                BiomeKeys.THE_END,
                BiomeKeys.END_BARRENS,
                BiomeKeys.END_HIGHLANDS,
                BiomeKeys.END_MIDLANDS,
                BiomeKeys.SMALL_END_ISLANDS
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_GRASS)
            .addOptionalTag(SURFACE_CONFIG_SWAMP)
            .add(BiomeKeys.SWAMP);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_MUD)
            .add(BiomeKeys.MANGROVE_SWAMP);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_MYCELIUM)
            .add(BiomeKeys.MUSHROOM_FIELDS);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_PODZOL)
            .add(
                BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_STONE)
            .add(
                BiomeKeys.STONY_PEAKS,
                BiomeKeys.STONY_SHORE
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW)
            .add(BiomeKeys.SNOWY_SLOPES);
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_DIRT)
            .add(
                BiomeKeys.GROVE,
                BiomeKeys.ICE_SPIKES
            );
        
        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_PACKED_ICE)
            .add(BiomeKeys.FROZEN_PEAKS);

        getOrCreateTagBuilder(SURFACE_CONFIG_SNOW_STONE)
            .add(BiomeKeys.JAGGED_PEAKS);

        /* Modern Beta Biome Height Tags */

        getOrCreateTagBuilder(HEIGHT_CONFIG_DEFAULT)
            .addTag(IS_TUNDRA)
            .addTag(HEIGHT_CONFIG_SHORT_HILLS)
            .add(
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS,
                BiomeKeys.SAVANNA
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_OCEAN)
            .addTag(BiomeTags.IS_OCEAN)
            .add(
                BiomeKeys.FROZEN_OCEAN,
                BiomeKeys.OCEAN,
                BiomeKeys.COLD_OCEAN,
                BiomeKeys.LUKEWARM_OCEAN,
                BiomeKeys.WARM_OCEAN
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_DESERT)
            .addTag(IS_DESERT)
            .add(
                BiomeKeys.DESERT,
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_BETA_HILLS)
            .addTag(IS_RAINFOREST)
            .add(
                ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
                BiomeKeys.WINDSWEPT_SAVANNA
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_EXTREME_HILLS)
            .add(
                ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
                BiomeKeys.WINDSWEPT_HILLS,
                BiomeKeys.WINDSWEPT_FOREST,
                BiomeKeys.WINDSWEPT_GRAVELLY_HILLS
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_TAIGA)
            .addTag(BiomeTags.IS_TAIGA);

        getOrCreateTagBuilder(HEIGHT_CONFIG_SWAMPLAND)
            .addTag(IS_SWAMP)
            .add(
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_RIVER)
            .add(
                BiomeKeys.RIVER,
                BiomeKeys.FROZEN_RIVER
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_MOUNTAINS)
            .addTag(IS_TUNDRA)
            .add(
                BiomeKeys.SNOWY_PLAINS,
                BiomeKeys.GROVE,
                BiomeKeys.FROZEN_PEAKS,
                BiomeKeys.JAGGED_PEAKS,
                BiomeKeys.STONY_PEAKS,
                BiomeKeys.SNOWY_SLOPES
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_MUSHROOM_ISLAND)
            .add(BiomeKeys.MUSHROOM_FIELDS);

        getOrCreateTagBuilder(HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE)
            .add(BiomeKeys.MUSHROOM_FIELDS);

        getOrCreateTagBuilder(HEIGHT_CONFIG_BEACH)
            .add(
                BiomeKeys.BEACH,
                BiomeKeys.SNOWY_BEACH
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_HILLS)
            .addTag(HEIGHT_CONFIG_DESERT)
            .addTag(HEIGHT_CONFIG_TAIGA);

        getOrCreateTagBuilder(HEIGHT_CONFIG_SHORT_HILLS)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_PLAINS)
            .add(
                BiomeKeys.PLAINS,
                BiomeKeys.FOREST,
                BiomeKeys.FLOWER_FOREST,
                BiomeKeys.BIRCH_FOREST,
                BiomeKeys.DARK_FOREST,
                BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                BiomeKeys.SNOWY_BEACH
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_EXTREME_HILLS_EDGE)
            .addTag(HEIGHT_CONFIG_EXTREME_HILLS);

        getOrCreateTagBuilder(HEIGHT_CONFIG_JUNGLE)
            .add(
                BiomeKeys.JUNGLE,
                BiomeKeys.BAMBOO_JUNGLE,
                BiomeKeys.SPARSE_JUNGLE
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_JUNGLE_HILLS)
            .addTag(HEIGHT_CONFIG_JUNGLE)
            .add(
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS,
                BiomeKeys.SAVANNA
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_PLATEAU)
            .add(
                BiomeKeys.CHERRY_GROVE,
                BiomeKeys.MEADOW
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_SWAMPLAND_HILLS)
            .addTag(HEIGHT_CONFIG_SWAMPLAND);

        getOrCreateTagBuilder(HEIGHT_CONFIG_PLATEAU_HILL)
            .add(
                BiomeKeys.BADLANDS,
                BiomeKeys.ERODED_BADLANDS,
                BiomeKeys.WOODED_BADLANDS
            );

        getOrCreateTagBuilder(HEIGHT_CONFIG_DEEP_OCEAN)
            .add(
                BiomeKeys.DEEP_FROZEN_OCEAN,
                BiomeKeys.DEEP_OCEAN,
                BiomeKeys.DEEP_COLD_OCEAN,
                BiomeKeys.DEEP_LUKEWARM_OCEAN
            );

        /* Modern Beta Fractal Tags */

        getOrCreateTagBuilder(FRACTAL_SWAMP_RIVERS)
            .addTag(HEIGHT_CONFIG_SWAMPLAND);

        getOrCreateTagBuilder(FRACTAL_JUNGLE_RIVERS)
            .addTag(HEIGHT_CONFIG_JUNGLE);

        getOrCreateTagBuilder(FRACTAL_NO_BEACHES)
            .addTag(BiomeTags.IS_OCEAN)
            .add(
                BiomeKeys.RIVER,
                ModernBetaBiomes.LATE_BETA_SWAMPLAND,
                ModernBetaBiomes.EARLY_RELEASE_SWAMPLAND,
                BiomeKeys.SWAMP,
                BiomeKeys.MANGROVE_SWAMP,
                ModernBetaBiomes.LATE_BETA_EXTREME_HILLS,
                ModernBetaBiomes.EARLY_RELEASE_EXTREME_HILLS,
                BiomeKeys.WINDSWEPT_HILLS,
                BiomeKeys.WINDSWEPT_FOREST,
                BiomeKeys.WINDSWEPT_GRAVELLY_HILLS
            );
    }
    
    private void configureVanilla(WrapperLookup lookup) {
        /* Vanilla Biome Tags */
        
        getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
            .addTag(IS_MODERN_BETA);
        
        getOrCreateTagBuilder(BiomeTags.IS_DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(BiomeTags.IS_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(BiomeTags.IS_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.IS_TAIGA)
            .addTag(IS_TAIGA);
        
        /* Vanilla Biome Structure Tags */
        
        getOrCreateTagBuilder(BiomeTags.BURIED_TREASURE_HAS_STRUCTURE)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(BiomeTags.DESERT_PYRAMID_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.IGLOO_HAS_STRUCTURE)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(BiomeTags.JUNGLE_TEMPLE_HAS_STRUCTURE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(BiomeTags.MINESHAFT_HAS_STRUCTURE)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);

        getOrCreateTagBuilder(BiomeTags.OCEAN_RUIN_COLD_HAS_STRUCTURE).add(
            ModernBetaBiomes.BETA_OCEAN,
            ModernBetaBiomes.BETA_COLD_OCEAN,
            ModernBetaBiomes.BETA_FROZEN_OCEAN,

            ModernBetaBiomes.PE_OCEAN,
            ModernBetaBiomes.PE_COLD_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.OCEAN_RUIN_WARM_HAS_STRUCTURE).add(
            ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
            ModernBetaBiomes.BETA_WARM_OCEAN,

            ModernBetaBiomes.PE_LUKEWARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SWAMP)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_DESERT_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_STANDARD_HAS_STRUCTURE)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_TUNDRA)
            .add(ModernBetaBiomes.BETA_SKY);
        
        getOrCreateTagBuilder(BiomeTags.RUINED_PORTAL_SWAMP_HAS_STRUCTURE)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.STRONGHOLD_HAS_STRUCTURE)
            .addTag(IS_BETA)
            .addTag(IS_PE)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV);
        
        getOrCreateTagBuilder(BiomeTags.SWAMP_HUT_HAS_STRUCTURE)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_DESERT_HAS_STRUCTURE)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_PLAINS_HAS_STRUCTURE)
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_SAVANNA)
            .addTag(IS_ALPHA)
            .addTag(IS_INFDEV)
            .addTag(IS_INDEV);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_SNOWY_HAS_STRUCTURE)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(BiomeTags.VILLAGE_TAIGA_HAS_STRUCTURE)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(BiomeTags.WOODLAND_MANSION_HAS_STRUCTURE)
            .addTag(IS_SEASONAL_FOREST);
        
        /* Misc. Tags */
        
        getOrCreateTagBuilder(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(
            ModernBetaBiomes.BETA_WARM_OCEAN,
            ModernBetaBiomes.PE_WARM_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(BiomeTags.TRAIL_RUINS_HAS_STRUCTURE)
            .add(
                ModernBetaBiomes.BETA_TAIGA,
                ModernBetaBiomes.BETA_RAINFOREST,
                
                ModernBetaBiomes.PE_TAIGA,
                ModernBetaBiomes.PE_RAINFOREST
            );

        getOrCreateTagBuilder(BiomeTags.SPAWNS_SNOW_FOXES)
            .add(
                    ModernBetaBiomes.BETA_TAIGA,
                    ModernBetaBiomes.PE_TAIGA,
                    ModernBetaBiomes.EARLY_RELEASE_TAIGA
            );
    }
    
    /*
     * For determining climate tags, see:
     * https://www.minecraftforum.net/forums/archive/alpha/alpha-survival-single-player/820956-biome-geography-algorithm-analysis-updated-11-4
     * 
     */
    private void configureConventional(WrapperLookup lookup) {
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_AQUATIC)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_AQUATIC_ICY).add(
            ModernBetaBiomes.BETA_FROZEN_OCEAN,
            ModernBetaBiomes.PE_FROZEN_OCEAN
        );
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_COLD)
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_DRY)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_HOT)
            .addTag(IS_DESERT)
            .addTag(IS_PLAINS)
            .addTag(IS_SEASONAL_FOREST)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_TEMPERATE)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_FOREST)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_WET)
            .addTag(IS_OCEAN)
            .addTag(IS_SWAMP)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_DESERT)
            .addTag(IS_DESERT);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_FOREST)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_OVERWORLD)
            .addTag(IS_MODERN_BETA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_JUNGLE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_DEEP_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_PLAINS)
            .addTag(IS_PLAINS)
            .addTag(IS_SHRUBLAND);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SAVANNA)
            .addTag(IS_SAVANNA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SHALLOW_OCEAN)
            .addTag(IS_OCEAN);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SNOWY)
            .addTag(IS_TAIGA)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SNOWY_PLAINS)
            .addTag(IS_TUNDRA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SWAMP)
            .addTag(IS_SWAMP);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_TAIGA)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_CONIFEROUS_TREE)
            .addTag(IS_TAIGA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_DECIDUOUS_TREE)
            .addTag(IS_FOREST)
            .addTag(IS_SEASONAL_FOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_JUNGLE_TREE)
            .addTag(IS_RAINFOREST);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_SAVANNA_TREE)
            .addTag(IS_SAVANNA);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_DENSE)
            .addTag(IS_RAINFOREST)
            .addTag(IS_PLAINS);
        
        getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_SPARSE)
            .addTag(IS_DESERT)
            .addTag(IS_SAVANNA)
            .addTag(IS_SHRUBLAND)
            .addTag(IS_TUNDRA);
    }
}