package mod.bluestaggo.modernerbeta;

import mod.bluestaggo.modernerbeta.tags.ModernBetaBiomeTags;

import java.util.Set;

public final class ModernBetaBuiltInTypes {
    public static final String DEFAULT_ID = "default";
    
    public enum Chunk {
        BETA("beta"),
        SKYLANDS("skylands"),
        ALPHA("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_420("infdev_420"),
        INFDEV_415("infdev_415"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        PE("pe"),
        EARLY_RELEASE("early_release"),
        MAJOR_RELEASE("major_release")
        ;

        public static final Set<String> CHUNK_PROVIDER_NOISE = Set.of(
            BETA.id, SKYLANDS.id, ALPHA.id, INFDEV_611.id, INFDEV_420.id, INFDEV_415.id, PE.id, EARLY_RELEASE.id, MAJOR_RELEASE.id);
        public static final Set<String> CHUNK_PROVIDER_FORCED_HEIGHT = Set.of(EARLY_RELEASE.id, MAJOR_RELEASE.id);
        public static final Set<String> CHUNK_PROVIDER_FINITE = Set.of(INDEV.id, CLASSIC_0_30.id);

        public final String id;
        
        private Chunk(String id) {
            this.id = id;
        }
    }
    
    public enum Biome {
        BETA("beta"),
        SINGLE("single"),
        PE("pe"),
        VORONOI("voronoi"),
        FRACTAL("fractal")
        ;

        public static final Set<String> BIOME_PROVIDER_USES_NOISE = Set.of(BETA.id, PE.id, VORONOI.id);

        public final String id;
        
        private Biome(String id) { this.id = id; }
    }
    
    public enum CaveBiome {
        NONE("none"),
        SINGLE("single"),
        VORONOI("voronoi")
        ;

        public final String id;
        
        private CaveBiome(String id) {
            this.id = id;
        }
    }
    
    public enum SurfaceConfig {
        SAND(ModernBetaBiomeTags.SURFACE_CONFIG_SAND.id().toString()),
        RED_SAND(ModernBetaBiomeTags.SURFACE_CONFIG_RED_SAND.id().toString()),
        BADLANDS(ModernBetaBiomeTags.SURFACE_CONFIG_BADLANDS.id().toString()),
        NETHER(ModernBetaBiomeTags.SURFACE_CONFIG_NETHER.id().toString()),
        WARPED_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_WARPED_NYLIUM.id().toString()),
        CRIMSON_NYLIUM(ModernBetaBiomeTags.SURFACE_CONFIG_CRIMSON_NYLIUM.id().toString()),
        BASALT(ModernBetaBiomeTags.SURFACE_CONFIG_BASALT.id().toString()),
        SOUL_SOIL(ModernBetaBiomeTags.SURFACE_CONFIG_SOUL_SOIL.id().toString()),
        THEEND(ModernBetaBiomeTags.SURFACE_CONFIG_END.id().toString()),
        GRASS(ModernBetaBiomeTags.SURFACE_CONFIG_GRASS.id().toString()),
        MUD(ModernBetaBiomeTags.SURFACE_CONFIG_MUD.id().toString()),
        MYCELIUM(ModernBetaBiomeTags.SURFACE_CONFIG_MYCELIUM.id().toString()),
        PODZOL(ModernBetaBiomeTags.SURFACE_CONFIG_PODZOL.id().toString()),
        STONE(ModernBetaBiomeTags.SURFACE_CONFIG_STONE.id().toString()),
        SNOW(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW.id().toString()),
        SNOW_DIRT(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_DIRT.id().toString()),
        SNOW_PACKED_ICE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_PACKED_ICE.id().toString()),
        SNOW_STONE(ModernBetaBiomeTags.SURFACE_CONFIG_SNOW_STONE.id().toString())
        ;
        
        public final String id;
        
        private SurfaceConfig(String id) {
            this.id = id;
        }
    }

    public enum HeightConfig {
        HEIGHT_CONFIG_DEFAULT(ModernBetaBiomeTags.HEIGHT_CONFIG_DEFAULT.id().toString()),
        HEIGHT_CONFIG_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_OCEAN.id().toString()),
        HEIGHT_CONFIG_DESERT(ModernBetaBiomeTags.HEIGHT_CONFIG_DESERT.id().toString()),
        HEIGHT_CONFIG_EXTREME_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS.id().toString()),
        HEIGHT_CONFIG_BETA_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_BETA_HILLS.id().toString()),
        HEIGHT_CONFIG_TAIGA(ModernBetaBiomeTags.HEIGHT_CONFIG_TAIGA.id().toString()),
        HEIGHT_CONFIG_SWAMPLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND.id().toString()),
        HEIGHT_CONFIG_RIVER(ModernBetaBiomeTags.HEIGHT_CONFIG_RIVER.id().toString()),
        HEIGHT_CONFIG_MOUNTAINS(ModernBetaBiomeTags.HEIGHT_CONFIG_MOUNTAINS.id().toString()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND.id().toString()),
        HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE(ModernBetaBiomeTags.HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE.id().toString()),
        HEIGHT_CONFIG_BEACH(ModernBetaBiomeTags.HEIGHT_CONFIG_BEACH.id().toString()),
        HEIGHT_CONFIG_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_HILLS.id().toString()),
        HEIGHT_CONFIG_SHORT_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SHORT_HILLS.id().toString()),
        HEIGHT_CONFIG_EXTREME_HILLS_EDGE(ModernBetaBiomeTags.HEIGHT_CONFIG_EXTREME_HILLS_EDGE.id().toString()),
        HEIGHT_CONFIG_JUNGLE(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE.id().toString()),
        HEIGHT_CONFIG_JUNGLE_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_JUNGLE_HILLS.id().toString()),
        HEIGHT_CONFIG_PLATEAU(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU.id().toString()),
        HEIGHT_CONFIG_SWAMPLAND_HILLS(ModernBetaBiomeTags.HEIGHT_CONFIG_SWAMPLAND_HILLS.id().toString()),
        HEIGHT_CONFIG_PLATEAU_HILL(ModernBetaBiomeTags.HEIGHT_CONFIG_PLATEAU_HILL.id().toString()),
        HEIGHT_CONFIG_DEEP_OCEAN(ModernBetaBiomeTags.HEIGHT_CONFIG_DEEP_OCEAN.id().toString())
        ;

        public final String id;

        private HeightConfig(String id) {
            this.id = id;
        }
    }
    
    public enum NoisePostProcessor {
        NONE("none")
        ;
        
        public final String id;
        
        private NoisePostProcessor(String id) {
            this.id = id;
        }
    }
    
    public enum BlockSource {
        DEEPSLATE("deepslate")
        ;
        
        public final String id;
        
        private BlockSource(String id) {
            this.id = id;
        }
    }
    
    public enum Preset {
        BETA_1_7_3("beta"),
        BETA_1_1_02("beta_1_1_02"),
        SKYLANDS("skylands"),
        ALPHA_1_1_2_01("alpha"),
        INFDEV_611("infdev_611"),
        INFDEV_420("infdev_420"),
        INFDEV_415("infdev_415"),
        INFDEV_325("infdev_325"),
        INFDEV_227("infdev_227"),
        INDEV("indev"),
        CLASSIC_0_30("classic_0_30"),
        CLASSIC_0_0_14A_08("classic_0_0_14a_08"),
        PE("pe"),
        BETA_1_8_1("beta_1_8_1"),
        BETA_1_9_PRE_3("beta_1_9_pre_3"),
        RELEASE_1_0_0("release_1_0_0"),
        RELEASE_1_1("release_1_1"),
        RELEASE_1_2_5("release_1_2_5"),
        RELEASE_1_6_4("release_1_6_4"),
        RELEASE_1_12_2("release_1_12_2"),
        RELEASE_1_17_1("release_1_17_1"),
        BETA_SKYLANDS("beta_skylands"),
        BETA_ISLES("beta_isles"),
        BETA_WATER_WORLD("beta_water_world"),
        BETA_ISLE_LAND("beta_isle_land"),
        BETA_CAVE_DELIGHT("beta_cave_delight"),
        BETA_MOUNTAIN_MADNESS("beta_mountain_madness"),
        BETA_DROUGHT("beta_drought"),
        BETA_CAVE_CHAOS("beta_cave_chaos"),
        BETA_LARGE_BIOMES("beta_large_biomes"),
        BETA_XBOX_LEGACY("beta_xbox_legacy"),
        BETA_SURVIVAL_ISLAND("beta_survival_island"),
        BETA_VANILLA("beta_vanilla"),
        RELEASE_HYBRID("release_hybrid"),
        ALPHA_WINTER("alpha_winter"),
        INDEV_PARADISE("indev_paradise"),
        INDEV_WOODS("indev_woods"),
        INDEV_HELL("indev_hell"),
        WATER_WORLD("water_world"),
        ISLE_LAND("isle_land"),
        CAVE_DELIGHT("cave_delight"),
        MOUNTAIN_MADNESS("mountain_madness"),
        DROUGHT("drought"),
        CAVE_CHAOS("cave_chaos"),
        BETA_1_8_1_LARGE_BIOMES("beta_1_8_1_large_biomes"),
        BETA_1_9_PRE_3_LARGE_BIOMES("beta_1_9_pre_3_large_biomes"),
        RELEASE_1_0_0_LARGE_BIOMES("release_1_0_0_large_biomes"),
        RELEASE_1_1_LARGE_BIOMES("release_1_1_large_biomes"),
        RELEASE_1_2_5_LARGE_BIOMES("release_1_2_5_large_biomes"),
        RELEASE_1_6_4_LARGE_BIOMES("release_1_6_4_large_biomes"),
        RELEASE_1_12_2_LARGE_BIOMES("release_1_12_2_large_biomes"),
        RELEASE_1_17_1_LARGE_BIOMES("release_1_17_1_large_biomes"),
        ;
        
        public final String id;
        
        private Preset(String id) {
            this.id = id;
        }
    }

    public enum PresetCategory {
        BETA("beta"),
        ALPHA_INFDEV("alpha_infdev"),
        FINITE("finite"),
        EARLY_RELEASE("early_release"),
        EARLY_RELEASE_LARGE_BIOMES("early_release_large_biomes"),
        MAJOR_RELEASE("major_release"),
        BETA_CUSTOM("beta_custom"),
        RELEASE_CUSTOM("release_custom"),
        ;

        public final String id;

        private PresetCategory(String id) {
            this.id = id;
        }
    }
}
