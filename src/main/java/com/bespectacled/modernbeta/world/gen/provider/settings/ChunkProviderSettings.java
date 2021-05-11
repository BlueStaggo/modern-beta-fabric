package com.bespectacled.modernbeta.world.gen.provider.settings;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaGenerationConfig;

import net.minecraft.nbt.NbtCompound;

public class ChunkProviderSettings {
    protected static final ModernBetaGenerationConfig CONFIG = ModernBeta.BETA_CONFIG.generation_config;
    
    public static NbtCompound createSettingsBase(String worldType) {
        NbtCompound settings = new NbtCompound();
        
        settings.putString("worldType", worldType);
        
        return settings;
    }
    
    public static NbtCompound createSettingsAll(String worldType) {
        NbtCompound settings = createSettingsBase(worldType);
        
        settings.putBoolean("generateOceans", CONFIG.generateOceans);
        
        settings.putBoolean("generateInfdevPyramid", CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", CONFIG.generateInfdevWall);
        
        settings.putString("levelType", CONFIG.indevLevelType);
        settings.putString("levelTheme", CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", CONFIG.indevLevelWidth);
        settings.putInt("levelLength", CONFIG.indevLevelLength);
        settings.putInt("levelHeight", CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", CONFIG.indevCaveRadius);
        
        settings.putBoolean("generateOuterIslands", CONFIG.generateOuterIslands);
        settings.putInt("centerIslandRadius", CONFIG.centerIslandRadius);
        settings.putFloat("centerIslandFalloff", CONFIG.centerIslandFalloff);
        settings.putInt("centerOceanLerpDistance", CONFIG.centerOceanLerpDistance);
        settings.putInt("centerOceanRadius", CONFIG.centerOceanRadius);
        settings.putFloat("outerIslandNoiseScale", CONFIG.outerIslandNoiseScale);
        settings.putFloat("outerIslandNoiseOffset", CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
    
    /*
    public static NbtCompound createSettingsInf(String worldType) {
        return createSettingsBase(worldType, CONFIG.generateOceans);
    }
    
    public static NbtCompound createSettingsInfNoOceans(String worldType) {
        return createSettingsBase(worldType, false);
    }
    
    public static NbtCompound createSettingsInfdev227(String worldType) {
        NbtCompound settings = createSettingsBase(worldType, CONFIG.generateOceans);
        
        settings.putBoolean("generateInfdevPyramid", CONFIG.generateInfdevPyramid);
        settings.putBoolean("generateInfdevWall", CONFIG.generateInfdevWall);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIndev(String worldType) {
        NbtCompound settings = createSettingsBase(worldType, false);
        
        settings.putString("levelType", CONFIG.indevLevelType);
        settings.putString("levelTheme", CONFIG.indevLevelTheme);
        settings.putInt("levelWidth", CONFIG.indevLevelWidth);
        settings.putInt("levelLength", CONFIG.indevLevelLength);
        settings.putInt("levelHeight", CONFIG.indevLevelHeight);
        settings.putFloat("caveRadius", CONFIG.indevCaveRadius);
        
        return settings;
    }
    
    public static NbtCompound createSettingsIslands(String worldType) {
        NbtCompound settings = createSettingsBase(worldType, CONFIG.generateOceans);
        
        settings.putBoolean("generateOuterIslands", CONFIG.generateOuterIslands);
        settings.putInt("centerOceanLerpDistance", CONFIG.centerOceanLerpDistance);
        settings.putInt("centerOceanRadius", CONFIG.centerOceanRadius);
        settings.putFloat("centerIslandFalloff", CONFIG.centerIslandFalloff);
        settings.putFloat("outerIslandNoiseScale", CONFIG.outerIslandNoiseScale);
        settings.putFloat("outerIslandNoiseOffset", CONFIG.outerIslandNoiseOffset);
        
        return settings;
    }
    */
}
