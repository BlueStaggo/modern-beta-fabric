package mod.bluestaggo.modernerbeta.settings;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import org.slf4j.event.Level;

public record ModernBetaSettingsPreset(ModernBetaSettingsChunk settingsChunk, ModernBetaSettingsBiome settingsBiome, ModernBetaSettingsCaveBiome settingsCaveBiome) {
    
    public ModernBetaSettingsPreset(
        NbtCompound newSettingsChunk,
        NbtCompound newSettingsBiome,
        NbtCompound newSettingsCaveBiome
    ) {
        this(
            ModernBetaSettingsChunk.fromCompound(newSettingsChunk),
            ModernBetaSettingsBiome.fromCompound(newSettingsBiome),
            ModernBetaSettingsCaveBiome.fromCompound(newSettingsCaveBiome)
       );
    }
    
    public Pair<ModernBetaSettingsPreset, Boolean> set(String stringChunk, String stringBiome, String stringCaveBiome) {
        ModernBetaSettingsChunk settingsChunk;
        ModernBetaSettingsBiome settingsBiome;
        ModernBetaSettingsCaveBiome settingsCaveBiome;
        
        boolean successful = true;
        
        try {
            // Attempt to read settings
            settingsChunk = stringChunk != null && !stringChunk.isBlank() ?
                ModernBetaSettingsChunk.fromString(stringChunk) :
                this.settingsChunk;
            
            settingsBiome = stringBiome != null && !stringBiome.isBlank() ?
                ModernBetaSettingsBiome.fromString(stringBiome) :
                this.settingsBiome;
            
            settingsCaveBiome = stringCaveBiome != null && !stringCaveBiome.isBlank() ?
                ModernBetaSettingsCaveBiome.fromString(stringCaveBiome) :
                this.settingsCaveBiome;
            
            // Test providers
            ModernBetaRegistries.CHUNK.get(settingsChunk.chunkProvider);
            ModernBetaRegistries.BIOME.get(settingsBiome.biomeProvider);
            ModernBetaRegistries.CAVE_BIOME.get(settingsCaveBiome.biomeProvider);
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings JSON! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;
            
            settingsChunk = this.settingsChunk;
            settingsBiome = this.settingsBiome;
            settingsCaveBiome = this.settingsCaveBiome;
        }
        
        return new Pair<>(new ModernBetaSettingsPreset(settingsChunk, settingsBiome, settingsCaveBiome), successful);
    }

    public Pair<ModernBetaSettingsPreset, Boolean> set(NbtCompound nbtChunk, NbtCompound nbtBiome, NbtCompound nbtCaveBiome) {
        ModernBetaSettingsChunk settingsChunk;
        ModernBetaSettingsBiome settingsBiome;
        ModernBetaSettingsCaveBiome settingsCaveBiome;

        boolean successful = true;

        try {
            // Attempt to read settings
            settingsChunk = nbtChunk != null ?
                ModernBetaSettingsChunk.fromCompound(nbtChunk) :
                this.settingsChunk;

            settingsBiome = nbtBiome != null ?
                ModernBetaSettingsBiome.fromCompound(nbtBiome) :
                this.settingsBiome;

            settingsCaveBiome = nbtCaveBiome != null ?
                ModernBetaSettingsCaveBiome.fromCompound(nbtCaveBiome) :
                this.settingsCaveBiome;

            // Test providers
            ModernBetaRegistries.CHUNK.get(settingsChunk.chunkProvider);
            ModernBetaRegistries.BIOME.get(settingsBiome.biomeProvider);
            ModernBetaRegistries.CAVE_BIOME.get(settingsCaveBiome.biomeProvider);
        } catch (Exception e) {
            ModernerBeta.log(Level.ERROR, "Unable to read settings NBT! Reverting to previous settings..");
            ModernerBeta.log(Level.ERROR, String.format("Reason: %s", e.getMessage()));
            successful = false;

            settingsChunk = this.settingsChunk;
            settingsBiome = this.settingsBiome;
            settingsCaveBiome = this.settingsCaveBiome;
        }

        return new Pair<>(new ModernBetaSettingsPreset(settingsChunk, settingsBiome, settingsCaveBiome), successful);
    }
    
    public ModernBetaSettingsPreset copy() {
        return this.set("", "", "").getLeft();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof ModernBetaSettingsPreset))
            return false;
        
        ModernBetaSettingsPreset other = (ModernBetaSettingsPreset)obj;
        
        return 
            this.settingsChunk.toCompound().equals(other.settingsChunk.toCompound()) &&
            this.settingsBiome.toCompound().equals(other.settingsBiome.toCompound()) &&
            this.settingsCaveBiome.toCompound().equals(other.settingsCaveBiome.toCompound());
    }
}
