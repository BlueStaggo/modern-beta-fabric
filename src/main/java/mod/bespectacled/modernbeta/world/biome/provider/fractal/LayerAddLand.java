package mod.bespectacled.modernbeta.world.biome.provider.fractal;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;

public class LayerAddLand extends Layer {
	private final BiomeInfo ocean, land, frozenOcean, snowyLand;
	private boolean r17;

	public LayerAddLand(long seed, Layer parent) {
		this(seed, parent, DummyBiome.OCEAN, DummyBiome.PLAINS, DummyBiome.FROZEN_OCEAN, DummyBiome.ICE_PLAINS);
	}

	public LayerAddLand(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> land,
						RegistryEntry<Biome> frozenOcean, RegistryEntry<Biome> snowyLand) {
		super(seed);
		this.parent = parent;
		this.ocean = BiomeInfo.of(ocean);
		this.land = BiomeInfo.of(land);
		this.frozenOcean = BiomeInfo.of(frozenOcean);
		this.snowyLand = BiomeInfo.of(snowyLand);
	}

	public static LayerAddLand r17(long seed, Layer parent) {
		LayerAddLand layer = new LayerAddLand(seed, parent);
		layer.r17 = true;
		return layer;
	}

	public static LayerAddLand r17(long seed, Layer parent, RegistryEntry<Biome> ocean, RegistryEntry<Biome> land,
								   RegistryEntry<Biome> frozenOcean, RegistryEntry<Biome> snowyLand) {
		LayerAddLand layer = new LayerAddLand(seed, parent, ocean, land, frozenOcean, snowyLand);
		layer.r17 = true;
		return layer;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		BiomeInfo climate4 = BiomeInfo.of(DummyBiome.CLIMATE, 4);
		return this.forEachWithNeighbors(x, z, width, length, (input, ix, iz, neighbors) -> {
			if (this.r17) {
				if (input.equals(ocean) && !allNeighborsEqual(neighbors, ocean)) {
					int landSampleChance = 1;
					BiomeInfo sampledLand = land;

					for (BiomeInfo neighbor : neighbors) {
						if (!neighbor.equals(ocean) && nextInt(landSampleChance++) == 0) {
							sampledLand = neighbor;
						}
					}

					if (nextInt(3) == 0 || sampledLand.equals(climate4)) {
						return sampledLand;
					} else if (sampledLand.equals(snowyLand)) {
						return frozenOcean;
					} else {
						return ocean;
					}
				} else if (!input.equals(ocean) && neighborsContain(neighbors, ocean)) {
					if (input.equals(climate4)) {
						return input;
					} else if (nextInt(5) == 0) {
						return input.equals(snowyLand) ? frozenOcean : ocean;
					}
				}
				return input;
			}

			if (!input.equals(ocean) || allNeighborsEqual(neighbors, ocean)) {
				if (input.equals(climate4)) {
					return input;
				} else if (!input.equals(ocean) && neighborsContain(neighbors, ocean) && nextInt(5) == 0) {
					return input.equals(snowyLand) ? frozenOcean : ocean;
				} else {
					return input;
				}
			} else {
				int landSampleChance = 1;
				BiomeInfo sampledLand = land;

				for (BiomeInfo neighbor : neighbors) {
					if (!neighbor.equals(ocean) && nextInt(landSampleChance++) == 0) {
						sampledLand = neighbor;
					}
				}

				if (nextInt(3) == 0 || sampledLand.equals(climate4)) {
					return sampledLand;
				} else if (sampledLand.equals(snowyLand)) {
					return frozenOcean;
				} else {
					return ocean;
				}
			}
		}, true);
	}
}
