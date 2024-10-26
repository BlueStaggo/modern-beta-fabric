package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;

import java.util.List;

public record ModernBetaSettingsPresetCategory(String defaultIcon, List<String> presets) {
    public static ModernBetaSettingsPresetCategory BETA = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_1_02.id,
                ModernBetaBuiltInTypes.Preset.SKYLANDS.id,
                ModernBetaBuiltInTypes.Preset.PE.id
            )
    );
    public static ModernBetaSettingsPresetCategory ALPHA_INFDEV = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.ALPHA_1_1_2_01.id,
                ModernBetaBuiltInTypes.Preset.ALPHA_WINTER.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_611.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_420.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_415.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_325.id,
                ModernBetaBuiltInTypes.Preset.INFDEV_227.id
            )
    );
    public static ModernBetaSettingsPresetCategory FINITE = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.INDEV.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.INDEV.id,
                ModernBetaBuiltInTypes.Preset.INDEV_PARADISE.id,
                ModernBetaBuiltInTypes.Preset.INDEV_WOODS.id,
                ModernBetaBuiltInTypes.Preset.INDEV_HELL.id,
                ModernBetaBuiltInTypes.Preset.CLASSIC_0_30.id,
                ModernBetaBuiltInTypes.Preset.CLASSIC_0_0_14A_08.id
            )
    );
    public static ModernBetaSettingsPresetCategory EARLY_RELEASE = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_1_8_1.id,
                ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_1.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4.id
            )
    );
    public static ModernBetaSettingsPresetCategory EARLY_RELEASE_LARGE_BIOMES = new ModernBetaSettingsPresetCategory(
        ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.id,
        List.of(
            ModernBetaBuiltInTypes.Preset.BETA_1_8_1_LARGE_BIOMES.id,
            ModernBetaBuiltInTypes.Preset.BETA_1_9_PRE_3_LARGE_BIOMES.id,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_0_0_LARGE_BIOMES.id,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_1_LARGE_BIOMES.id,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_2_5_LARGE_BIOMES.id,
            ModernBetaBuiltInTypes.Preset.RELEASE_1_6_4_LARGE_BIOMES.id
        )
    );
    public static ModernBetaSettingsPresetCategory MAJOR_RELEASE = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_12_2_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1.id,
                ModernBetaBuiltInTypes.Preset.RELEASE_1_17_1_LARGE_BIOMES.id
            )
    );
    public static ModernBetaSettingsPresetCategory BETA_CUSTOM = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.BETA_SKYLANDS.id,
                ModernBetaBuiltInTypes.Preset.BETA_ISLES.id,
                ModernBetaBuiltInTypes.Preset.BETA_WATER_WORLD.id,
                ModernBetaBuiltInTypes.Preset.BETA_ISLE_LAND.id,
                ModernBetaBuiltInTypes.Preset.BETA_CAVE_DELIGHT.id,
                ModernBetaBuiltInTypes.Preset.BETA_MOUNTAIN_MADNESS.id,
                ModernBetaBuiltInTypes.Preset.BETA_DROUGHT.id,
                ModernBetaBuiltInTypes.Preset.BETA_CAVE_CHAOS.id,
                ModernBetaBuiltInTypes.Preset.BETA_LARGE_BIOMES.id,
                ModernBetaBuiltInTypes.Preset.BETA_XBOX_LEGACY.id,
                ModernBetaBuiltInTypes.Preset.BETA_SURVIVAL_ISLAND.id,
                ModernBetaBuiltInTypes.Preset.BETA_VANILLA.id
            )
    );
    public static ModernBetaSettingsPresetCategory RELEASE_CUSTOM = new ModernBetaSettingsPresetCategory(
            ModernBetaBuiltInTypes.Preset.ISLE_LAND.id,
            List.of(
                ModernBetaBuiltInTypes.Preset.RELEASE_HYBRID.id,
                ModernBetaBuiltInTypes.Preset.WATER_WORLD.id,
                ModernBetaBuiltInTypes.Preset.ISLE_LAND.id,
                ModernBetaBuiltInTypes.Preset.CAVE_DELIGHT.id,
                ModernBetaBuiltInTypes.Preset.MOUNTAIN_MADNESS.id,
                ModernBetaBuiltInTypes.Preset.DROUGHT.id,
                ModernBetaBuiltInTypes.Preset.CAVE_CHAOS.id
            )
    );
}
