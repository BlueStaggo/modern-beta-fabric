package mod.bluestaggo.modernerbeta.fabric.data;

import mod.bluestaggo.modernerbeta.world.structure.ModernBetaStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.world.gen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class ModernBetaTagProviderStructure extends FabricTagProvider<Structure> {
    public ModernBetaTagProviderStructure(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STRUCTURE, registriesFuture);
    }

    @Override
    protected void configure(WrapperLookup lookup) {
        getOrCreateTagBuilder(StructureTags.EYE_OF_ENDER_LOCATED)
            .add(ModernBetaStructures.INDEV_STRONGHOLD);
    }
}
