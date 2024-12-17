package mod.bluestaggo.modernerbeta.world.feature;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ModernBetaFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ModernerBeta.MOD_ID, RegistryKeys.FEATURE);
    public static final RegistrySupplier<BetaFreezeTopLayerFeature> FREEZE_TOP_LAYER = register(
        ModernBetaFeatureTags.FREEZE_TOP_LAYER, new BetaFreezeTopLayerFeature(DefaultFeatureConfig.CODEC)
    );

    public static final RegistrySupplier<BetaFancyOakFeature> OLD_FANCY_OAK = register(
        ModernBetaFeatureTags.FANCY_OAK, new BetaFancyOakFeature(DefaultFeatureConfig.CODEC)
    );

    public static final RegistrySupplier<BetaOreClayFeature> ORE_CLAY = register(
        ModernBetaFeatureTags.ORE_CLAY, new BetaOreClayFeature(OreFeatureConfig.CODEC)
    );

    public static final RegistrySupplier<CaveInfdev325Feature> CAVE_INFDEV_325 = register(
        ModernBetaFeatureTags.CAVE_INFDEV_325, new CaveInfdev325Feature(OreFeatureConfig.CODEC)
    );
    
    private static <F extends Feature<?>> RegistrySupplier<F> register(String id, F feature) {
        return FEATURES.register(ModernerBeta.createId(id), () -> feature);
    }
    
    public static void register() {
        FEATURES.register();
    }
}