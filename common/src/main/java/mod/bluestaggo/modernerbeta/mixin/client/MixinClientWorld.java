package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.world.ModernBetaClientWorld;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsBiome;
import mod.bluestaggo.modernerbeta.util.SeedUtil;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.provider.BiomeProviderBeta;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientWorld.class, priority = 1)
public abstract class MixinClientWorld implements ModernBetaClientWorld {
    @Shadow private MinecraftClient client;
    
    @Unique private Vec3d modernBeta_pos;
    @Unique private ClimateSampler modernBeta_climateSampler;
    @Unique private ClimateSamplerSky modernBeta_climateSamplerSky;
    @Unique private boolean modernBeta_isModernBetaWorld;
    
    @Override
    public boolean isModernBetaWorld() {
        return this.modernBeta_isModernBetaWorld;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(
        ClientPlayNetworkHandler networkHandler,
        ClientWorld.Properties properties,
        RegistryKey<World> registryKeyWorld,
        RegistryEntry<DimensionType> registryEntryDimensionType,
        int loadDistance,
        int simulationDistance,
        WorldRenderer renderer,
        boolean debugWorld,
        long seed,
        int seaLevel,
        CallbackInfo info
    ) {
        long worldSeed = SeedUtil.parseSeed(ModernerBeta.CONFIG.fixedSeed);
        boolean useFixedSeed = ModernerBeta.CONFIG.useFixedSeed;
        
        // Init with default values
        BiomeProviderBeta biomeProviderBeta = new BiomeProviderBeta(new ModernBetaSettingsBiome().toCompound(), null, worldSeed);
        this.modernBeta_climateSampler = useFixedSeed ?  biomeProviderBeta : null;
        this.modernBeta_climateSamplerSky = useFixedSeed ? biomeProviderBeta : null;
        this.modernBeta_isModernBetaWorld = false;
        
        // Server check
        if (this.client.getServer() != null && registryKeyWorld != null) {
            ServerWorld serverWorld = this.client.getServer().getWorld(registryKeyWorld);
            
            ChunkGenerator chunkGenerator = serverWorld.getChunkManager().getChunkGenerator();
            BiomeSource biomeSource = chunkGenerator.getBiomeSource();
            
            if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                BiomeProvider biomeProvider = modernBetaBiomeSource.getBiomeProvider();
                
                if (biomeProvider instanceof ClimateSampler climateSampler)
                    this.modernBeta_climateSampler = climateSampler;
                
                if (biomeProvider instanceof ClimateSamplerSky skyClimateSampler)
                    this.modernBeta_climateSamplerSky = skyClimateSampler;
            }
            
            this.modernBeta_isModernBetaWorld = chunkGenerator instanceof ModernBetaChunkGenerator || biomeSource instanceof ModernBetaBiomeSource;
        }
        
        // Set climate sampler
        BlockColorSampler.INSTANCE.setClimateSampler(this.modernBeta_climateSampler);
    }
    
    @Inject(method = "getSkyColor", at = @At("HEAD"))
    private void capturePos(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        this.modernBeta_pos = cameraPos;
    }
    
    @ModifyVariable(
        method = "getSkyColor",
        at = @At(
            value = "INVOKE_ASSIGN",  
            target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"
        ),
        index = 5
    )
    private Vec3d injectSkyColor(Vec3d skyColorVec) {
        if (this.modernBeta_climateSamplerSky != null && this.modernBeta_climateSamplerSky.useSkyColor()) {
            int x = (int)modernBeta_pos.getX();
            int z = (int)modernBeta_pos.getZ();
            
            float temp = (float)this.modernBeta_climateSamplerSky.sampleSky(x, z);
            temp /= 3F;
            temp = MathHelper.clamp(temp, -1F, 1F);
            
            skyColorVec = Vec3d.unpackRgb(MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
        }
        
        return skyColorVec;
    }
}

