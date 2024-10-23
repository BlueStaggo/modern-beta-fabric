package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.cavebiome.climate.CaveClime;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderForcedHeight;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderNoise;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkHeightmap;
import mod.bluestaggo.modernerbeta.world.biome.HeightConfig;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.BiomeInfo;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public abstract class MixinDebugHud {
    @Shadow private MinecraftClient client;
    
    @Inject(method = "getLeftText", at = @At("TAIL"))
    private void injectGetLeftText(CallbackInfoReturnable<List<String>> info) {
        BlockPos pos = this.client.getCameraEntity().getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        IntegratedServer integratedServer = this.client.getServer();
        ServerWorld serverWorld = null;
        
        if (integratedServer != null) {
            serverWorld = integratedServer.getWorld(this.client.world.getRegistryKey());
        }

        if (serverWorld != null) {
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();

            if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
                if (chunkProvider instanceof ChunkProviderForcedHeight chunkProviderForcedHeight) {
                    BiomeInfo biomeInfo = chunkProviderForcedHeight.getBiomeInfo(x >> 2, z >> 2);
                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Biome variant: %s",
                            biomeInfo.getId()
                        )
                    );
                }
            }

            if (ModernerBeta.DEV_ENV) {
                if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                    if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
                        Clime clime = climateSampler.sample(x, z);
                        double temp = clime.temp();
                        double rain = clime.rain();

                        info.getReturnValue().add(
                            String.format(
                                "[Modern Beta] Climate Temp: %.3f Rainfall: %.3f",
                                temp,
                                rain
                            )
                        );
                    }

                    if (modernBetaBiomeSource.getCaveBiomeProvider() instanceof CaveClimateSampler climateSampler) {
                        CaveClime clime = climateSampler.sample(x >> 2, y >> 2, z >> 2);
                        double temp = clime.temp();
                        double rain = clime.rain();

                        info.getReturnValue().add(
                            String.format(
                                "[Modern Beta] Cave Climate Temp: %.3f Rainfall: %.3f",
                                temp,
                                rain
                            )
                        );
                    }

                }

                if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
                    ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d",
                            chunkProvider.getHeight(x, z, Type.WORLD_SURFACE_WG),
                            chunkProvider.getHeight(x, z, Type.OCEAN_FLOOR),
                            chunkProvider.getSeaLevel()
                        )
                    );

                    if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
                        info.getReturnValue().add(
                            String.format(
                                "[Modern Beta] Noise Chunk Provider WSF height: %d",
                                noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
                            )
                        );
                    }

                    if (chunkProvider instanceof ChunkProviderForcedHeight forcedHeightChunkProvider) {
                        HeightConfig heightConfig = forcedHeightChunkProvider.getRawHeightConfigAt(x >> 2, z >> 2);
                        info.getReturnValue().add(
                            String.format(
                                "[Modern Beta] Forced Height Chunk Provider height: %s",
                                heightConfig.toString()
                            )
                        );
                    }

                    /*
                    int worldMinY = modernBetaChunkGenerator.getMinimumY();
                    int surfaceHeight = modernBetaChunkGenerator.getBiomeInjector().sampleMinHeightAround(biomeX, biomeZ);
                    BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, -1, surfaceHeight).setY(y);

                    boolean canPlaceCave = BiomeInjector.CAVE_PREDICATE.test(context);

                    info.getReturnValue().add(
                        String.format(
                            "[Modern Beta] Valid cave biome position: %b",
                            canPlaceCave
                        )
                    );
                    */

                    if (modernBetaChunkGenerator.getBiomeInjector() != null) {
                        String biome = modernBetaChunkGenerator.getBiomeInjector().getBiomeNameAtBlock(x, y, z, null, BiomeInjectionStep.ALL);
                        info.getReturnValue().add(String.format("[Modern Beta] Injected biome: %s", biome));
                    }
                }
            }

        }
    }
}
