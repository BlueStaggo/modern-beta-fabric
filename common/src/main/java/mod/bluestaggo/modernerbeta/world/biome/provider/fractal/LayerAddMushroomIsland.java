package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddMushroomIsland extends Layer {
	private final BiomeInfo ocean, mushroomIsland;

	public LayerAddMushroomIsland(long seed, Layer parent) {
		this(seed, parent, DummyBiome.OCEAN, DummyBiome.MUSHROOM_ISLAND);
	}

	public LayerAddMushroomIsland(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> mushroomIsland) {
		super(seed, parent);
		this.ocean = BiomeInfo.of(ocean);
		this.mushroomIsland = BiomeInfo.of(mushroomIsland);
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEachWithNeighbors(x, z, width, length,
			(b, ix, iz, n) -> b.equals(ocean) && allNeighborsEqual(n, ocean) && nextInt(100) == 0 ? mushroomIsland : b, true);
	}
}
