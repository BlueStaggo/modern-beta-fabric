package mod.bespectacled.modernbeta.world.cavebiome.provider;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClime;
import mod.bespectacled.modernbeta.config.ModernBetaConfigCaveBiome.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointRules;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class VoronoiCaveBiomeProvider extends CaveBiomeProvider implements CaveClimateSampler {
    private VoronoiCaveClimateSampler climateSampler;
    private VoronoiPointRules<RegistryKey<Biome>, CaveClime> rules;

    public VoronoiCaveBiomeProvider(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry) {
        super(settings, biomeRegistry);
    }
    
    @Override
    public boolean initProvider(long seed) {
        this.climateSampler = new VoronoiCaveClimateSampler(
            seed,
            this.settings.voronoiVerticalNoiseScale,
            this.settings.voronoiHorizontalNoiseScale,
            this.settings.voronoiDepthMinY,
            this.settings.voronoiDepthMaxY
        );
        this.rules = buildRules(this.settings.voronoiPoints);
        
        return true;
    }

    @Override
    public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
        CaveClime clime = this.sample(biomeX, biomeY, biomeZ);
        RegistryKey<Biome> biomeKey = this.rules.calculateClosestTo(clime);
        
        return biomeKey == null ? null : this.biomeRegistry.getOrThrow(biomeKey);
    }
    
    @Override
    public List<RegistryEntry<Biome>> getBiomesForRegistry() {        
        return this.rules.getItems().stream().distinct().map(key -> this.biomeRegistry.getOrThrow(key)).collect(Collectors.toList());
    }

    @Override
    public CaveClime sample(int x, int y, int z) {
        return this.climateSampler.sample(x, y, z);
    }
    
    private static VoronoiPointRules<RegistryKey<Biome>, CaveClime> buildRules(List<VoronoiPointCaveBiome> points) {
        VoronoiPointRules.Builder<RegistryKey<Biome>, CaveClime> builder = new VoronoiPointRules.Builder<>();
        
        for (VoronoiPointCaveBiome point : points) {
            RegistryKey<Biome> biomeKey = point.biome.isBlank() ? null : RegistryKey.of(RegistryKeys.BIOME, new Identifier(point.biome));
            
            builder.add(biomeKey, new CaveClime(point.temp, point.rain, point.depth));
        }
        
        return builder.build();
    }

    private static class VoronoiCaveClimateSampler {
        private final PerlinOctaveNoise tempNoiseOctaves;
        private final PerlinOctaveNoise rainNoiseOctaves;
        private final PerlinOctaveNoise detailNoiseOctaves;
        
        private final float verticalScale;
        private final float horizontalScale;
        
        private final int depthMinY;
        private final int depthMaxY;
        
        public VoronoiCaveClimateSampler(long seed, float verticalScale, float horizontalScale, int depthMinY, int depthMaxY) {
            this.tempNoiseOctaves = new PerlinOctaveNoise(new Random(seed * 9871L), 2, true);
            this.rainNoiseOctaves = new PerlinOctaveNoise(new Random(seed * 39811L), 2, true);
            this.detailNoiseOctaves = new PerlinOctaveNoise(new Random(seed * 543321L), 1, true);
            
            this.verticalScale = verticalScale;
            this.horizontalScale = horizontalScale;
            
            this.depthMinY = depthMinY >> 2;
            this.depthMaxY = depthMaxY >> 2;
        }
        
        public CaveClime sample(int x, int y, int z) {
            // 1 Octave range: -0.6240559817912857/0.6169702737066762
            // 2 Octave range: -1.4281536012354779/1.4303502066204832
            // 4 Octave range: -7.6556244276339145/7.410194314594666
            
            double tempNoise = this.tempNoiseOctaves.sample(
                x / (double)this.horizontalScale,
                y / (double)this.verticalScale,
                z / (double)this.horizontalScale
            );
            
            double rainNoise = this.rainNoiseOctaves.sample(
                x / (double)this.horizontalScale, 
                y / (double)this.verticalScale, 
                z / (double)this.horizontalScale
            );
            
            double detailNoise = this.detailNoiseOctaves.sample(
                x / (double)this.horizontalScale, 
                y / (double)this.verticalScale, 
                z / (double)this.horizontalScale
            );
            
            tempNoise /= 1.4D;
            rainNoise /= 1.4D;
            detailNoise /= 0.55D;
            
            tempNoise = tempNoise * 0.99D + detailNoise * 0.01D;
            rainNoise = rainNoise * 0.98D + detailNoise * 0.02D;
            
            tempNoise = (tempNoise + 1.0) / 2D;
            rainNoise = (rainNoise + 1.0) / 2D;
            
            int depthHeight = this.depthMaxY - this.depthMinY;
            double depth = MathHelper.clamp(y, this.depthMinY, this.depthMaxY);
            
            depth -= this.depthMinY;
            depth /= depthHeight;
            
            return new CaveClime(
                MathHelper.clamp(tempNoise, 0.0, 1.0),
                MathHelper.clamp(rainNoise, 0.0, 1.0),
                MathHelper.clamp(depth, 0.0, 1.0)
            );
        }
    }
}
