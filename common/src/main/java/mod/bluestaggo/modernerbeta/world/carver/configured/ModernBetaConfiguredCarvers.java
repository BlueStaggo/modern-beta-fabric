package mod.bluestaggo.modernerbeta.world.carver.configured;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.tags.ModernBetaBlockTags;
import mod.bluestaggo.modernerbeta.world.carver.BetaCaveCarverConfig;
import mod.bluestaggo.modernerbeta.world.carver.ModernBetaCarvers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.TrapezoidFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.RavineCarverConfig;
import net.minecraft.world.gen.heightprovider.BiasedToBottomHeightProvider;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

import java.util.Optional;

public class ModernBetaConfiguredCarvers {
    public static final RegistryKey<ConfiguredCarver<?>> BETA_CAVE = of("beta_cave");
    public static final RegistryKey<ConfiguredCarver<?>> BETA_CAVE_DEEP = of("beta_cave_deep");
    public static final RegistryKey<ConfiguredCarver<?>> BETA_CANYON = of("beta_canyon");

    @SuppressWarnings("unchecked")
    public static void bootstrap(Registerable<?> registerable) {
        Registerable<ConfiguredCarver<?>> carverRegisterable = (Registerable<ConfiguredCarver<?>>)registerable;
        RegistryEntryLookup<Block> registryBlock = carverRegisterable.getRegistryLookup(RegistryKeys.BLOCK);
        
        boolean useFixedCaves = false;
        boolean useAquifers = false;
        
        BetaCaveCarverConfig configCave = new BetaCaveCarverConfig(
            0.0f,                                                                               // Probability, unused here
            BiasedToBottomHeightProvider.create(YOffset.fixed(0), YOffset.fixed(127), 8),       // Y Level
            ConstantFloatProvider.create(0.5f),                                                 // Y scale, for large cave case(?)
            YOffset.aboveBottom(11),                                                            // Lava Level
            CarverDebugConfig.create(false, Blocks.WARPED_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            ConstantFloatProvider.create(1.0f),                                                 // Tunnel horizontal scale
            ConstantFloatProvider.create(1.0f),                                                 // Tunnel vertical scale
            ConstantFloatProvider.create(-0.7f),                                                // Y Floor Level
            Optional.of(useFixedCaves),
            Optional.of(useAquifers)
        );
        
        BetaCaveCarverConfig configCaveDeep = new BetaCaveCarverConfig(
            0.15f,                                                                              // Probability, unused here
            UniformHeightProvider.create(YOffset.aboveBottom(0), YOffset.fixed(0)),             // Y Level
            UniformFloatProvider.create(0.1f, 0.9f),                                            // Y scale, for large cave case(?)
            YOffset.aboveBottom(11),                                                            // Lava Level
            CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(ModernBetaBlockTags.OVERWORLD_CARVER_REPLACEABLES),
            UniformFloatProvider.create(0.7f, 1.4f),                                            // Tunnel horizontal scale
            UniformFloatProvider.create(0.8f, 1.3f),                                            // Tunnel vertical scale
            UniformFloatProvider.create(-1.0f, -0.4f),                                          // Y Floor Level
            Optional.of(useFixedCaves),
            Optional.of(useAquifers)
        );

        RavineCarverConfig configRavine = new RavineCarverConfig(
            0.02f,                                                                              // Probability
            BiasedToBottomHeightProvider.create(YOffset.aboveBottom(20), YOffset.fixed(67), 8), // Y Level
            ConstantFloatProvider.create(3.0F),                                                 // Y scale
            // Off-by-one error requires this value to be 1 block lower.
            YOffset.aboveBottom(10),                                                            // Lava Level
            CarverDebugConfig.create(false, Blocks.WARPED_BUTTON.getDefaultState()),
            registryBlock.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
            UniformFloatProvider.create(-0.125F, 0.125F),                                       // Vertical rotation
            new RavineCarverConfig.Shape(
                UniformFloatProvider.create(0.75F, 1.0F),                                       // Distance factor
                TrapezoidFloatProvider.create(0.0F, 6.0F, 2.0F),                                // Thickness
                3,                                                                              // Width smoothness
                UniformFloatProvider.create(0.75F, 1.0F),                                       // Horizontal radius factor
                1.0F,                                                                           // Vertical radius default factor
                0.0F                                                                            // Vertical radius center factor
            )
        );
    
        carverRegisterable.register(BETA_CAVE, ModernBetaCarvers.BETA_CAVE.get().configure(configCave));
        carverRegisterable.register(BETA_CAVE_DEEP, Carver.CAVE.configure(configCaveDeep));
        carverRegisterable.register(BETA_CANYON, Carver.RAVINE.configure(configRavine));
    }
    
    public static RegistryKey<ConfiguredCarver<?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, ModernerBeta.createId(id));
    }
}
