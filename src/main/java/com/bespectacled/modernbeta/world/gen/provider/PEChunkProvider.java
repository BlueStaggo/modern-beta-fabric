package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.biome.climate.Clime;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.api.world.gen.SurfaceConfig;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.util.mersenne.MTRandom;
import com.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.provider.PEBiomeProvider;
import com.bespectacled.modernbeta.world.biome.provider.settings.BiomeProviderSettings;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;
import com.bespectacled.modernbeta.world.spawn.PESpawnLocator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class PEChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final ClimateSampler climateSampler;
    
    public PEChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        
        // Use Mersenne Twister random instead of Java random
        MTRandom mtRand = new MTRandom(chunkGenerator.worldSeed);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(mtRand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(mtRand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(mtRand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(mtRand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(mtRand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(mtRand, 8, true);
        
        this.setForestOctaves(forestNoiseOctaves);
        
        // Get climate sampler from biome provider if exists and enabled,
        // else create new default Beta climate sampler.
        boolean sampleClimate = NbtUtil.readBoolean(
            NbtTags.SAMPLE_CLIMATE, 
            this.providerSettings, 
            ModernBeta.GEN_CONFIG.sampleClimate
        );
        
        this.climateSampler = (
            chunkGenerator.getBiomeSource() instanceof OldBiomeSource oldBiomeSource &&
            oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
            sampleClimate
        ) ? climateSampler : new PEBiomeProvider(chunkGenerator.getWorldSeed(), BiomeProviderSettings.createSettingsBeta(), null);
        this.spawnLocator = new PESpawnLocator(this, this.beachNoiseOctaves);
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double scale = 0.03125D;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        
        int bedrockFloor = this.worldMinY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable pos = new BlockPos.Mutable();

        AquiferSampler aquiferSampler = this.getAquiferSampler(chunk);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);
        
        // Surface builder stuff
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);
        HeightContext context = new HeightContext(this.chunkGenerator, region);
        
        BiomeAccess biomeAccess = region.getBiomeAccess();
        Registry<Biome> biomeRegistry = region.getRegistryManager().get(Registry.BIOME_KEY);
        
        MaterialRules.MaterialRuleContext ruleContext = new MaterialRules.MaterialRuleContext(
            this.surfaceBuilder,
            chunk,
            this.dummyNoiseChunkSampler,
            biomeAccess::getBiome,
            biomeRegistry,
            context
        );
        MaterialRules.BlockStateRule blockStateRule = this.surfaceRule.apply(ruleContext);

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = startX + localX;
                int z = startZ + localZ;
                int surfaceTopY = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG).get(localX, localZ);
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.worldMinY;
                
                // MCPE uses nextFloat() instead of nextDouble()
                boolean genSandBeach = this.beachNoiseOctaves.sample(
                    x, 0.0, startZ, localZ,
                    scale, scale, 1.0
                ) + rand.nextFloat() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.sampleXZ(
                    x, z,
                    scale, scale
                ) + rand.nextFloat() * 0.2 > 3.0;
                
                double surfaceNoise = this.surfaceNoiseOctaves.sample(
                    x, 0.0, startZ, localZ,
                    scale * 2.0, scale * 2.0, scale * 2.0
                );
                
                int surfaceDepth = (int) (surfaceNoise / 3.0 + 3.0 + rand.nextFloat() * 0.25);
                
                int runDepth = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

                SurfaceConfig surfaceConfig = SurfaceConfig.getSurfaceConfig(biome);
                BlockState topBlock = surfaceConfig.topBlock();
                BlockState fillerBlock = surfaceConfig.fillerBlock();

                boolean usedCustomSurface = this.surfaceBuilder.buildSurfaceColumn(
                    region.getRegistryManager().get(Registry.BIOME_KEY),
                    region.getBiomeAccess(), 
                    blockColumn, 
                    chunk, 
                    biome, 
                    ruleContext, 
                    blockStateRule,
                    localX,
                    localZ,
                    surfaceTopY
                );
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
                    BlockState blockState;
                    pos.set(localX, y, localZ);
                    
                    // Place deepslate
                    blockState = this.sampleDeepslateState(x, y, z);
                    if (blockState != null) {
                        chunk.setBlockState(pos, blockState, false);
                    }
                    
                    // Place bedrock
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(pos, BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }
                    
                    // Skip if used custom surface generation.
                    if (usedCustomSurface) {
                        continue;
                    }

                    blockState = chunk.getBlockState(pos);

                    if (blockState.isAir()) { // Skip if air block
                        runDepth = -1;
                        continue;
                    }

                    if (!blockState.isOf(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }

                    if (runDepth == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = surfaceConfig.topBlock();
                            fillerBlock = surfaceConfig.fillerBlock();

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        runDepth = surfaceDepth;
                        
                        if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                            BlockState fluidBlock = aquiferSampler.apply(x, y, z, 0.0, 0.0);
                            
                            boolean isAir = fluidBlock == null;
                            topBlock = isAir ? BlockStates.AIR : fluidBlock;
                            
                            this.scheduleFluidTick(chunk, aquiferSampler, pos, topBlock);
                        }
                        
                        blockState = (y >= this.seaLevel - 1 || (y < this.seaLevel - 1 && chunk.getBlockState(pos.up()).isAir())) ? 
                            topBlock : 
                            fillerBlock;
                        
                        chunk.setBlockState(pos, blockState, false);

                        continue;
                    }

                    if (runDepth <= 0) {
                        continue;
                    }

                    runDepth--;
                    chunk.setBlockState(pos, fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (runDepth == 0 && fillerBlock.isOf(BlockStates.SAND.getBlock())) {
                        runDepth = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }
    
    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
        int x = (startNoiseX / this.noiseSizeX * 16) + localNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
        int z = (startNoiseZ / this.noiseSizeZ * 16) + localNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;
        
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;

        double coordinateScale = 684.412D * this.xzScale; 
        double heightScale = 684.412D * this.yScale;
        
        double mainNoiseScaleX = this.xzFactor; // Default: 80
        double mainNoiseScaleY = this.yFactor;  // Default: 160
        double mainNoiseScaleZ = this.xzFactor;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        double baseSize = 8.5D;
        double heightStretch = 12D;
        
        // Density norm (sum of 16 octaves of noise / limitScale => [-128, 128])
        double densityScale = 128.0;
        double tunnelThreshold = 200.0 / densityScale;
        
        Clime clime = this.climateSampler.sample(x, z);
        double temp = clime.temp();
        double rain = clime.rain() * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;

        double scale = this.scaleNoiseOctaves.sampleXZ(noiseX, noiseZ, 1.121D, 1.121D);
        scale = (scale + 256D) / 512D;
        scale *= rain;
        
        if (scale > 1.0D) {
            scale = 1.0D;
        }
        
        double depth = this.depthNoiseOctaves.sampleXZ(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depth /= 8000D;

        if (depth < 0.0D) {
            depth = -depth * 0.3D;
        }

        depth = depth * 3D - 2D;

        if (depth < 0.0D) {
            depth /= 2D;

            if (depth < -1D) {
                depth = -1D;
            }

            depth /= 1.4D;
            depth /= 2D;

            scale = 0.0D;

        } else {
            if (depth > 1.0D) {
                depth = 1.0D;
            }
            depth /= 8D;
        }

        if (scale < 0.0D) {
            scale = 0.0D;
        }

        scale += 0.5D;
        depth = depth * baseSize / 8D;
        depth = baseSize + depth * 4D;
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            double density;
            double heightmapDensity;
            
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);
            
            // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
            double mainNoise = (this.mainNoiseOctaves.sample(
                noiseX, noiseZ, 0, noiseY, 
                coordinateScale / mainNoiseScaleX, 
                heightScale / mainNoiseScaleY, 
                coordinateScale / mainNoiseScaleZ
            ) / 10D + 1.0D) / 2D;
            
            if (mainNoise < 0.0D) {
                density = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseZ, 0, noiseY, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseZ, 0, noiseY, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseZ, 0, noiseY, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseZ, 0, noiseY, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }

            // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
            density -= densityOffset;
            
            // Normalize density
            density /= densityScale;
            
            // Sample without noise caves
            heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, tunnelThreshold, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applySlides(density, y);
            heightmapDensity = this.applySlides(heightmapDensity, y);
            
            primaryBuffer[y] = MathHelper.clamp(density, -64.0, 64.0);
            heightmapBuffer[y] = MathHelper.clamp(heightmapDensity, -64.0, 64.0);
        }
    }
    
    /*
     * MCPE uses different values to seed random surface generation.
     */
    @Override
    protected Random createSurfaceRandom(int chunkX, int chunkZ) {
        long seed = (long)chunkX * 0x14609048 + (long)chunkZ * 0x7ebe2d5;
        
        return new MTRandom(seed);
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
} 
