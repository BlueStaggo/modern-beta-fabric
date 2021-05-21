package com.bespectacled.modernbeta.world.biome.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.bespectacled.modernbeta.api.world.WorldSettings;
import com.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import com.bespectacled.modernbeta.util.NBTUtil;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class SingleBiomeProvider extends BiomeProvider {
    private static final Identifier DEFAULT_BIOME_ID = new Identifier("plains");
    
    private final Identifier biomeId;
    
    public SingleBiomeProvider(long seed, NbtCompound settings) {
        super(seed, settings);
        
        this.biomeId = new Identifier(NBTUtil.readString(WorldSettings.TAG_SINGLE_BIOME, settings, DEFAULT_BIOME_ID.toString()));
    }

    @Override
    public Biome getBiomeForNoiseGen(Registry<Biome> biomeRegistry, int biomeX, int biomeY, int biomeZ) {
        Optional<Biome> biome = biomeRegistry.getOrEmpty(biomeId);
        
        // If custom biome is not present for whatever reason, fetch the default.
        return biome.orElse(biomeRegistry.get(DEFAULT_BIOME_ID));
    }
    
    @Override
    public List<RegistryKey<Biome>> getBiomesForRegistry() {
        return Arrays.asList(RegistryKey.of(Registry.BIOME_KEY, this.biomeId));
    }
}