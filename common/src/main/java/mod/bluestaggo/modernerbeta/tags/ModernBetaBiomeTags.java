package mod.bluestaggo.modernerbeta.tags;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class ModernBetaBiomeTags {
    public static final TagKey<Biome> IS_MODERN_BETA = keyOf("is_modern_beta");
    public static final TagKey<Biome> IS_EARLY_RELEASE = keyOf("is_early_release");
    public static final TagKey<Biome> IS_LATE_BETA = keyOf("is_late_beta");
    public static final TagKey<Biome> IS_BETA = keyOf("is_beta");
    public static final TagKey<Biome> IS_PE = keyOf("is_pe");
    public static final TagKey<Biome> IS_ALPHA = keyOf("is_alpha");
    public static final TagKey<Biome> IS_INFDEV = keyOf("is_infdev");
    public static final TagKey<Biome> IS_INDEV = keyOf("is_indev");

    public static final TagKey<Biome> IS_FOREST = keyOf("is_forest");
    public static final TagKey<Biome> IS_SEASONAL_FOREST = keyOf("is_seasonal_forest");
    public static final TagKey<Biome> IS_RAINFOREST = keyOf("is_rainforest");
    public static final TagKey<Biome> IS_DESERT = keyOf("is_desert");
    public static final TagKey<Biome> IS_PLAINS = keyOf("is_plains");
    public static final TagKey<Biome> IS_SHRUBLAND = keyOf("is_shrubland");
    public static final TagKey<Biome> IS_SAVANNA = keyOf("is_savanna");
    public static final TagKey<Biome> IS_SWAMP = keyOf("is_swamp");
    public static final TagKey<Biome> IS_TAIGA = keyOf("is_taiga");
    public static final TagKey<Biome> IS_TUNDRA = keyOf("is_tundra");
    public static final TagKey<Biome> IS_OCEAN = keyOf("is_ocean");
    public static final TagKey<Biome> IS_RELEASE_SPAWN = keyOf("is_early_release_spawn");

    public static final TagKey<Biome> INDEV_STRONGHOLD_HAS_STRUCTURE = keyOf("has_structure/indev_stronghold");

    /*
     * TODO: Deprecated, remove 1.20
     */
    public static final TagKey<Biome> SURFACE_CONFIG_IS_DESERT = keyOf("surface_config/is_desert");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_BADLANDS = keyOf("surface_config/is_badlands");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_NETHER = keyOf("surface_config/is_nether");
    public static final TagKey<Biome> SURFACE_CONFIG_IS_END = keyOf("surface_config/is_end");
    public static final TagKey<Biome> SURFACE_CONFIG_SWAMP = keyOf("surface_config/swamp");

    public static final TagKey<Biome> SURFACE_CONFIG_SAND = keyOf("surface_config/sand");
    public static final TagKey<Biome> SURFACE_CONFIG_RED_SAND = keyOf("surface_config/red_sand");
    public static final TagKey<Biome> SURFACE_CONFIG_BADLANDS = keyOf("surface_config/badlands");
    public static final TagKey<Biome> SURFACE_CONFIG_NETHER = keyOf("surface_config/nether");
    public static final TagKey<Biome> SURFACE_CONFIG_WARPED_NYLIUM = keyOf("surface_config/warped_nylium");
    public static final TagKey<Biome> SURFACE_CONFIG_CRIMSON_NYLIUM = keyOf("surface_config/crimson_nylium");
    public static final TagKey<Biome> SURFACE_CONFIG_BASALT = keyOf("surface_config/basalt");
    public static final TagKey<Biome> SURFACE_CONFIG_SOUL_SOIL = keyOf("surface_config/soul_soil");
    public static final TagKey<Biome> SURFACE_CONFIG_END = keyOf("surface_config/end");
    public static final TagKey<Biome> SURFACE_CONFIG_GRASS = keyOf("surface_config/grass");
    public static final TagKey<Biome> SURFACE_CONFIG_MUD = keyOf("surface_config/mud");
    public static final TagKey<Biome> SURFACE_CONFIG_MYCELIUM = keyOf("surface_config/mycelium");
    public static final TagKey<Biome> SURFACE_CONFIG_PODZOL = keyOf("surface_config/podzol");
    public static final TagKey<Biome> SURFACE_CONFIG_STONE = keyOf("surface_config/stone");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW = keyOf("surface_config/snow");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_DIRT = keyOf("surface_config/snow_dirt");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_PACKED_ICE = keyOf("surface_config/snow_packed_ice");
    public static final TagKey<Biome> SURFACE_CONFIG_SNOW_STONE = keyOf("surface_config/snow_stone");

    public static final TagKey<Biome> HEIGHT_CONFIG_DEFAULT = keyOf("height_config/default");
    public static final TagKey<Biome> HEIGHT_CONFIG_OCEAN = keyOf("height_config/ocean");
    public static final TagKey<Biome> HEIGHT_CONFIG_DESERT = keyOf("height_config/desert");
    public static final TagKey<Biome> HEIGHT_CONFIG_EXTREME_HILLS = keyOf("height_config/extreme_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_BETA_HILLS = keyOf("height_config/beta_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_TAIGA = keyOf("height_config/taiga");
    public static final TagKey<Biome> HEIGHT_CONFIG_SWAMPLAND = keyOf("height_config/swampland");
    public static final TagKey<Biome> HEIGHT_CONFIG_RIVER = keyOf("height_config/river");
    public static final TagKey<Biome> HEIGHT_CONFIG_MOUNTAINS = keyOf("height_config/mountains");
    public static final TagKey<Biome> HEIGHT_CONFIG_MUSHROOM_ISLAND = keyOf("height_config/mushroom_island");
    public static final TagKey<Biome> HEIGHT_CONFIG_MUSHROOM_ISLAND_SHORE = keyOf("height_config/mushroom_island_shore");
    public static final TagKey<Biome> HEIGHT_CONFIG_BEACH = keyOf("height_config/beach");
    public static final TagKey<Biome> HEIGHT_CONFIG_HILLS = keyOf("height_config/hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_SHORT_HILLS = keyOf("height_config/short_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_EXTREME_HILLS_EDGE = keyOf("height_config/extreme_hills_edge");
    public static final TagKey<Biome> HEIGHT_CONFIG_JUNGLE = keyOf("height_config/jungle");
    public static final TagKey<Biome> HEIGHT_CONFIG_JUNGLE_HILLS = keyOf("height_config/jungle_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_PLATEAU = keyOf("height_config/plateau");
    public static final TagKey<Biome> HEIGHT_CONFIG_SWAMPLAND_HILLS = keyOf("height_config/swampland_hills");
    public static final TagKey<Biome> HEIGHT_CONFIG_PLATEAU_HILL = keyOf("height_config/plateau_hill");
    public static final TagKey<Biome> HEIGHT_CONFIG_DEEP_OCEAN = keyOf("height_config/deep_ocean");

    public static final TagKey<Biome> FRACTAL_SWAMP_RIVERS = keyOf("fractal_swamp_rivers");
    public static final TagKey<Biome> FRACTAL_JUNGLE_RIVERS = keyOf("fractal_jungle_rivers");
    public static final TagKey<Biome> FRACTAL_NO_BEACHES = keyOf("fractal_no_beaches");


    private static TagKey<Biome> keyOf(String id) {
        return TagKey.of(RegistryKeys.BIOME, ModernerBeta.createId(id));
    }
}
