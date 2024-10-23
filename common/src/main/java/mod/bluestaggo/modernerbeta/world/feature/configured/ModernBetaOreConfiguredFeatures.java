package mod.bluestaggo.modernerbeta.world.feature.configured;

import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatureTags;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ModernBetaOreConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);
    public static final RegistryKey<ConfiguredFeature<?, ?>> CAVE_INFDEV_325 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.CAVE_INFDEV_325);

    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredFeature<?, ?>> featureRegisterable = (Registerable<ConfiguredFeature<?, ?>>)registerable;
        
        TagMatchRuleTest ruleStone = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchRuleTest ruleDeepslate = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        TagMatchRuleTest ruleDirt = new TagMatchRuleTest(BlockTags.DIRT);

        List<OreFeatureConfig.Target> emeraldTargets = List.of(
            OreFeatureConfig.createTarget(ruleStone, Blocks.EMERALD_ORE.getDefaultState()),
            OreFeatureConfig.createTarget(ruleDeepslate, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState())
        );
        List<OreFeatureConfig.Target> caveTargets = List.of(
            OreFeatureConfig.createTarget(ruleStone, Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(ruleDeepslate, Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(ruleDirt, Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", "ores"))), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(TagKey.of(RegistryKeys.BLOCK, Identifier.of("forge", "ores"))), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.COAL_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.COPPER_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.IRON_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.GOLD_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DIAMOND_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.REDSTONE_ORES), Blocks.AIR.getDefaultState()),
            OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.LAPIS_ORES), Blocks.AIR.getDefaultState())
        );
        
        ConfiguredFeatures.register(featureRegisterable, ORE_CLAY, ModernBetaFeatures.ORE_CLAY.get(), new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SAND), Blocks.CLAY.getDefaultState(), 33));
        ConfiguredFeatures.register(featureRegisterable, ORE_EMERALD_Y95, Feature.ORE, new OreFeatureConfig(emeraldTargets, 8, 0.9f));
        ConfiguredFeatures.register(featureRegisterable, CAVE_INFDEV_325, ModernBetaFeatures.CAVE_INFDEV_325.get(), new OreFeatureConfig(caveTargets, 16, 0.0f));
    }
}
