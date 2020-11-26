package com.bespectacled.modernbeta.gen.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.bespectacled.modernbeta.biome.BetaBiomes;
import com.bespectacled.modernbeta.biome.IOldBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BiomeUtil;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;

public class BetaChunkProvider implements IOldChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise stoneNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];

    private double mainNoise[]; 
    private double minLimitNoise[];
    private double maxLimitNoise[];

    private double scaleNoise[];
    private double depthNoise[];
    
    private static final double HEIGHTMAP[] = new double[425];
    
    private static final Mutable POS = new Mutable();
    private static final Random RAND = new Random();
    
    private static final double[] TEMPS = new double[256];
    private static final double[] HUMIDS = new double[256];
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = new ObjectArrayList<StructurePiece>(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = new ObjectArrayList<JigsawJunction>(32);
    
    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final Identifier[] BIOMES = new Identifier[256];
    
    public BetaChunkProvider(long seed) {
        RAND.setSeed(seed);

        // Noise Generators
        minLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        maxLimitNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        mainNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);
        beachNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        stoneNoiseOctaves = new PerlinOctaveNoise(RAND, 4, false);
        scaleNoiseOctaves = new PerlinOctaveNoise(RAND, 10, false);
        depthNoiseOctaves = new PerlinOctaveNoise(RAND, 16, false);
        forestNoiseOctaves = new PerlinOctaveNoise(RAND, 8, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_BETA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    @Override
    public void makeChunk(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk, IOldBiomeSource biomeSource) {
        RAND.setSeed((long) chunk.getPos().x * 0x4f9939f508L + (long) chunk.getPos().z * 0x1ef1565bd5L);

        BiomeUtil.fetchTempHumid(chunk.getPos().x << 4, chunk.getPos().z << 4, TEMPS, HUMIDS);
        generateTerrain(chunk, TEMPS, structureAccessor);
    }
    
    @Override
    public void makeSurface(ChunkRegion region, Chunk chunk, IOldBiomeSource biomeSource) {
        byte seaLevel = (byte) 64;
        double thirtysecond = 0.03125D; // eighth

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        BiomeUtil.fetchTempHumid(chunkX << 4, chunkZ << 4, TEMPS, HUMIDS);
        BetaBiomes.getBiomesFromLookup(TEMPS, HUMIDS, BIOMES, null);
        
        Biome curBiome;

        sandNoise = beachNoiseOctaves.sampleOctavesArrBeta(sandNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond, thirtysecond, 1.0D);
        gravelNoise = beachNoiseOctaves.sampleOctavesArrBeta(gravelNoise, chunkX * 16, 109.0134D, chunkZ * 16, 16, 1,
                16, thirtysecond, 1.0D, thirtysecond);
        stoneNoise = stoneNoiseOctaves.sampleOctavesArrBeta(stoneNoise, chunkX * 16, chunkZ * 16, 0.0D, 16, 16, 1,
                thirtysecond * 2D, thirtysecond * 2D, thirtysecond * 2D);

        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {

                boolean genSandBeach = sandNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[z + x * 16] + RAND.nextDouble() * 0.20000000000000001D > 3D;

                int genStone = (int) (stoneNoise[z + x * 16] / 3D + 3D + RAND.nextDouble() * 0.25D);
                int flag = -1;
                
                int absX = chunk.getPos().getStartX() + x;
                int absZ = chunk.getPos().getStartZ() + z;
                    
                curBiome = biomeSource.isBeta() ? 
                    biomeSource.getBiomeRegistry().get(BIOMES[z + x * 16]) :
                    region.getBiome(POS.set(absX, 0, absZ));    

                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;

                // Generate from top to bottom of world
                for (int y = 127; y >= 0; y--) {

                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + RAND.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), BlockStates.BEDROCK, false);
                        continue;
                    }

                    Block someBlock = chunk.getBlockState(POS.set(x, y, z)).getBlock();

                    if (someBlock.equals(Blocks.AIR)) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!someBlock.equals(Blocks.STONE)) { // Skip if not stone
                        continue;
                    }

                    if (flag == -1) {
                        if (genStone <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = BlockStates.STONE;
                        } else if (y >= seaLevel - 4 && y <= seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        if (y < seaLevel && topBlock.equals(BlockStates.AIR)) { // Generate water bodies
                            topBlock = BlockStates.WATER;
                        }

                        flag = genStone;
                        if (y >= seaLevel - 1) {
                            chunk.setBlockState(POS.set(x, y, z), topBlock, false);
                        } else {
                            chunk.setBlockState(POS.set(x, y, z), fillerBlock, false);
                        }

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(POS.set(x, y, z), fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                        flag = RAND.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    public int getHeight(int x, int z, Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            BiomeUtil.fetchTempHumid((x >> 4) << 4, (z >> 4) << 4, TEMPS, HUMIDS);
            
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);
        
        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < 64)
            groundHeight = 64;

        return groundHeight;
    }
    
    private void generateTerrain(Chunk chunk, double[] temps, StructureAccessor structureAccessor) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        //int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);

        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();

        generateHeightmap(chunk.getPos().x * byte4, 0, chunk.getPos().z * byte4);
        
        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth; 
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double quarter = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * quarter; // Lerp
                        double var13 = (var4 - var2) * quarter;
                        
                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14; 

                            int absX = (chunk.getPos().x << 4) + i * 4 + m;

                            for (int n = 0; n < 4; n++) {
                                int absZ = (chunk.getPos().z << 4) + j * 4 + n;
                                double temp = temps[(i * 4 + m) * 16 + (j * 4 + n)];
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                clampedDensity += GenUtil.addStructDensity(
                                    structureListIterator, 
                                    jigsawListIterator, 
                                    STRUCTURE_LIST.size(), 
                                    JIGSAW_LIST.size(), 
                                    absX, y, absZ);

                                BlockState blockToSet = IOldChunkProvider.getBlockState(clampedDensity, y, temp);

                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);

                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }
    }
    
    private void generateHeightmap(int x, int y, int z) {
        byte byte4 = 4;
        byte byte17 = 17;

        int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;

        // Var names taken from old customized preset names
        double coordinateScale = 684.41200000000003D; // d
        double heightScale = 684.41200000000003D; // d1

        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        double depthNoiseScaleExponent = 0.5D;

        double mainNoiseScaleX = 80D;
        double mainNoiseScaleY = 160D;
        double mainNoiseScaleZ = 80D;

        double lowerLimitScale = 512D;
        double upperLimitScale = 512D;

        scaleNoise = scaleNoiseOctaves.sampleOctavesArrBeta(scaleNoise, x, z, int5_0, int5_1, 1.121D, 1.121D, 0.5D);
        depthNoise = depthNoiseOctaves.sampleOctavesArrBeta(depthNoise, x, z, int5_0, int5_1, depthNoiseScaleX, depthNoiseScaleZ, depthNoiseScaleExponent);

        mainNoise = mainNoiseOctaves.sampleOctavesArrBeta(
            mainNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale / mainNoiseScaleX, 
            heightScale / mainNoiseScaleY, 
            coordinateScale / mainNoiseScaleZ
        );

        minLimitNoise = minLimitNoiseOctaves.sampleOctavesArrBeta(
            minLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        maxLimitNoise = maxLimitNoiseOctaves.sampleOctavesArrBeta(
            maxLimitNoise, 
            x, y, z, 
            int5_0, byte17, int5_1,
            coordinateScale, 
            heightScale, 
            coordinateScale
        );

        int i = 0;
        int j = 0;
        int k = 16 / int5_0;

        for (int l = 0; l < int5_0; l++) {
            int idx0 = l * k + k / 2;

            for (int m = 0; m < int5_1; m++) {
                int idx1 = m * k + k / 2;

                double curTemp = TEMPS[idx0 * 16 + idx1];
                double curHumid = HUMIDS[idx0 * 16 + idx1] * curTemp;

                double humidMod = 1.0D - curHumid;
                humidMod *= humidMod;
                humidMod *= humidMod;
                humidMod = 1.0D - humidMod;

                double scaleMod = (scaleNoise[j] + 256D) / 512D;
                scaleMod *= humidMod;

                if (scaleMod > 1.0D) {
                    scaleMod = 1.0D;
                }

                double depthMod = depthNoise[j] / 8000D;

                if (depthMod < 0.0D) {
                    depthMod = -depthMod * 0.29999999999999999D;
                }

                depthMod = depthMod * 3D - 2D;

                if (depthMod < 0.0D) {
                    depthMod /= 2D;

                    if (depthMod < -1D) {
                        depthMod = -1D;
                    }

                    depthMod /= 1.3999999999999999D;
                    depthMod /= 2D;

                    scaleMod = 0.0D;

                } else {
                    if (depthMod > 1.0D) {
                        depthMod = 1.0D;
                    }
                    depthMod /= 8D;
                }

                if (scaleMod < 0.0D) {
                    scaleMod = 0.0D;
                }

                scaleMod += 0.5D;
                depthMod = (depthMod * (double) byte17) / 16D;

                double depthMod2 = (double) byte17 / 2D + depthMod * 4D;

                j++;

                for (int n = 0; n < byte17; n++) {
                    double heightVal = 0.0D;
                    double scaleMod2 = (((double) n - depthMod2) * 12D) / scaleMod;

                    if (scaleMod2 < 0.0D) {
                        scaleMod2 *= 4D;
                    }

                    double minLimitMod = minLimitNoise[i] / lowerLimitScale;
                    double maxLimitMod = maxLimitNoise[i] / upperLimitScale;
                    double mainNoiseMod = (mainNoise[i] / 10D + 1.0D) / 2D;

                    if (mainNoiseMod < 0.0D) {
                        heightVal = minLimitMod;
                    } else if (mainNoiseMod > 1.0D) {
                        heightVal = maxLimitMod;
                    } else {
                        heightVal = minLimitMod + (maxLimitMod - minLimitMod) * mainNoiseMod;
                    }
                    heightVal -= scaleMod2;

                    if (n > byte17 - 4) {
                        double d13 = (float) (n - (byte17 - 4)) / 3F;
                        heightVal = heightVal * (1.0D - d13) + -10D * d13;
                    }
                    
                    HEIGHTMAP[i] = heightVal;
                    i++;
                }
            }
        }
    }

    private void sampleHeightmap(int sampleX, int sampleZ) {
        byte byte4 = 4;
        // byte seaLevel = (byte)this.getSeaLevel();
        byte byte17 = 17;

        //int int5_0 = byte4 + 1;
        int int5_1 = byte4 + 1;
        
        int chunkX = sampleX >> 4;
        int chunkZ = sampleZ >> 4;

        generateHeightmap(chunkX * byte4, 0, chunkZ * byte4);

        for (int i = 0; i < byte4; i++) {
            for (int j = 0; j < byte4; j++) {
                for (int k = 0; k < 16; k++) {
                    double eighth = 0.125D;

                    double var1 = HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var2 = HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 0)];
                    double var3 = HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 0)];
                    double var4 = HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 0)];

                    double var5 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var1) * eighth;
                    double var6 = (HEIGHTMAP[((i + 0) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var2) * eighth;
                    double var7 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 0)) * byte17 + (k + 1)] - var3) * eighth;
                    double var8 = (HEIGHTMAP[((i + 1) * int5_1 + (j + 1)) * byte17 + (k + 1)] - var4) * eighth;

                    for (int l = 0; l < 8; l++) {
                        double var9 = 0.25D;
                        double var10 = var1;
                        double var11 = var2;
                        double var12 = (var3 - var1) * var9;
                        double var13 = (var4 - var2) * var9;

                        for (int m = 0; m < 4; m++) {
                            int x = (m + i * 4);
                            int y = k * 8 + l;
                            int z = (j * 4);

                            double var14 = 0.25D;
                            double density = var10; // var15
                            double var16 = (var11 - var10) * var14;

                            for (int n = 0; n < 4; n++) {
                                if (density > 0.0) {
                                    CHUNK_Y[x][z] = y;
                                }

                                ++z;
                                density += var16;
                            }

                            var10 += var12;
                            var11 += var13;
                        }

                        var1 += var5;
                        var2 += var6;
                        var3 += var7;
                        var4 += var8;
                    }
                }
            }
        }

        for (int pX = 0; pX < CHUNK_Y.length; pX++) {
            for (int pZ = 0; pZ < CHUNK_Y[pX].length; pZ++) {
                BlockPos structPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                
                GROUND_CACHE_Y.put(structPos, CHUNK_Y[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
    }

    
}
