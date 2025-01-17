package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.client.color.BlockColorSampler;
import mod.bluestaggo.modernerbeta.client.world.ModernBetaClientWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class MixinBackgroundRenderer {
    @Unique private static Vec3d modernBeta_pos;
    @Unique private static int modernBeta_renderDistance = 16;
    @Unique private static float modernBeta_fogWeight = calculateFogWeight(16);
    @Unique private static boolean modernBeta_isModernBetaWorld = false;
    
    @Redirect(
        method = "getFogColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"
        )
    )
    private static int modifyWaterFogColor(Biome instance) {
        if (BlockColorSampler.INSTANCE.useWaterColor()) {
            int x = (int)modernBeta_pos.getX();
            int z = (int)modernBeta_pos.getZ();
            
            Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(x, z);
            
            return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
        }
        
        return instance.getWaterFogColor();
    }
    
    @Inject(method = "getFogColor", at = @At("HEAD"))
    private static void captureVars(Camera camera, float tickDelta, ClientWorld world, int renderDistance, float skyDarkness, CallbackInfoReturnable<Vector4f> cir) {
        modernBeta_pos = camera.getPos();

        if (modernBeta_renderDistance != renderDistance) {
            modernBeta_renderDistance = renderDistance;
            modernBeta_fogWeight = calculateFogWeight(renderDistance);
        }

        // Track whether current client world is Modern Beta world,
        // old fog weighting won't be used if not.
        modernBeta_isModernBetaWorld = ((ModernBetaClientWorld)world).isModernBetaWorld();
    }
    
    @ModifyVariable(
        method = "getFogColor",
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/client/world/ClientWorld;getSkyColor(Lnet/minecraft/util/math/Vec3d;F)I"
        ),
        index = 10
    )
    private static float modifyFogWeighting(float weight) {
        return modernBeta_isModernBetaWorld && ModernerBeta.CONFIG.useOldFogColor ? modernBeta_fogWeight : weight;
    }
    
    @Unique
    private static float calculateFogWeight(int renderDistance) {
        // Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance) 
        // where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'
        
        int clampedDistance = MathHelper.clamp(renderDistance, 4, 16);
        clampedDistance -= 4;
        clampedDistance /= 4;

        int oldRenderDistance = Math.abs(clampedDistance - 3); 
        
        float weight = 1.0F / (float)(4 - oldRenderDistance);
        weight = 1.0F - (float)Math.pow(weight, 0.25);
        
        return weight;
    }
}
