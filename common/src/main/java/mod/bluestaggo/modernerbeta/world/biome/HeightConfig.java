package mod.bluestaggo.modernerbeta.world.biome;

import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.world.biome.provider.fractal.BiomeInfo;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.event.Level;

import java.util.List;

public record HeightConfig(float depth, float scale, byte type) {
	public static final HeightConfig DEFAULT = new HeightConfig(0.1F, 0.3F);
	public static final HeightConfig OCEAN = new HeightConfig(-1.0F, 0.4F);
	public static final HeightConfig DESERT = new HeightConfig(0.1F, 0.2F);
	public static final HeightConfig EXTREME_HILLS = new HeightConfig(0.2F, 1.3F);
	public static final HeightConfig BETA_HILLS = new HeightConfig(0.2F, 1.8F);
	public static final HeightConfig TAIGA = new HeightConfig(0.1F, 0.4F);
	public static final HeightConfig SWAMPLAND = new HeightConfig(-0.2F, 0.1F);
	public static final HeightConfig RIVER = new HeightConfig(-0.5F, 0.0F);
	public static final HeightConfig MOUNTAINS = new HeightConfig(0.2F, 1.2F, 1);
	public static final HeightConfig MUSHROOM_ISLAND = new HeightConfig(0.2F, 1.0F);
	public static final HeightConfig MUSHROOM_ISLAND_SHORE = new HeightConfig(-1.0F, 0.1F, 1);
	public static final HeightConfig BEACH = new HeightConfig(0.0F, 0.1F);
	public static final HeightConfig HILLS = new HeightConfig(0.2F, 0.7F, 1);
	public static final HeightConfig SHORT_HILLS = new HeightConfig(0.2F, 0.6F, 1);
	public static final HeightConfig EXTREME_HILLS_EDGE = new HeightConfig(0.2F, 0.8F, 1);
	public static final HeightConfig JUNGLE = new HeightConfig(0.2F, 0.4F);
	public static final HeightConfig JUNGLE_HILLS = new HeightConfig(1.8F, 0.2F, 1);
	public static final HeightConfig PLATEAU = new HeightConfig(1.8F, 0.2F);
	public static final HeightConfig SWAMPLAND_HILLS = new HeightConfig(-0.1F, 0.5F, 1);
	public static final HeightConfig PLATEAU_HILL = new HeightConfig(1.8F, 0.2F, 1);
	public static final HeightConfig DEEP_OCEAN = new HeightConfig(-1.8F, 0.2F);

	public HeightConfig(float depth, float scale) {
		this(depth, scale, 0);
	}

	public HeightConfig(float depth, float scale, int type) {
		this(depth, scale, (byte) type);
	}

	public static HeightConfig getHeightConfig(BiomeInfo biomeInfo) {
        List<HeightConfig> configs = ModernBetaRegistries.HEIGHT_CONFIG.getKeySet()
            .stream()
            .filter(id -> biomeInfo.biome().isIn(TagKey.of(RegistryKeys.BIOME, Identifier.of(id))))
	        .map(ModernBetaRegistries.HEIGHT_CONFIG::get)
	        .toList();
		return configs.stream().filter(config -> biomeInfo.type() == config.type).findAny()
				.orElse(configs.stream().findAny().orElse(DEFAULT));
	}

	public static HeightConfig parse(String string, HeightConfig fallback) {
		String[] heightConfigPair = string.split(";");
		try {
			float scale = Float.parseFloat(heightConfigPair[0]);
			float depth = Float.parseFloat(heightConfigPair[1]);
			return new HeightConfig(scale, depth);
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
			ModernerBeta.log(Level.WARN, String.format("Invalid height config \"%s\"", string));
			return fallback;
		}
	}

	public static String makeString(float depth, float scale) {
		return depth + ";" + scale;
	}

	public String makeString() {
		return makeString(this.depth, this.scale);
	}

	@Override
	public String toString() {
		return String.format(
			"[depth=%.3f, scale=%.3f]",
			this.depth,
			this.scale
		);
	}
}
