package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.api.world.spawn.SpawnLocator;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnLocating.class)
public abstract class MixinSpawnLocating {
    /*
     * Override vanilla behavior of moving player to highest solid block, 
     * even after finding initial spawn point.
     */
    @Inject(method = "findOverworldSpawn", at = @At("HEAD"), cancellable = true)
    private static void injectFindOverworldSpawnHeight(ServerWorld world, int x, int z, CallbackInfoReturnable<BlockPos> info) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator && 
            modernBetaChunkGenerator.getChunkProvider().getSpawnLocator() != SpawnLocator.DEFAULT
        ) {
            int spawnY = world.getLevelProperties().getSpawnPos().getY();
            
            info.setReturnValue(new BlockPos(x, spawnY, z));
        }
    }
}
