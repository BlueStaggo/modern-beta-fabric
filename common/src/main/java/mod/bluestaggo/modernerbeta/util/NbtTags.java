package mod.bluestaggo.modernerbeta.util;

public class NbtTags {
    /* Chunk Settings */
    
    public static final String CHUNK_PROVIDER = "chunkProvider";
    
    public static final String USE_DEEPSLATE = "useDeepslate";
    public static final String DEEPSLATE_MIN_Y = "deepslateMinY";
    public static final String DEEPSLATE_MAX_Y = "deepslateMaxY";
    public static final String DEEPSLATE_BLOCK = "deepslateBlock";
    
    public static final String USE_WORLD_BORDER = "useWorldBorder";
    public static final String WORLD_BORDER_WIDTH = "worldBorderWidth";
    public static final String WORLD_BORDER_LENGTH = "worldBorderLength";

    public static final String USE_SURFACE_RULES = "useSurfaceRules";
    public static final String SEA_LEVEL_OFFSET = "seaLevelOffset";

    public static final String USE_CAVES = "useCaves";
    public static final String USE_FIXED_CAVES = "useFixedCaves";
    public static final String FORCE_BETA_CAVES = "forceBetaCaves";
    public static final String FORCE_BETA_RAVINES = "forceBetaRavines";

    // Noise
    public static final String NOISE_POST_PROCESSOR = "noisePostProcessor";
    public static final String NOISE_COORDINATE_SCALE = "noiseCoordinateScale";
    public static final String NOISE_HEIGHT_SCALE = "noiseHeightScale";
    public static final String NOISE_UPPER_LIMIT_SCALE = "noiseUpperLimitScale";
    public static final String NOISE_LOWER_LIMIT_SCALE = "noiseLowerLimitScale";
    public static final String NOISE_DEPTH_NOISE_SCALE_X = "noiseDepthNoiseScaleX";
    public static final String NOISE_DEPTH_NOISE_SCALE_Z = "noiseDepthNoiseScaleZ";
    public static final String NOISE_MAIN_NOISE_SCALE_X = "noiseMainNoiseScaleX";
    public static final String NOISE_MAIN_NOISE_SCALE_Y = "noiseMainNoiseScaleY";
    public static final String NOISE_MAIN_NOISE_SCALE_Z = "noiseMainNoiseScaleZ";
    public static final String NOISE_BASE_SIZE = "noiseBaseSize";
    public static final String NOISE_STRETCH_Y = "noiseStretchY";
    
    public static final String NOISE_TOP_SLIDE_TARGET = "noiseTopSlideTarget";
    public static final String NOISE_TOP_SLIDE_SIZE = "noiseTopSlideSize";
    public static final String NOISE_TOP_SLIDE_OFFSET = "noiseTopSlideOffset";
    
    public static final String NOISE_BOTTOM_SLIDE_TARGET = "noiseBottomSlideTarget";
    public static final String NOISE_BOTTOM_SLIDE_SIZE = "noiseBottomSlideSize";
    public static final String NOISE_BOTTOM_SLIDE_OFFSET = "noiseBottomSlideOffset";

    // Release
    public static final String RELEASE_HEIGHT_OVERRIDES = "releaseHeightOverrides";
    public static final String RELEASE_BIOME_DEPTH_OFFSET = "releaseBiomeDepthOffset";
    public static final String RELEASE_BIOME_DEPTH_WEIGHT = "releaseBiomeDepthWeight";
    public static final String RELEASE_BIOME_SCALE_OFFSET = "releaseBiomeScaleOffset";
    public static final String RELEASE_BIOME_SCALE_WEIGHT = "releaseBiomeScaleWeight";

    // Infdev 227
    public static final String INFDEV_USE_PYRAMID = "infdevUsePyramid";
    public static final String INFDEV_USE_WALL = "infdevUseWall";
    
    // Indev
    public static final String INDEV_LEVEL_THEME = "indevLevelTheme";
    public static final String INDEV_LEVEL_TYPE = "indevLevelType";
    public static final String INDEV_LEVEL_WIDTH = "indevLevelWidth";
    public static final String INDEV_LEVEL_LENGTH = "indevLevelLength";
    public static final String INDEV_LEVEL_HEIGHT = "indevLevelHeight";
    public static final String INDEV_CAVE_RADIUS = "indevCaveRadius";
    public static final String INDEV_USE_CAVES = "indevUseCaves";
    public static final String INDEV_NOISE_SCALE = "indevNoiseScale";
    public static final String INDEV_SELECTOR_SCALE = "indevSelectorScale";
    public static final String INDEV_MIN_HEIGHT_DAMP = "indevMinHeightDamp";
    public static final String INDEV_MIN_HEIGHT_BOOST = "indevMinHeightBoost";
    public static final String INDEV_MAX_HEIGHT_DAMP = "indevMaxHeightDamp";
    public static final String INDEV_MAX_HEIGHT_BOOST = "indevMaxHeightBoost";
    public static final String INDEV_HEIGHT_UNDER_DAMP = "indevHeightUnderDamp";
    public static final String INDEV_CAVE_RARITY = "indevCaveRarity";
    public static final String INDEV_SAND_BEACH_THRESHOLD = "indevSandBeachThreshold";
    public static final String INDEV_SAND_BEACH_UNDER_AIR = "indevSandBeachUnderAir";
    public static final String INDEV_SAND_BEACH_UNDER_FLUID = "indevSandBeachUnderFluid";
    public static final String INDEV_GRAVEL_BEACH_THRESHOLD = "indevGravelBeachThreshold";
    public static final String INDEV_GRAVEL_BEACH_UNDER_AIR = "indevGravelBeachUnderAir";
    public static final String INDEV_GRAVEL_BEACH_UNDER_FLUID = "indevGravelBeachUnderFluid";
    public static final String INDEV_WATER_RARITY = "indevWaterRarity";
    public static final String INDEV_LAVA_RARITY = "indevLavaRarity";
    public static final String INDEV_SPAWN_HOUSE = "indevSpawnHouse";

    // Islands
    public static final String ISLES_USE_ISLANDS = "islesUseIslands";
    public static final String ISLES_USE_OUTER_ISLANDS = "islesUseOuterIslands";
    public static final String ISLES_OCEAN_SLIDE_TARGET = "islesOceanSlideTarget";
    public static final String ISLES_CENTER_ISLAND_SHAPE = "islesCenterIslandShape";
    public static final String ISLES_CENTER_ISLAND_RADIUS = "islesCenterIslandRadius";
    public static final String ISLES_CENTER_ISLAND_FALLOFF_DIST = "islesCenterIslandFalloffDistance";
    public static final String ISLES_CENTER_OCEAN_RADIUS = "islesCenterOceanRadius";
    public static final String ISLES_CENTER_OCEAN_FALLOFF_DIST = "islesCenterOceanFalloffDistance";
    public static final String ISLES_OUTER_ISLAND_NOISE_SCALE = "islesOuterIslandNoiseScale";
    public static final String ISLES_OUTER_ISLAND_NOISE_OFFSET = "islesOuterIslandNoiseOffset";

    /* Biome Settings */
    
    public static final String BIOME_PROVIDER = "biomeProvider";
    public static final String SINGLE_BIOME = "singleBiome";
    public static final String USE_OCEAN_BIOMES = "useOceanBiomes";
    
    /* Climate/Voronoi Settings */
    
    public static final String BIOME = "biome";
    public static final String OCEAN_BIOME = "oceanBiome";
    public static final String DEEP_OCEAN_BIOME = "deepOceanBiome";
    
    public static final String TEMP = "temp";
    public static final String RAIN = "rain";
    public static final String DEPTH = "depth";
    public static final String WEIRD = "weird";
    
    public static final String CLIMATE_TEMP_NOISE_SCALE = "climateTempNoiseScale";
    public static final String CLIMATE_RAIN_NOISE_SCALE = "climateRainNoiseScale";
    public static final String CLIMATE_DETAIL_NOISE_SCALE = "climateDetailNoiseScale";
    public static final String CLIMATE_WEIRD_NOISE_SCALE = "climateWeirdNoiseScale";
    public static final String CLIMATE_MAPPINGS = "climateMappings";
    
    public static final String VORONOI_HORIZONTAL_NOISE_SCALE = "voronoiHorizontalNoiseScale";
    public static final String VORONOI_VERTICAL_NOISE_SCALE = "voronoiVerticalNoiseScale";
    public static final String VORONOI_DEPTH_MIN_Y = "voronoiDepthMinY";
    public static final String VORONOI_DEPTH_MAX_Y = "voronoiDepthMaxY";
    public static final String VORONOI_POINTS = "voronoiPoints";

    public static final String FRACTAL_BIOMES = "fractalBiomes";
    public static final String FRACTAL_CLIMATIC_BIOMES = "fractalClimaticBiomes";
    public static final String FRACTAL_HILL_VARIANTS = "fractalHillVariants";
    public static final String FRACTAL_EDGE_VARIANTS = "fractalEdgeVariants";
    public static final String FRACTAL_VERY_RARE_VARIANTS = "fractalVeryRareVariants";
    public static final String FRACTAL_SUB_VARIANTS = "fractalSubVariants";
    public static final String FRACTAL_MUTATED_VARIANTS = "fractalMutatedVariants";
    public static final String FRACTAL_PLAINS = "fractalPlains";
    public static final String FRACTAL_ICE_PLAINS = "fractalIcePlains";
    public static final String FRACTAL_BIOME_SCALE = "fractalBiomeScale";
    public static final String FRACTAL_HILL_SCALE = "fractalHillScale";
    public static final String FRACTAL_SUB_VARIANT_SCALE = "fractalSubVariantScale";
    public static final String FRACTAL_SUB_VARIANT_SEED = "fractalSubVariantSeed";
    public static final String FRACTAL_BEACH_SHRINK = "fractalBeachShrink";
    public static final String FRACTAL_OCEAN_SHRINK = "fractalOceanShrink";
    public static final String FRACTAL_TERRAIN_TYPE = "fractalTerrainType";
    public static final String FRACTAL_OCEANS = "fractalOceans";
    public static final String FRACTAL_ADD_RIVERS = "fractalAddRivers";
    public static final String FRACTAL_ADD_SNOW = "fractalAddSnow";
    public static final String FRACTAL_ADD_MUSHROOM_ISLANDS = "fractalAddMushroomIslands";
    public static final String FRACTAL_ADD_BEACHES = "fractalAddBeaches";
    public static final String FRACTAL_ADD_STONY_SHORES = "fractalAddStonyShores";
    public static final String FRACTAL_ADD_HILLS = "fractalAddHills";
    public static final String FRACTAL_ADD_SWAMP_RIVERS = "fractalAddSwampRivers";
    public static final String FRACTAL_ADD_DEEP_OCEANS = "fractalAddDeepOceans";
    public static final String FRACTAL_ADD_MUTATIONS = "fractalAddMutations";
    public static final String FRACTAL_ADD_CLIMATIC_OCEANS = "fractalAddClimaticOceans";
    public static final String FRACTAL_USE_CLIMATIC_BIOMES = "fractalUseClimaticBiomes";

    /* Cave Biome Settings */
    
    public static final String CAVE_BIOME_PROVIDER = "biomeProvider";
}
