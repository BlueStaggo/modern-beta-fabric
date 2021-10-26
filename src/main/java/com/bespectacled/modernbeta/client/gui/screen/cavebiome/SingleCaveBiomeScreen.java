package com.bespectacled.modernbeta.client.gui.screen.cavebiome;

import java.util.function.Consumer;

import com.bespectacled.modernbeta.api.client.gui.screen.BiomeScreen;
import com.bespectacled.modernbeta.api.client.gui.screen.WorldScreen;
import com.bespectacled.modernbeta.api.client.gui.wrapper.ActionOptionWrapper;
import com.bespectacled.modernbeta.api.client.gui.wrapper.BooleanCyclingOptionWrapper;
import com.bespectacled.modernbeta.api.world.WorldSettings.WorldSetting;
import com.bespectacled.modernbeta.client.gui.Settings;
import com.bespectacled.modernbeta.util.GuiUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;

import net.minecraft.client.gui.screen.CustomizeBuffetLevelScreen;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SingleCaveBiomeScreen extends BiomeScreen {
    private static final String USE_NOISE_DISPLAY_STRING = "createWorld.customize.caveBiome.single.useNoise";
    private static final String USE_NOISE_TOOLTIP = "createWorld.customize.caveBIome.single.useNoise.tooltip";
    
    private Identifier biomeId;
    
    private SingleCaveBiomeScreen(WorldScreen parent, WorldSetting worldSetting, Consumer<Settings> consumer, Settings biomeSettings) {
        super(parent, worldSetting, consumer, biomeSettings);
        
        this.biomeId = new Identifier(NbtUtil.readStringOrThrow(NbtTags.SINGLE_BIOME, biomeSettings.getNbt()));
    }
    
    public static SingleCaveBiomeScreen create(WorldScreen parent, WorldSetting worldSetting) {
        return new SingleCaveBiomeScreen(
            parent, 
            worldSetting, 
            settings -> parent.getWorldSettings().putChanges(worldSetting, settings.getNbt()),
            new Settings(parent.getWorldSettings().getNbt(worldSetting))
        );
    }
    
    @Override
    protected void init() {
        super.init();
        
        ActionOptionWrapper singleBiomeOption = new ActionOptionWrapper(
            "createWorld.customize.biomeType.biome", // Key
            GuiUtil.createTranslatableBiomeStringFromId(NbtUtil.toStringOrThrow(this.getBiomeSetting(NbtTags.SINGLE_BIOME))),
            buttonWidget -> this.client.setScreen(new CustomizeBuffetLevelScreen(
                this,
                this.registryManager,
                biome -> {
                    this.biomeSettings.putChange(NbtTags.SINGLE_BIOME, NbtString.of(this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome).toString()));
                    this.biomeId = this.registryManager.<Biome>get(Registry.BIOME_KEY).getId(biome);
                }, 
                this.registryManager.<Biome>get(Registry.BIOME_KEY).get(this.biomeId)  
            ))
        );
        
        BooleanCyclingOptionWrapper useNoiseOption = new BooleanCyclingOptionWrapper(
            USE_NOISE_DISPLAY_STRING,
            () -> NbtUtil.toBooleanOrThrow(this.getBiomeSetting(NbtTags.USE_NOISE)),
            value -> this.putBiomeSetting(NbtTags.USE_NOISE, NbtByte.of(value)),
            this.client.textRenderer.wrapLines(new TranslatableText(USE_NOISE_TOOLTIP), 200)
        );
        
        this.addOption(singleBiomeOption);
        this.addOption(useNoiseOption);
    }
}
