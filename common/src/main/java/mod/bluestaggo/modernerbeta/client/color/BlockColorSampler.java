package mod.bluestaggo.modernerbeta.client.color;

import mod.bluestaggo.modernerbeta.api.world.biome.climate.ClimateSampler;
import mod.bluestaggo.modernerbeta.api.world.biome.climate.Clime;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

import java.util.Optional;

public final class BlockColorSampler {
    public static final BlockColorSampler INSTANCE = new BlockColorSampler();

    public final BlockColormap colormapGrass;
    public final BlockColormap colormapFoliage;
    public final BlockColormap colormapWater;
    public final BlockColormap colormapUnderwater;
    
    private Optional<ClimateSampler> climateSampler;
    
    private BlockColorSampler() {
        this.colormapGrass = new BlockColormap();
        this.colormapFoliage = new BlockColormap();
        this.colormapWater = new BlockColormap();
        this.colormapUnderwater = new BlockColormap();
        
        this.climateSampler = Optional.empty();
    }
    
    public Optional<ClimateSampler> getClimateSampler() {
        return this.climateSampler;
    }
    
    public void setClimateSampler(ClimateSampler climateSampler) {
        this.climateSampler = Optional.ofNullable(climateSampler);
    }
    
    public int getGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColors.getDefaultColor();
        }

        if (this.useBiomeColor()) {
            int x = pos.getX();
            int z = pos.getZ();

            Clime clime = this.climateSampler.get().sample(x, z);
            
            return GrassColors.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getTallGrassColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return GrassColors.getDefaultColor();
        }
        
        if (this.useBiomeColor()) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            long shift = x * 0x2fc20f + z * 0x5d8875 + y;
            shift = shift * shift * 0x285b825L + shift * 11L;
            x = (int) ((long) x + (shift >> 14 & 31L));
            y = (int) ((long) y + (shift >> 19 & 31L));
            z = (int) ((long) z + (shift >> 24 & 31L));
            
            Clime clime = this.climateSampler.get().sample(x, z);
            
            return GrassColors.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public int getFoliageColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
            return 0xFF48B518;
        }
        
        if (this.useBiomeColor()) {
            int x = pos.getX();
            int z = pos.getZ();
            
            Clime clime = this.climateSampler.get().sample(x, z);

            return FoliageColors.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getFoliageColor(view, pos);
    }
    
    public int getWaterColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useWaterColor()) {
            int x = pos.getX();
            int z = pos.getZ();
            
            Clime clime = this.climateSampler.get().sample(x, z);
            
            return this.colormapWater.getColor(clime.temp(), clime.rain());
        }
        
        return BiomeColors.getWaterColor(view, pos);
    }
    
    public int getSugarCaneColor(BlockState state, BlockRenderView view, BlockPos pos, int tintNdx) {
        if (view == null || pos == null) {
            return 0xFFFFFFFF;
        }

        if (this.useBiomeColor()) {
            return 0xFFFFFFFF;
        }
        
        return BiomeColors.getGrassColor(view, pos);
    }
    
    public boolean useBiomeColor() {
        return this.climateSampler.isPresent() && this.climateSampler.get().useBiomeColor();
    }
    
    public boolean useWaterColor() {
        return this.climateSampler.isPresent() && this.climateSampler.get().useWaterColor();
    }
}
