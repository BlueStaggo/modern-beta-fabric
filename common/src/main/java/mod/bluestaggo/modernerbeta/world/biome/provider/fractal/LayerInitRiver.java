package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

public class LayerInitRiver extends Layer {
	private final boolean wideRandom;

	public LayerInitRiver(long seed, Layer parent, boolean wideRandom) {
		super(seed, parent);
		this.wideRandom = wideRandom;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> b.biome().equals(DummyBiome.OCEAN) ? b :
				(wideRandom ? BiomeInfo.of(DummyBiome.RIVER).withType(nextInt(299999) + 2) :
						BiomeInfo.of(DummyBiome.RIVER).asSpecial(nextInt(2) == 1)));
	}
}
