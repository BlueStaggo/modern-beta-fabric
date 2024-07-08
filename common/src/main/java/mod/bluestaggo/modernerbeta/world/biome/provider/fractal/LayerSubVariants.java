package mod.bluestaggo.modernerbeta.world.biome.provider.fractal;

import java.util.List;
import java.util.Map;

public class LayerSubVariants extends Layer {
	private final Map<BiomeInfo, List<BiomeInfo>> subVariants;

	public LayerSubVariants(long seed, Layer parent, Map<BiomeInfo, List<BiomeInfo>> subVariants) {
		super(seed, parent);
		this.subVariants = subVariants;
	}

	@Override
	protected BiomeInfo[] getNewBiomes(int x, int z, int width, int length) {
		return forEach(x, z, width, length, b -> subVariants.containsKey(b) ? subVariants.get(b).get(nextInt(subVariants.get(b).size())) : b);
	}
}
