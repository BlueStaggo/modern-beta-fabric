package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderEarlyRelease;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderMajorRelease;
import mod.bluestaggo.modernerbeta.world.feature.BetaFreezeTopLayerFeature;
import mod.bluestaggo.modernerbeta.world.feature.placed.ModernBetaMiscPlacedFeatures;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FreezeTopLayerFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * Mixin is unfortunately needed since vanilla biomes mixed with Beta/PE biomes may confuse feature decorator,
 * and cause some chunks to not properly generate Beta/PE climate-influenced snow/ice layer.
 * 
 * Beta biomes + vanilla cave biomes is a situation where this would commonly occur.
 * 
 * TODO: Revisit this in a little to see if still necessary.
 * 
 * To Test --
 * Version: 6.0+1.19.4
 * World Preset: Beta Large Biomes
 * Seed: 8252128008552916748
 * Coord: -2770 139 -3404
 * 
 */
@Mixin(FreezeTopLayerFeature.class)
public abstract class MixinFreezeTopLayerFeature {
    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    private void injectGenerate(FeatureContext<DefaultFeatureConfig> context, CallbackInfoReturnable<Boolean> info) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        
        ChunkGenerator chunkGenerator = context.getGenerator();
        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator
                && (modernBetaChunkGenerator.getChunkProvider() instanceof ChunkProviderEarlyRelease
                        || modernBetaChunkGenerator.getChunkProvider() instanceof ChunkProviderMajorRelease)) {
            BetaFreezeTopLayerFeature.setFreezeTopLayer(world, pos, biomeSource, modernBetaChunkGenerator.getChunkProvider().getChunkSettings().useSurfaceRules);
            info.setReturnValue(true);
            return;
        }
        
        boolean hasClimateSampler =
            biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource &&
            modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler;
        
        if (hasClimateSampler) {
            int x = pos.getX();
            int z = pos.getZ();
            int y = context.getWorld().getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z);
            
            BlockPos topPos = new BlockPos(x, y, z);
            RegistryEntry<Biome> topBiome = context.getWorld().getBiome(topPos);
            
            Reference<PlacedFeature> betaFreezeTopLayer = context.getWorld()
                .getRegistryManager()
                .getOrThrow(RegistryKeys.PLACED_FEATURE)
                .getOptional(ModernBetaMiscPlacedFeatures.FREEZE_TOP_LAYER)
                .orElse(null);
            
            boolean hasBetaFreezeTopLayer = topBiome.value()
                .getGenerationSettings()
                .getFeatures()
                .stream()
                .anyMatch(list -> list.contains(betaFreezeTopLayer));
            
            if (hasBetaFreezeTopLayer) {
                BetaFreezeTopLayerFeature.setFreezeTopLayer(world, pos, biomeSource, false);
                info.setReturnValue(true);
            }
        }
    }
}
