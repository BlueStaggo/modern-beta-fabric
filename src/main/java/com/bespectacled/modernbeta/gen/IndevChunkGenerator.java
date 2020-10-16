package com.bespectacled.modernbeta.gen;

import java.util.Random;
import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.IndevUtil.Theme;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class IndevChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<IndevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    AlphaGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(IndevChunkGenerator::new)));

    private final AlphaGeneratorSettings settings;
    
    private IndevNoiseGeneratorCombined heightNoise1;
    private IndevNoiseGeneratorCombined heightNoise2;
    
    private IndevNoiseGeneratorOctaves heightNoise3;
    private IndevNoiseGeneratorOctaves islandNoise;
    
    private IndevNoiseGeneratorOctaves noise5;
    private IndevNoiseGeneratorOctaves noise6;
    
    private IndevNoiseGeneratorCombined erodeNoise1;
    private IndevNoiseGeneratorCombined erodeNoise2;
    
    private IndevNoiseGeneratorOctaves sandNoiseOctaves;
    private IndevNoiseGeneratorOctaves gravelNoiseOctaves;

    private Block blockArr[][][];
    private int heightmap[]; // field_4180_q
    
    private final int width;
    private final int length;
    private final int height;
    private final int layers;
    private int waterLevel;
    
    private boolean pregenerated = false;
    private Random rand;

    IndevBiomeSource biomeSource;
    private final Theme theme;
    private final Type type;

    public static long seed;
    
    public IndevChunkGenerator(BiomeSource biomes, long seed, AlphaGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.seed = seed;
        this.rand = new Random(seed);
        this.biomeSource = (IndevBiomeSource) biomes;
        
        theme = Theme.NORMAL;
        type = Type.INLAND;
        
        this.width = 256;
        this.length = 256;
        this.height = 256;
        this.waterLevel = this.height / 2;
        this.layers = (this.type == Type.FLOATING) ? (this.height - 64) / 48 + 1 : 1;
        
        this.pregenerated = false;
        
        blockArr = new Block[this.width][this.height][this.length];
        fillBlockArr(blockArr);
        
        

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_ALPHA_NOISE_DECORATOR.setSeed(seed);
        ModernBeta.setBlockColorsSeed(0L, true);
        ModernBeta.SEED = seed;
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "indev"), CODEC);
        ModernBeta.LOGGER.log(Level.INFO, "Registered Indev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return IndevChunkGenerator.CODEC;
    }
    
    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkPos pos = chunk.getPos();
        
        if (inIndevRegion(pos.x, pos.z)) {
            if (!pregenerated) {
                blockArr = pregenerateTerrain(blockArr);
               
                pregenerated = true;   
            }
            
            setTerrain(chunk, blockArr);
           
            
        } else if (this.type != Type.FLOATING) {
            if (this.type == Type.ISLAND)
                generateWaterBorder(chunk);
            else {
                generateWorldBorder(chunk);
            }
        }
    }
    
    private void setTerrain(Chunk chunk, Block[][][] blockArr) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        int offsetX = chunkX * 16;
        int offsetZ = chunkZ * 16;
        
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    Block someBlock = blockArr[offsetX + x][y][offsetZ + z];
                    
                     if (!someBlock.equals(Blocks.AIR)) {
                        chunk.setBlockState(mutable.set(x, y, z), someBlock.getDefaultState(), false);
                    }
                }
            }
        }
    }
    
    private Block[][][] pregenerateTerrain(Block[][][] blockArr) {
        
        for (int l = 0; l < this.layers; ++l) { 
            // Floating island layer generation depends on water level being lowered on each iteration
            this.waterLevel = (this.type == Type.FLOATING) ? this.height - 32 - l * 48 : this.waterLevel; 
            
            // Noise Generators (Here instead of constructor to randomize floating layer generation)    
            heightNoise1 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
            heightNoise2 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
            
            heightNoise3 = new IndevNoiseGeneratorOctaves(this.rand, 6);
            islandNoise = new IndevNoiseGeneratorOctaves(this.rand, 2);
            
            noise5 = new IndevNoiseGeneratorOctaves(this.rand, 8);
            noise6 = new IndevNoiseGeneratorOctaves(this.rand, 8);
            
            erodeNoise1 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
            erodeNoise2 = new IndevNoiseGeneratorCombined(new IndevNoiseGeneratorOctaves(this.rand, 8), new IndevNoiseGeneratorOctaves(this.rand, 8));
            
            sandNoiseOctaves = new IndevNoiseGeneratorOctaves(this.rand, 8);
            gravelNoiseOctaves = new IndevNoiseGeneratorOctaves(this.rand, 8);
            
            heightmap = generateHeightmap(heightmap);
            erodeTerrain(heightmap);
             
            ModernBeta.LOGGER.log(Level.INFO, "[Indev] Soiling..");
            for (int x = 0; x < this.width; ++x) {
                 
                double var1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
                 
                 //relZ = 0;
                 for (int z = 0; z < this.length; ++z) {
                     
                     double var2 = Math.max(var1, Math.abs(z / (this.length - 1.0) - 0.5) * 2.0);
                     var2 = var2 * var2 * var2;
                     
                     int dirtTransition = heightmap[x + z * this.width] + this.waterLevel;
                     int dirtThickness = (int)(noise5.IndevNoiseGenerator(x, z) / 24.0) - 4;
                 
                     int stoneTransition = dirtTransition + dirtThickness;
                     heightmap[x + z * this.width] = Math.max(dirtTransition, stoneTransition);
                     
                     if (heightmap[x + z * this.width] > this.height - 2) {
                         heightmap[x + z * this.width] = this.height - 2;
                     }
                     
                     if (heightmap[x + z * this.width] <= 0) {
                         heightmap[x + z * this.width] = 1;
                     }
                     
                     double var4 = noise6.IndevNoiseGenerator(x * 2.3, z * 2.3) / 24.0;
                     int var5 = (int)(Math.sqrt(Math.abs(var4)) * Math.signum(var4) * 20.0) + this.waterLevel;
                     var5 = (int)(var5 * (1.0 - var2) + var2 * this.height);
                     
                     if (var5 > this.waterLevel) {
                         var5 = this.height;
                     }
                     
                     for (int y = 0; y < this.height; ++y) {
                         Block blockToSet = Blocks.AIR;
                         
                         if (y <= dirtTransition)
                             blockToSet = Blocks.DIRT;
                         
                         if (y <= stoneTransition)
                             blockToSet = Blocks.STONE;
                         
                         if (this.type == Type.FLOATING && y < var5)
                             blockToSet = Blocks.AIR;

                         Block someBlock = blockArr[x][y][z];
                         
                         if (someBlock.equals(Blocks.AIR)) {
                             blockArr[x][y][z] = blockToSet;
                         }
                     }
                 }
            }
            
            buildIndevSurface(blockArr, heightmap);
            floodTerrain(blockArr);
            plantIndevSurface(blockArr);
        }
        
        return blockArr;
    }
    
    
    private int[] generateHeightmap(int heightmap[]) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Raising..");
        
        if (heightmap == null) {
            heightmap = new int[this.width * this.length]; // For entire indev world
        }
        
        for (int x = 0; x < this.width; ++x) {
            double islandVar1 = Math.abs((x / (this.width - 1.0) - 0.5) * 2.0);
            
            for (int z = 0; z < this.length; ++z) {
                double islandVar2 = Math.abs((z / (this.length - 1.0) - 0.5) * 2.0);
                
                double heightLow = heightNoise1.IndevNoiseGenerator(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
                double heightHigh = heightNoise2.IndevNoiseGenerator(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;
                
                double heightCheck = heightNoise3.IndevNoiseGenerator(x, z) / 8.0;
                
                if (heightCheck > 0.0) {
                    heightHigh = heightLow;
                }
                
                double heightResult = Math.max(heightLow, heightHigh) / 2.0;
                
                //if (islandGen) {
                if (this.type == Type.ISLAND) {
                    double islandVar3 = Math.sqrt(islandVar1 * islandVar1 + islandVar2 * islandVar2) * 1.2000000476837158;
                    islandVar3 = Math.min(islandVar3, islandNoise.IndevNoiseGenerator(x * 0.05f, z * 0.05f) / 4.0 + 1.0);
                    islandVar3 = Math.max(islandVar3, Math.max(islandVar1, islandVar2));
                    
                    if (islandVar3 > 1.0) {
                        islandVar3 = 1.0;
                    } else if (islandVar3 < 0.0) {
                        islandVar3 = 0.0;
                    }
                    
                    islandVar3 *= islandVar3;
                    heightResult = heightResult * (1.0 - islandVar3) - islandVar3 * 10.0 + 5.0;
                    
                    if (heightResult < 0.0) {
                        heightResult -= heightResult * heightResult * 0.20000000298023224;
                    }
                            
                            
                } else if (heightResult < 0.0) {
                    heightResult *= 0.8;
                }
                
                heightmap[x + z * this.width] = (int)heightResult;
            }
            
        }
       
        return heightmap;
        
    }
    
    private void erodeTerrain(int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Eroding..");
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                double var1 = erodeNoise1.IndevNoiseGenerator(x << 1, z << 1) / 8.0;
                int var2 = erodeNoise2.IndevNoiseGenerator(x << 1, z << 1) > 0.0 ? 1 : 0;
            
                if (var1 > 2.0) {
                    int var3 = heightmap[x + z * this.width];
                    var3 = ((var3 - var2) / 2 << 1) + var2;
                    
                    heightmap[x + z * this.width] = var3;
                }
            }
        }
    }
    
    private void buildIndevSurface(Block[][][] blockArr, int[] heightmap) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Growing..");
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                boolean genSand = sandNoiseOctaves.IndevNoiseGenerator(x, z) > 8.0;
                boolean genGravel = gravelNoiseOctaves.IndevNoiseGenerator(x, z) > 12.0;
                
                if (this.type == Type.ISLAND) {
                    genSand = sandNoiseOctaves.IndevNoiseGenerator(x, z) > -8.0;
                }
                
                int heightResult = heightmap[x + z *  this.width];
                Block block = blockArr[x][heightResult][z];
                Block blockAbove = blockArr[x][heightResult + 1][z];
                
                if ((blockAbove.equals(Blocks.WATER) || blockAbove.equals(Blocks.AIR)) && heightResult <= this.waterLevel - 1 && genGravel) {
                    blockArr[x][heightResult][z] = Blocks.GRAVEL;
                }
                
     
                if (blockAbove.equals(Blocks.AIR)) {
                    Block surfaceBlock = null;
                    
                    if (heightResult <= this.waterLevel - 1 && genSand) {
                        surfaceBlock = Blocks.SAND; 
                    }
                    
                    if (!block.equals(Blocks.AIR) && surfaceBlock != null) {
                        blockArr[x][heightResult][z] = surfaceBlock;
                    }
                }
            }
        }
    }
    
    private void floodTerrain(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Watering..");
        
        if (this.type == Type.FLOATING) {
            return;
        }
        
        if (this.type == Type.ISLAND) {
            floodIsland(blockArr);
        }
        
        int waterSourceCount = this.width * this.length / 800;
        
        for (int i = 0; i < waterSourceCount; ++i) {
            int randX = random.nextInt(this.width);
            int randZ = random.nextInt(this.length);
            int y = random.nextBoolean() ? waterLevel - 1 : waterLevel - 2;
            
            flood(blockArr, randX, y, randZ);
        }
    }
    
    private void flood(Block[][][] blockArr, int x, int y, int z) {
        if (x < 0 || z < 0 || x > this.width - 1 || z > this.length - 1) return;
        
        Block someBlock = blockArr[x][y][z];
        
        if (someBlock.equals(Blocks.AIR)) {
            blockArr[x][y][z] = Blocks.WATER;
            
            flood(blockArr, x, y - 1, z);
            flood(blockArr, x - 1, y, z);
            flood(blockArr, x + 1, y, z);
            flood(blockArr, x, y, z - 1);
            flood(blockArr, x, y, z + 1);
        }
    }
    
    private void floodIsland(Block[][][] blockArr) {
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    Block someBlock = blockArr[x][y][z];
                    
                    if (someBlock.equals(Blocks.AIR) && y < waterLevel) {
                        blockArr[x][y][z] = Blocks.WATER;
                    }
                }
            }
        }
    }
    
    private void plantIndevSurface(Block[][][] blockArr) {
        ModernBeta.LOGGER.log(Level.INFO, "[Indev] Planting..");
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height - 2; ++y) {
                    Block block = blockArr[x][y][z];
                    Block blockAbove = blockArr[x][y + 1][z];
                    
                    if (block.equals(Blocks.DIRT) && blockAbove.equals(Blocks.AIR)) {
                        blockArr[x][y][z] = Blocks.GRASS_BLOCK;
                    }
                }
            }
        }
    }
    
    private void generateWorldBorder(Chunk chunk) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
         
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                    } else if (y == this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.GRASS_BLOCK.getDefaultState(), false);
                    }
                }
            }
        }
    }
    
    private void generateWaterBorder(Chunk chunk) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
         
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    if (y < this.waterLevel - 10) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                    } else if (y == this.waterLevel - 10) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.DIRT.getDefaultState(), false);
                    } else if (y < this.waterLevel) {
                        chunk.setBlockState(mutable.set(x, y, z), Blocks.WATER.getDefaultState(), false);
                    }
                }
            }
        }
    }
    
    private void countBlocks(Block[][][] blockArr) {
        int countStone = 0;
        int countDirt = 0;
        int countAir = 0;
        
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for(int y = 0; y < this.height; ++y) {
                    Block someBlock = blockArr[x][y][z];
                    
                    if (someBlock.equals(Blocks.STONE)) countStone++;
                    if (someBlock.equals(Blocks.DIRT)) countDirt++;
                    if (someBlock.equals(Blocks.AIR)) countAir++;
                }
            }
        }
        
        System.out.println("Block count, stone/dirt/air: " + countStone + ", " + countDirt + ", " + countAir);
    }
    
    private void fillBlockArr(Block[][][] blockArr) {
        for (int x = 0; x < this.width; ++x) {
            for (int z = 0; z < this.length; ++z) {
                for (int y = 0; y < this.height; ++y) {
                    blockArr[x][y][z] = Blocks.AIR;
                }
            }
        }
    }
    
    
    private boolean inIndevRegion(int chunkX, int chunkZ) {
        int chunkWidth = this.width / 16;
        int chunkLength = this.length / 16;
        
        if (chunkX >= 0 && chunkX < chunkWidth && chunkZ >= 0 && chunkZ < chunkLength)
            return true;
        
        return false;
    }
    
    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
        // Do nothing, for now.
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        int height = this.waterLevel + 1;
        
        if (x < 0 || x > 255 || z < 0 || z > 255) return height;
        
        if (!pregenerated) {
            blockArr = pregenerateTerrain(blockArr);
            pregenerated = true;
        }
        
        for (int y = this.height - 1; y >= 0; --y) {
            Block someBlock = this.blockArr[x][y][z];
            
            if (!someBlock.equals(Blocks.AIR)) {
                break;
            }
            height = y;
        }
         
        return height;
    }

    @Override
    public int getMaxY() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return this.waterLevel;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new IndevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }

}