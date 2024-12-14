package mod.bluestaggo.modernerbeta.mixin;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderFinite;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bluestaggo.modernerbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bluestaggo.modernerbeta.world.chunk.provider.indev.IndevTheme;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.SpawnLocating;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Inject(method = "setupSpawn", at = @At("RETURN"))
    private static void injectSetupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo info) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();

        // Set old spawn angle (doesn't seem to work?)
        if (chunkGenerator instanceof ModernBetaChunkGenerator) {
            worldProperties.setSpawnPos(worldProperties.getSpawnPos(), -90.0f);
        }
    }
    
    @Redirect(
        method = "setupSpawn", 
        at = @At(
            value = "INVOKE", 
            target = "Lnet/minecraft/server/network/SpawnLocating;findServerSpawnPoint(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/ChunkPos;)Lnet/minecraft/util/math/BlockPos;"
        )
    )
    private static BlockPos redirectSpawnLocating(ServerWorld world, ChunkPos chunkPos) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        BlockPos spawnPos = SpawnLocating.findServerSpawnPoint(world, chunkPos);
        
        if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
            ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();
            
            world.getGameRules().get(GameRules.SPAWN_RADIUS).set(0, world.getServer()); // Ensure a centered spawn
            spawnPos = chunkProvider.getSpawnLocator().locateSpawn().orElse(spawnPos);
            
            if (spawnPos != null && ModernerBeta.DEV_ENV) {
                int x = spawnPos.getX();
                int y = spawnPos.getY();
                int z = spawnPos.getZ();
                
                ModernerBeta.log(Level.INFO, String.format("Spawning at %d/%d/%d", x, y, z));
            }
            
            if (spawnPos != null && chunkProvider instanceof ChunkProviderIndev chunkProviderIndev) {
                // Generate Indev house
                chunkProviderIndev.generateIndevHouse(world, spawnPos);
                
                // Set Indev world properties.
                setIndevProperties(world, chunkProviderIndev.getLevelTheme());
            }
            
            if (chunkProvider instanceof ChunkProviderFinite) {
                ChunkProviderFinite.resetPhase();
            }
        }
        
        return spawnPos;
    }
    
    @Unique
    private static void setIndevProperties(ServerWorld world, IndevTheme theme) {
        switch(theme) {
            case HELL -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(18000);
            } case PARADISE -> {
                world.getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null); 
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setTimeOfDay(6000);
            } case WOODS -> {
                world.getGameRules().get(GameRules.DO_WEATHER_CYCLE).set(false, null); 
                world.setWeather(0, Integer.MAX_VALUE, true, false);
            } default -> {}
        }
    }
}
