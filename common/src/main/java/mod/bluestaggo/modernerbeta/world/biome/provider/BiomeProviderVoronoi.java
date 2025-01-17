package mod.bluestaggo.modernerbeta.world.biome.provider;

import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverOcean;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkCache;
import mod.bluestaggo.modernerbeta.util.chunk.ChunkClimate;
import mod.bluestaggo.modernerbeta.util.noise.SimplexOctaveNoise;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateMapping;
import mod.bluestaggo.modernerbeta.world.biome.provider.climate.ClimateType;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bluestaggo.modernerbeta.world.biome.voronoi.VoronoiPointRules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BiomeProviderVoronoi extends BiomeProvider implements BiomeResolverOcean {
    private final VoronoiClimateSampler climateSampler;
    private final VoronoiPointRules<ClimateMapping, Clime> rules;
    
    public BiomeProviderVoronoi(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
        super(settings, biomeRegistry, seed);
        
        this.climateSampler = new VoronoiClimateSampler(
            seed,
            this.settings.climateTempNoiseScale,
            this.settings.climateRainNoiseScale,
            this.settings.climateDetailNoiseScale,
            this.settings.climateWeirdNoiseScale
        );
        this.rules = buildRules(this.settings.voronoiPoints);
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.LAND));
    }
 
    @Override
    public RegistryEntry<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.OCEAN));
    }
    
    @Override
    public RegistryEntry<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
        ClimateMapping climateMapping = this.getClimateMapping(biomeX, biomeZ);
        
        return this.biomeRegistry.getOrThrow(climateMapping.getBiome(ClimateType.DEEP_OCEAN));
    }

    @Override
    public List<RegistryEntry<Biome>> getBiomes() {
        List<String> biomes = new ArrayList<>();
        
        this.rules.getItems().forEach(key -> biomes.add(key.biome()));
        this.rules.getItems().forEach(key -> biomes.add(key.oceanBiome()));
        this.rules.getItems().forEach(key -> biomes.add(key.deepOceanBiome()));
        
        return biomes
            .stream()
            .distinct()
            .map(key -> this.biomeRegistry.getOrThrow(RegistryKey.of(RegistryKeys.BIOME, Identifier.of(key))))
            .collect(Collectors.toList());
    }
    
    private ClimateMapping getClimateMapping(int biomeX, int biomeZ) {
        int x = biomeX << 2;
        int z = biomeZ << 2;
        
        Clime clime = this.climateSampler.sample(x, z);

        return this.rules.calculateClosestTo(clime);
    }
    
    private static VoronoiPointRules<ClimateMapping, Clime> buildRules(List<VoronoiPointBiome> points) {
        VoronoiPointRules.Builder<ClimateMapping, Clime> builder = new VoronoiPointRules.Builder<>();
        
        for (VoronoiPointBiome point : points) {
            String biome = point.biome().isBlank() ? null : point.biome();
            String oceanBiome = point.oceanBiome().isBlank() ? null : point.oceanBiome();
            String deepOceanBiome = point.deepOceanBiome().isBlank() ? null : point.deepOceanBiome();
            
            double temp = MathHelper.clamp(point.temp(), 0.0, 1.0);
            double rain = MathHelper.clamp(point.rain(), 0.0, 1.0);
            double weird = MathHelper.clamp(point.weird(), 0.0, 1.0);
            
            ClimateMapping climateMapping = new ClimateMapping(biome, oceanBiome, deepOceanBiome);
            Clime clime = new Clime(temp, rain, weird);
            
            builder.add(climateMapping, clime);
        }
        
        return builder.build();
    }
    
    private static class VoronoiClimateSampler {
        private final SimplexOctaveNoise tempOctaveNoise;
        private final SimplexOctaveNoise rainOctaveNoise;
        private final SimplexOctaveNoise detailOctaveNoise;
        private final SimplexOctaveNoise weirdOctaveNoise;
        
        private final ChunkCache<ChunkClimate> chunkCacheClimate;
        
        private final double tempNoiseScale;
        private final double rainNoiseScale;
        private final double detailNoiseScale;
        private final double weirdNoiseScale;
        
        public VoronoiClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale, double weirdNoiseScale) {
            this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
            this.rainOctaveNoise = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
            this.detailOctaveNoise = new SimplexOctaveNoise(new Random(seed * 543321L), 2);
            this.weirdOctaveNoise = new SimplexOctaveNoise(new Random(seed * 134714L), 2);
            
            this.chunkCacheClimate = new ChunkCache<>(
                "climate",
                (chunkX, chunkZ) -> new ChunkClimate(chunkX, chunkZ, this::sampleNoise)
            );
            
            this.tempNoiseScale = tempNoiseScale;
            this.rainNoiseScale = rainNoiseScale;
            this.detailNoiseScale = detailNoiseScale;
            this.weirdNoiseScale = weirdNoiseScale;
        }

        public Clime sample(int x, int z) {
            int chunkX = x >> 4;
            int chunkZ = z >> 4;
            
            return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z);
        }
        
        public Clime sampleNoise(int x, int z) {
            double temp = this.tempOctaveNoise.sample(x, z, this.tempNoiseScale, 0.25D);
            double rain = this.rainOctaveNoise.sample(x, z, this.rainNoiseScale, 0.33333333333333331D);
            double detail = this.detailOctaveNoise.sample(x, z, this.detailNoiseScale, 0.58823529411764708D);
            double weird = this.weirdOctaveNoise.sample(x, z, this.weirdNoiseScale, 0.2941176471D);

            detail = detail * 1.1D + 0.5D;
            weird = (weird / 1.525D + 1.0D) / 2.0D;

            temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
            rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

            temp = 1.0D - (1.0D - temp) * (1.0D - temp);
            
            temp = MathHelper.clamp(temp, 0.0, 1.0);
            rain = MathHelper.clamp(rain, 0.0, 1.0);
            weird = MathHelper.clamp(weird, 0.0, 1.0);
            
            return new Clime(temp, rain, weird);
        }
    }
}
