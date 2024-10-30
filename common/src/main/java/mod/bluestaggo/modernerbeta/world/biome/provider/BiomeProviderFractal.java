package mod.bluestaggo.modernerbeta.world.biome.provider;

import it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeProvider;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverBlock;
import mod.bluestaggo.modernerbeta.api.world.biome.BiomeResolverInfo;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeProviderFractal extends BiomeProvider implements BiomeResolverBlock, BiomeResolverInfo {
	private final List<RegistryEntry<Biome>> allBiomes;

	private final Layer biomeGenLayer;
	private final Layer biomeBlockLayer;

	private final Long2ObjectMap<BiomeInfo[]> genCache = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());
	private final Long2ObjectMap<BiomeInfo[]> blockCache = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap<>());

	public BiomeProviderFractal(NbtCompound settings, RegistryEntryLookup<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		List<BiomeInfo> selectedBiomes = this.settings.fractalBiomes.stream().map(b -> BiomeInfo.fromId(b, biomeRegistry)).toList();
		List<ClimaticBiomeList<BiomeInfo>> climaticBiomes = qualifyClimaticBiomeLists(biomeRegistry, this.settings.fractalClimaticBiomes);
		Map<BiomeInfo, BiomeInfo> hillVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalHillVariants);
		Map<BiomeInfo, BiomeInfo> edgeVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalEdgeVariants);
		Map<BiomeInfo, BiomeInfo> mutatedVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalMutatedVariants);
		Map<BiomeInfo, BiomeInfo> veryRareVariants = qualifyBiomeMap(biomeRegistry, this.settings.fractalVeryRareVariants);
		Int2ObjectMap<Map<BiomeInfo, List<BiomeInfo>>> subVariants = qualifySubVariants(biomeRegistry, this.settings.fractalSubVariants);

		var allBiomes = new HashSet<RegistryEntry<Biome>>();
		allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.OCEAN));
		allBiomes.add(getBiomeEntry(this.settings.fractalPlains));
		allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.RIVER));
		if (this.settings.fractalAddSnow) {
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.FROZEN_OCEAN));
			allBiomes.add(getBiomeEntry(this.settings.fractalIcePlains));
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.FROZEN_RIVER));
		}
		if (this.settings.fractalAddMushroomIslands) allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.MUSHROOM_FIELDS));
		if (this.settings.fractalAddBeaches) allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.BEACH));
		if (this.settings.fractalAddDeepOceans) allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.DEEP_OCEAN));
		if (this.settings.fractalAddClimaticOceans) {
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.LUKEWARM_OCEAN));
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.WARM_OCEAN));
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.COLD_OCEAN));
			allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.FROZEN_OCEAN));
			if (this.settings.fractalAddDeepOceans) {
				allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.DEEP_LUKEWARM_OCEAN));
				allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.DEEP_COLD_OCEAN));
				allBiomes.add(biomeRegistry.getOrThrow(BiomeKeys.DEEP_FROZEN_OCEAN));
			}
		}
		if (this.settings.fractalUseClimaticBiomes) {
			for (ClimaticBiomeList<BiomeInfo> climate : climaticBiomes) {
				climate.normalBiomes().forEach(b -> allBiomes.add(b.biome()));
				climate.rareBiomes().forEach(b -> allBiomes.add(b.biome()));
			}
		} else {
			selectedBiomes.stream().map(BiomeInfo::biome).forEach(allBiomes::add);
		}
		subVariants.values().forEach(v ->
			v.values().forEach(bl ->
				bl.forEach(b -> allBiomes.add(b.biome()))));
		addBiomeMap(allBiomes, edgeVariants);
		addBiomeMap(allBiomes, hillVariants);
		addBiomeMap(allBiomes, mutatedVariants);
		addBiomeMap(allBiomes, veryRareVariants);
		this.allBiomes = allBiomes.stream().toList();

		var fractalSettings = new FractalSettings.Builder();
		fractalSettings.biomes = selectedBiomes;
		fractalSettings.climaticBiomes = climaticBiomes;
		fractalSettings.hillVariants = hillVariants;
		fractalSettings.edgeVariants = edgeVariants;
		fractalSettings.mutatedVariants = mutatedVariants;
		fractalSettings.veryRareVariants = veryRareVariants;
		fractalSettings.subVariants = subVariants;
		fractalSettings.plains = getBiomeEntry(this.settings.fractalPlains);
		fractalSettings.icePlains = getBiomeEntry(this.settings.fractalIcePlains);
		fractalSettings.biomeScale = this.settings.fractalBiomeScale;
		fractalSettings.hillScale = this.settings.fractalHillScale;
		fractalSettings.subVariantSeed = this.settings.fractalSubVariantSeed;
		fractalSettings.beachShrink = this.settings.fractalBeachShrink;
		fractalSettings.oceanShrink = this.settings.fractalOceanShrink;
		fractalSettings.terrainType = FractalSettings.TerrainType.fromString(this.settings.fractalTerrainType);
		fractalSettings.oceans = this.settings.fractalOceans;
		fractalSettings.addRivers = this.settings.fractalAddRivers;
		fractalSettings.addSnow = this.settings.fractalAddSnow;
		fractalSettings.addMushroomIslands = this.settings.fractalAddMushroomIslands;
		fractalSettings.addBeaches = this.settings.fractalAddBeaches;
		fractalSettings.addStonyShores = this.settings.fractalAddStonyShores;
		fractalSettings.addHills = this.settings.fractalAddHills;
		fractalSettings.addSwampRivers = this.settings.fractalAddSwampRivers;
		fractalSettings.addDeepOceans = this.settings.fractalAddDeepOceans;
		fractalSettings.addMutations = this.settings.fractalAddMutations;
		fractalSettings.addClimaticOceans = this.settings.fractalAddClimaticOceans;
		fractalSettings.useClimaticBiomes = this.settings.fractalUseClimaticBiomes;

		biomeGenLayer = Layer.getLayer(biomeRegistry, seed, fractalSettings.build());
		biomeBlockLayer = new LayerVoronoiZoom(10, biomeGenLayer);
		biomeBlockLayer.setWorldSeed(seed);
	}

	private static List<ClimaticBiomeList<BiomeInfo>> qualifyClimaticBiomeLists(RegistryEntryLookup<Biome> biomeRegistry, List<ClimaticBiomeList<String>> lists) {
		return lists.stream().map(c -> ClimaticBiomeList.qualify(c, biomeRegistry)).toList();
	}

	private static Map<BiomeInfo, BiomeInfo> qualifyBiomeMap(RegistryEntryLookup<Biome> biomeRegistry, Map<String, String> map) {
		return map.entrySet().stream()
			.map(kv -> Map.entry(BiomeInfo.fromId(kv.getKey(), biomeRegistry), BiomeInfo.fromId(kv.getValue(), biomeRegistry)))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static Int2ObjectMap<Map<BiomeInfo, List<BiomeInfo>>> qualifySubVariants(RegistryEntryLookup<Biome> biomeRegistry, Map<String, Map<String, List<String>>> subVariants) {
		var subVariantIterator = subVariants.entrySet().stream()
			.map(kv -> {
				try {
					return new AbstractInt2ObjectMap.BasicEntry<>(
						Integer.parseInt(kv.getKey()),
						kv.getValue().entrySet().stream()
							.map(kv2 ->
								Map.entry(
									BiomeInfo.fromId(kv2.getKey(), biomeRegistry),
									kv2.getValue().stream()
										.map(v -> BiomeInfo.fromId(v, biomeRegistry)).toList()
								)
							)
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
					);
				} catch (NumberFormatException ignored) {
					return null;
				}
			})
			.iterator();

		Int2ObjectMap<Map<BiomeInfo, List<BiomeInfo>>> qualifiedSubVariants = new Int2ObjectOpenHashMap<>();
        while (subVariantIterator.hasNext()) {
			var subVariant = subVariantIterator.next();
			qualifiedSubVariants.put(subVariant.getIntKey(), subVariant.getValue());
		}
		return qualifiedSubVariants;
	}

	private static void addBiomeMap(Set<RegistryEntry<Biome>> allBiomes, Map<BiomeInfo, BiomeInfo> biomes) {
		biomes.entrySet().stream()
			.filter(kv -> allBiomes.contains(kv.getKey().biome()))
			.map(Map.Entry::getValue)
			.map(BiomeInfo::biome)
			.forEach(allBiomes::add);
	}

	private RegistryEntry<Biome> getBiomeEntry(String id) {
		RegistryKey<Biome> key = RegistryKey.of(RegistryKeys.BIOME, new Identifier(id));
		return biomeRegistry.getOrThrow(key);
	}

	@Override
	public RegistryEntry<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return getBiomeInfo(biomeX, biomeY, biomeZ).biome();
	}

	@Override
	public BiomeInfo getBiomeInfo(int biomeX, int biomeY, int biomeZ) {
		int chunkX = biomeX >> 2;
		int chunkZ = biomeZ >> 2;
		long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
		BiomeInfo[] biomes;

		synchronized (genCache) {
			biomes = genCache.get(chunkPos);
			if (biomes == null) {
				biomes = biomeGenLayer.getBiomes(chunkX << 2, chunkZ << 2, 4, 4);
				genCache.put(chunkPos, biomes);
				cleanCache(genCache, chunkX, chunkZ, 16);
			}
		}

		int subX = biomeX & 3;
		int subZ = biomeZ & 3;
		return biomes[subX + (subZ << 2)];
	}

	@Override
	public RegistryEntry<Biome> getBiomeBlock(int x, int y, int z) {
		return getBiomeInfoBlock(x, z).biome();
	}

	public BiomeInfo getBiomeInfoBlock(int x, int z) {
		int chunkX = x >> 4;
		int chunkZ = z >> 4;
		long chunkPos = ChunkPos.toLong(chunkX, chunkZ);
		BiomeInfo[] biomes;

		synchronized (blockCache) {
			biomes = blockCache.get(chunkPos);
			if (biomes == null) {
				biomes = biomeBlockLayer.getBiomes(chunkX << 4, chunkZ << 4, 16, 16);
				blockCache.put(chunkPos, biomes);
				cleanCache(blockCache, chunkX, chunkZ, 16);
			}
		}

		int subX = x & 15;
		int subZ = z & 15;
		return biomes[subX + (subZ << 4)];
	}

	@Override
	public List<RegistryEntry<Biome>> getBiomes() {
		return allBiomes;
	}

	private void cleanCache(Long2ObjectMap<BiomeInfo[]> cache, int x, int z, int dist) {
		if (cache.size() < 256) {
			return;
		}
		cache.clear();
	}
}
