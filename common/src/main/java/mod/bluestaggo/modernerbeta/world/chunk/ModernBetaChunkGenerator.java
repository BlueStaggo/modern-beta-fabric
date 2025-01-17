package mod.bluestaggo.modernerbeta.world.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.registry.registries.DeferredRegister;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProvider;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import mod.bluestaggo.modernerbeta.util.BlockStates;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector;
import mod.bluestaggo.modernerbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import mod.bluestaggo.modernerbeta.world.carver.BetaCaveCarverConfig;
import mod.bluestaggo.modernerbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModernBetaChunkGenerator extends NoiseChunkGenerator {
    public static final MapCodec<ModernBetaChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
            ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
            NbtCompound.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
        ).apply(instance, instance.stable(ModernBetaChunkGenerator::new)));
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = DeferredRegister.create(ModernerBeta.MOD_ID, RegistryKeys.CHUNK_GENERATOR);

    private final RegistryEntry<ChunkGeneratorSettings> settings;
    private final NbtCompound chunkSettings;
    private final BiomeInjector biomeInjector;
    
    private ChunkProvider chunkProvider;
    
    public ModernBetaChunkGenerator(
        BiomeSource biomeSource,
        RegistryEntry<ChunkGeneratorSettings> settings,
        NbtCompound chunkProviderSettings
    ) {
        super(biomeSource, settings);

        this.settings = settings;
        this.chunkSettings = chunkProviderSettings;
        this.biomeInjector = this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource ?
            new BiomeInjector(this, modernBetaBiomeSource) : 
            null;
        
        if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
            modernBetaBiomeSource.setChunkGenerator(this);
        }
    }

    public void initProvider(long seed) {
        ModernBetaSettingsChunk chunkSettings = ModernBetaSettingsChunk.fromCompound(this.chunkSettings);

        this.chunkProvider = ModernBetaRegistries.CHUNK
            .get(chunkSettings.chunkProvider)
            .apply(this, seed);
        
        this.chunkProvider.initForestOctaveNoise();
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(Util.debugSupplier(() -> {
            ChunkNoiseSampler noiseSampler = chunk.getOrCreateChunkNoiseSampler(c -> this.createChunkNoiseSampler(c, structureAccessor, blender, noiseConfig));
            chunk.populateBiomes(this.biomeSource, noiseSampler.createMultiNoiseSampler(noiseConfig.getNoiseRouter(), this.settings.value().spawnTarget()));
            
            return chunk;
            
        }, () -> "init_biomes"), Util.getMainWorkerExecutor());
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        CompletableFuture<Chunk> completedChunk = this.chunkProvider.provideChunk(Blender.getNoBlending(), structureAccessor, chunk, noiseConfig);
        
        return completedChunk;
    }

    @Override
    public void buildSurface(ChunkRegion chunkRegion, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk) {
        this.injectBiomes(chunk, noiseConfig.getMultiNoiseSampler(), BiomeInjectionStep.PRE);

        if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.SURFACE)) {
            if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
                if (this.chunkProvider.getChunkSettings().useSurfaceRules) {
                    this.buildDefaultSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
                    this.chunkProvider.provideSurfaceExtra(chunkRegion, structureAccessor, chunk, modernBetaBiomeSource, noiseConfig);
                } else {
                    this.chunkProvider.provideSurface(chunkRegion, structureAccessor, chunk, modernBetaBiomeSource, noiseConfig);
                }
            } else {
                super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
            }
        }

        this.injectBiomes(chunk, noiseConfig.getMultiNoiseSampler(), BiomeInjectionStep.POST);
    }

    public void buildDefaultSurface(ChunkRegion chunkRegion, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk) {
        super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
    }
    
    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
        if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.CARVERS)) return;

        ModernBetaSettingsChunk chunkSettings = this.chunkProvider.getChunkSettings();

        BiomeAccess biomeAccessWithSource = biomeAccess.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, noiseConfig.getMultiNoiseSampler()));
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;
        
        AquiferSampler aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);
        
        // Chunk Noise Sampler used to sample surface level
        ChunkNoiseSampler chunkNoiseSampler = chunk.getOrCreateChunkNoiseSampler(c -> this.createChunkNoiseSampler(c, structureAccessor, Blender.getBlender(chunkRegion), noiseConfig));

        Registry<ConfiguredCarver<?>> configuredCarverRegistry = chunkRegion.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_CARVER);
        CarverContext carverContext = new CarverContext(this, chunkRegion.getRegistryManager(), chunk.getHeightLimitView(), chunkNoiseSampler, noiseConfig, this.settings.value().surfaceRule());
        CarvingMask carvingMask = ((ProtoChunk)chunk).getOrCreateCarvingMask();
        
        LocalRandom random = new LocalRandom(seed);
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;

        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                ChunkPos carverPos = new ChunkPos(chunkX, chunkZ);
                Chunk carverChunk = chunkRegion.getChunk(carverPos.x, carverPos.z);
                
                @SuppressWarnings("deprecation")
                GenerationSettings genSettings = carverChunk.getOrCreateGenerationSettings(() -> this.getGenerationSettings(
                    this.biomeSource.getBiome(BiomeCoords.fromBlock(carverPos.getStartX()), 0, BiomeCoords.fromBlock(carverPos.getStartZ()), noiseConfig.getMultiNoiseSampler()))
                );
                Iterable<RegistryEntry<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep();

                for(RegistryEntry<ConfiguredCarver<?>> carverEntry : carverList) {
                    ConfiguredCarver<?> configuredCarver = carverEntry.value();
                    random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);

                    if (chunkSettings.forceBetaCaves || chunkSettings.forceBetaRavines) {
                        RegistryKey<ConfiguredCarver<?>> carverKey = carverEntry.getKey().orElse(null);
                        if (carverKey != null) {
                            ConfiguredCarver<?> replacementCarver = null;
                            if (chunkSettings.forceBetaCaves) {
                                if (carverKey.equals(ConfiguredCarvers.CAVE)) {
                                    replacementCarver = configuredCarverRegistry.get(ModernBetaConfiguredCarvers.BETA_CAVE);
                                } else if (carverKey.equals(ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND)) {
                                    replacementCarver = configuredCarverRegistry.get(ModernBetaConfiguredCarvers.BETA_CAVE_DEEP);
                                }
                            }
                            if (chunkSettings.forceBetaRavines && carverKey.equals(ConfiguredCarvers.CANYON)) {
                                replacementCarver = configuredCarverRegistry.get(ModernBetaConfiguredCarvers.BETA_CANYON);
                            }

                            if (replacementCarver != null) {
                                configuredCarver = replacementCarver;
                            }
                        }
                    }

                    if (configuredCarver.shouldCarve(random)) {
                        if (configuredCarver.config() instanceof BetaCaveCarverConfig betaCaveCarverConfig) {
                            betaCaveCarverConfig.useFixedCaves = Optional.of(this.chunkProvider.getChunkSettings().useFixedCaves);
                        }

                        configuredCarver.carve(carverContext, chunk, biomeAccessWithSource::getBiome, random, aquiferSampler, carverPos, carvingMask);
                    }
                }
            }
        }
    }
    
    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos pos = chunk.getPos();
        
        if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.FEATURES)) 
            return;

        super.generateFeatures(world, chunk, structureAccessor);
    }
    
    @Override
    public void populateEntities(ChunkRegion region) {
        ChunkPos pos = region.getCenterPos();
        
        if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.ENTITY_SPAWN))
            return;
        
        super.populateEntities(region);
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type type, HeightLimitView world, NoiseConfig noiseConfig) {
        return this.chunkProvider.getHeight(x, z, type);
    }
    
    public int getHeight(int x, int z, Heightmap.Type type) {
        return this.chunkProvider.getHeight(x, z, type);
    }
  
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        int height = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG);
        int worldHeight = this.chunkProvider.getWorldHeight();
        int minY = this.chunkProvider.getWorldMinY();
        
        BlockState[] column = new BlockState[worldHeight];
        
        for (int y = worldHeight - 1; y >= 0; --y) {
            int worldY = y + minY;
            
            if (worldY > height) {
                if (worldY > this.getSeaLevel())
                    column[y] = BlockStates.AIR;
                else
                    column[y] = this.settings.value().defaultFluid();
            } else {
                column[y] = this.settings.value().defaultBlock();
            }
        }
        
        return new VerticalBlockSample(minY, column);
    }

    @Override
    public int getWorldHeight() {
        // TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
        // Affects both getWorldHeight() and getMinimumY().
        // See: MC-236933 and MC-236723
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().generationShapeConfig().height();
       
        return this.chunkProvider.getWorldHeight();
    }
    
    @Override
    public int getMinimumY() {
        if (this.chunkProvider == null)
            return this.getGeneratorSettings().value().generationShapeConfig().minimumY();
        
        return this.chunkProvider.getWorldMinY();
    }

    @Override
    public int getSeaLevel() {
        return this.chunkProvider.getSeaLevel();
    }
    
    public ChunkNoiseSampler createChunkNoiseSampler(Chunk chunk, StructureAccessor world, Blender blender, NoiseConfig noiseConfig) {
        return ChunkNoiseSampler.create(
            chunk,
            noiseConfig,
            StructureWeightSampler.createStructureWeightSampler(world, chunk.getPos()),
            this.settings.value(),
            this.chunkProvider.getFluidLevelSampler(),
            blender
        );
    }

    public RegistryEntry<ChunkGeneratorSettings> getGeneratorSettings() {
        return this.settings;
    }
    
    public ChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public NbtCompound getChunkSettings() {
        return this.chunkSettings;
    }
    
    public BiomeInjector getBiomeInjector() {
        return this.biomeInjector;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return ModernBetaChunkGenerator.CODEC;
    }
    
    private void injectBiomes(Chunk chunk, MultiNoiseSampler noiseSampler, BiomeInjectionStep step) {
        if (this.biomeInjector != null) {
            this.biomeInjector.injectBiomes(chunk, noiseSampler, step);
        }
    }

    public static void register() {
        CHUNK_GENERATOR.register(ModernerBeta.createId(ModernerBeta.MOD_ID), () -> CODEC);
        CHUNK_GENERATOR.register();
    }
}
