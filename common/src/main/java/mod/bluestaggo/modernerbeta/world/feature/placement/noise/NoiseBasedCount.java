package mod.bluestaggo.modernerbeta.world.feature.placement.noise;

import net.minecraft.util.math.random.Random;

public interface NoiseBasedCount {
    int sample(int chunkX, int chunkZ, Random random);
}
