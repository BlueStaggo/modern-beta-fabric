package com.bespectacled.modernbeta.api.world.biome;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

import com.bespectacled.modernbeta.noise.SimplexOctaveNoise;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.MathHelper;

public enum BetaClimateSampler {
    INSTANCE;
    
    private final ChunkCache<ClimateCacheChunk> climateCache = new ChunkCache<>(ClimateCacheChunk::new, 384);
    private final ChunkCache<SkyCacheChunk> skyCache = new ChunkCache<>(SkyCacheChunk::new, 64);
    
    private SimplexOctaveNoise tempNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 9871L), 4);
    private SimplexOctaveNoise humidNoiseOctaves = new SimplexOctaveNoise(new Random(1 * 39811L), 4);
    private SimplexOctaveNoise noiseOctaves = new SimplexOctaveNoise(new Random(1 * 543321L), 2);
    
    private long seed;
    
    private BetaClimateSampler() {}
    
    protected void setSeed(long seed) {
        if (this.seed == seed) return;
        
        this.seed = seed;
        this.initNoise(seed);
        this.climateCache.clear();
        this.skyCache.clear();
    }
    
    protected double sampleTemp(int x, int z) {
        return this.climateCache.getCachedChunk(x, z).sampleTemp(x, z);
    }
    
    protected double sampleHumid(int x, int z) {
        return this.climateCache.getCachedChunk(x, z).sampleHumid(x, z);
    }
    
    protected void sampleTempHumid(double[] arr, int x, int z) {
        this.climateCache.getCachedChunk(x, z).sampleTempHumid(arr, x, z);
    }
    
    protected double sampleSkyTemp(int x, int z) {
        return this.skyCache.getCachedChunk(x, z).sampleTemp(x, z);
    }
    
    protected int sampleSkyColor(int x, int z) {
        float temp = (float)sampleSkyTemp(x, z);
        
        temp /= 3F;
        temp = MathHelper.clamp(temp, -1F, 1F);
        
        return MathHelper.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }
    
    private void sampleTempHumidAtPoint(double arr[], int x, int z) {
        double temp  = this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
        double humid = this.humidNoiseOctaves.sample(x, z, 0.05000000074505806D, 0.05000000074505806D, 0.33333333333333331D);
        double noise = this.noiseOctaves.sample(x, z, 0.25D, 0.25D, 0.58823529411764708D);

        double d = noise * 1.1000000000000001D + 0.5D;
        double d1 = 0.01D;
        double d2 = 1.0D - d1;

        temp = (temp * 0.14999999999999999D + 0.69999999999999996D) * d2 + d * d1;

        d1 = 0.002D;
        d2 = 1.0D - d1;

        humid = (humid * 0.14999999999999999D + 0.5D) * d2 + d * d1;

        temp = 1.0D - (1.0D - temp) * (1.0D - temp);

        arr[0] = MathHelper.clamp(temp, 0.0, 1.0);
        arr[1] = MathHelper.clamp(humid, 0.0, 1.0);
    }
    
    private double sampleSkyTempAtPoint(int x, int z) {
        return this.tempNoiseOctaves.sample(x, z, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
    }
    
    private void initNoise(long seed) {
        this.tempNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
        this.humidNoiseOctaves = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
        this.noiseOctaves = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
    }
    
    private abstract class CacheChunk {}
    
    private class ClimateCacheChunk extends CacheChunk {
        private final double temps[] = new double[256];
        private final double humids[] = new double [256];
        
        private ClimateCacheChunk(int chunkX, int chunkZ) {
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;
            double[] tempHumid = new double[2];
            
            int ndx = 0;
            for (int x = startX; x < startX + 16; ++x) {
                for (int z = startZ; z < startZ + 16; ++z) {
                    BetaClimateSampler.INSTANCE.sampleTempHumidAtPoint(tempHumid, x, z);
                    
                    this.temps[ndx] = tempHumid[0];
                    this.humids[ndx] = tempHumid[1];

                    ndx++;
                }
            }
        }
        
        private double sampleTemp(int x, int z) {
            return temps[(z & 0xF) + (x & 0xF) * 16];
        }
        
        private double sampleHumid(int x, int z) {
            return humids[(z & 0xF) + (x & 0xF) * 16];
        }
        
        private void sampleTempHumid(double[] tempHumid, int x, int z) {
            tempHumid[0] = temps[(z & 0xF) + (x & 0xF) * 16];
            tempHumid[1] = humids[(z & 0xF) + (x & 0xF) * 16];
        }
    }
    
    private class SkyCacheChunk extends CacheChunk {
        private final double temps[] = new double[256];
        
        private SkyCacheChunk(int chunkX, int chunkZ) {
            int startX = chunkX << 4;
            int startZ = chunkZ << 4;
            
            int ndx = 0;
            for (int x = startX; x < startX + 16; ++x) {
                for (int z = startZ; z < startZ + 16; ++z) {    
                    this.temps[ndx] = BetaClimateSampler.INSTANCE.sampleSkyTempAtPoint(x, z);
                    
                    ndx++;
                }
            }
        }
        
        private double sampleTemp(int x, int z) {
            return temps[(z & 0xF) + (x & 0xF) * 16];
        }
    }
    
    private final class ChunkCache<T extends CacheChunk> {
        private final Long2ObjectLinkedOpenHashMap<T> chunkCache;
        private final ReentrantReadWriteLock lock;
        private final BiFunction<Integer, Integer, T> newCacheChunk;
        
        private ChunkCache(BiFunction<Integer, Integer, T> newCacheChunk, int expected) {
            this.newCacheChunk = newCacheChunk;
            this.chunkCache = new Long2ObjectLinkedOpenHashMap<T>(expected);
            
            this.lock = new ReentrantReadWriteLock();
        }
        
        private void clear() {
            this.lock.writeLock().lock();
            try {
                this.chunkCache.clear();
                this.chunkCache.trim();
            } finally {
                this.lock.writeLock().unlock();
            }
        }
        
        private T getCachedChunk(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            long hashedCoord = (long)chunkX & 0xffffffffL | ((long)chunkZ & 0xffffffffL) << 32;
            
            T cachedChunk;
            
            this.lock.readLock().lock();
            try {
                cachedChunk = this.chunkCache.get(hashedCoord);
            } finally {
                this.lock.readLock().unlock();
            }
            
            if (cachedChunk == null) { 
                this.lock.writeLock().lock();
                try {
                    cachedChunk = this.newCacheChunk.apply(chunkX, chunkZ);
                    this.chunkCache.put(hashedCoord, cachedChunk);
                } finally {
                    this.lock.writeLock().unlock();
                }
                
            }
            
            return cachedChunk;
        }
    }
}