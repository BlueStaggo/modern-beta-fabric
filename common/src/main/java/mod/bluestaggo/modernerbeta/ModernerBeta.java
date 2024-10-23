package mod.bluestaggo.modernerbeta;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.color.BlockColors;
import mod.bluestaggo.modernerbeta.client.resource.ModernBetaColormapResource;
import mod.bluestaggo.modernerbeta.command.DebugProviderSettingsCommand;
import mod.bluestaggo.modernerbeta.config.ModernBetaConfig;
import mod.bluestaggo.modernerbeta.world.ModernBetaWorldInitializer;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.carver.ModernBetaCarvers;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFeatures;
import mod.bluestaggo.modernerbeta.world.feature.ModernBetaFoliagePlacers;
import mod.bluestaggo.modernerbeta.world.feature.placement.ModernBetaPlacementTypes;
import net.fabricmc.api.EnvType;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class ModernerBeta {
    public static final String MOD_ID = "moderner_beta";
    public static final String MOD_NAME = "Moderner Beta";

    public static final boolean CLIENT_ENV = Platform.getEnv() == EnvType.CLIENT;
    public static final boolean DEV_ENV = Platform.isDevelopmentEnvironment();

    public static final ModernBetaConfig CONFIG = AutoConfig.register(ModernBetaConfig.class, GsonConfigSerializer::new).getConfig();
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ModernerBeta.log(Level.INFO, "Initializing Moderner Beta...");

        ModernBetaFoliagePlacers.register();
        ModernBetaPlacementTypes.register();
        ModernBetaFeatures.register();
        ModernBetaCarvers.register();

        ModernBetaBiomeSource.register();
        ModernBetaChunkGenerator.register();

        // Register default providers
        ModernBetaBuiltInProviders.registerChunkProviders();
        ModernBetaBuiltInProviders.registerBiomeProviders();
        ModernBetaBuiltInProviders.registerCaveBiomeProviders();
        ModernBetaBuiltInProviders.registerSurfaceConfigs();
        ModernBetaBuiltInProviders.registerHeightConfigs();
        ModernBetaBuiltInProviders.registerNoisePostProcessors();
        ModernBetaBuiltInProviders.registerBlockSources();
        ModernBetaBuiltInProviders.registerSettingsPresets();
        ModernBetaBuiltInProviders.registerSettingsPresetCategories();

        if (CLIENT_ENV) {
            // Override default biome grass/foliage colors
            BlockColors.register();

            // Load colormaps
            ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, new ModernBetaColormapResource(
                    "textures/colormap/water.png",
                    BlockColorSampler.INSTANCE.colormapWater::setColormap
            ));

            ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, new ModernBetaColormapResource(
                    "textures/colormap/underwater.png",
                    BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
            ));
        }

        if (DEV_ENV) {
            DebugProviderSettingsCommand.register();
        }

        // Initializes chunk and biome providers at server start-up.
        LifecycleEvent.SERVER_BEFORE_START.register(ModernBetaWorldInitializer::init);
    }

    public static Identifier createId(String name) {
        return Identifier.of(MOD_ID, name);
    }

    public static void log(Level level, String message) {
        LOGGER.atLevel(level).log("[" + MOD_NAME + "] {}", message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }
}
