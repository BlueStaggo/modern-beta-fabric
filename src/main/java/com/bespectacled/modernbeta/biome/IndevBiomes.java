package com.bespectacled.modernbeta.biome;

import java.util.ArrayList;
import java.util.List;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.indev.IndevHell;
import com.bespectacled.modernbeta.biome.indev.IndevHellEdge;
import com.bespectacled.modernbeta.biome.indev.IndevNormal;
import com.bespectacled.modernbeta.biome.indev.IndevNormalEdge;
import com.bespectacled.modernbeta.biome.indev.IndevParadise;
import com.bespectacled.modernbeta.biome.indev.IndevParadiseEdge;
import com.bespectacled.modernbeta.biome.indev.IndevSnowy;
import com.bespectacled.modernbeta.biome.indev.IndevSnowyEdge;
import com.bespectacled.modernbeta.biome.indev.IndevWoods;
import com.bespectacled.modernbeta.biome.indev.IndevWoodsEdge;
import com.google.common.collect.ImmutableList;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeCreator;

public class IndevBiomes {
    public static final Identifier INDEV_EDGE_ID = ModernBeta.createId("indev_edge");
    public static final Identifier INDEV_HELL_EDGE_ID = ModernBeta.createId("indev_hell_edge");
    public static final Identifier INDEV_PARADISE_EDGE_ID = ModernBeta.createId("indev_paradise_edge");
    public static final Identifier INDEV_WOODS_EDGE_ID = ModernBeta.createId("indev_woods_edge");
    public static final Identifier INDEV_SNOWY_EDGE_ID = ModernBeta.createId("indev_snowy_edge");
    
    public static final Identifier INDEV_NORMAL_ID = ModernBeta.createId("indev_normal");
    public static final Identifier INDEV_HELL_ID = ModernBeta.createId("indev_hell");
    public static final Identifier INDEV_PARADISE_ID = ModernBeta.createId("indev_paradise");
    public static final Identifier INDEV_WOODS_ID = ModernBeta.createId("indev_woods");
    public static final Identifier INDEV_SNOWY_ID = ModernBeta.createId("indev_snowy");
    
    public static final ImmutableList<Identifier> BIOMES = ImmutableList.of(
        INDEV_EDGE_ID,
        INDEV_HELL_EDGE_ID,
        INDEV_PARADISE_EDGE_ID,
        INDEV_WOODS_EDGE_ID,
        INDEV_SNOWY_EDGE_ID,
        
        INDEV_NORMAL_ID,
        INDEV_HELL_ID,
        INDEV_PARADISE_ID,
        INDEV_WOODS_ID,
        INDEV_SNOWY_ID
    );

    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, INDEV_EDGE_ID, IndevNormalEdge.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_HELL_EDGE_ID, IndevHellEdge.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_PARADISE_EDGE_ID, IndevParadiseEdge.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_WOODS_EDGE_ID, IndevWoodsEdge.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_SNOWY_EDGE_ID, IndevSnowyEdge.BIOME);
        
        Registry.register(BuiltinRegistries.BIOME, INDEV_NORMAL_ID, IndevNormal.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_HELL_ID, IndevHell.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_PARADISE_ID, IndevParadise.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_WOODS_ID, IndevWoods.BIOME);
        Registry.register(BuiltinRegistries.BIOME, INDEV_SNOWY_ID, IndevSnowy.BIOME);
    }
}
