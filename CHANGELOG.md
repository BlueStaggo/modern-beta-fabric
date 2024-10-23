# 1.5.0
## Additions
- Presets are now sorted into categories, allowing for easier browsing.
- Added large biomes presets for release versions.

## Changes
- Changed up biome distribution in the Release Hybrid preset to be more like 1.6.4 with additional biome mutations.

## Fixes
- Added Ice Spikes to 1.12.2+ presets.
- Infdev 20100325 caves now overwrite ores with air.
- Fixed some deep oceans having the incorrect height values.
- Mutated variants now save properly.

## Removals
- Removed Beta Hybrid preset.

# 1.4.0
## Changes
- Ported to Minecraft Forge using Architectury
  - Due to this the mod now depends on [Architectury API](https://modrinth.com/mod/architectury-api).
- Dropped "For Fabric" from logos and all assets.
  - The icon has been updated on Fabric to add the logo for the mod
  - The banner and icon now feature a new screenshot
- Dropped Fabric suffix from mod name.
- Dropped 6.5+er version prefix. 
- Mod ID has been changed to `moderner_beta` to distinguish from the original Modern Beta mod.
- Mentions of Modern Beta ingame have been replaced with Moderner Beta.

# 1.3.1
## Fixes
- Bedrock now generates correctly in Infdev 415+ world generators with surface rules enabled

# 1.3
Woo! New world generators! Classic 0.0.14a_08 and Infdev 20100325 are now available as well as a datapack to reduce world height.

## Additions
- Added Classic 0.0.14a_08 and Infdev 20100325 world generators.
  - Note: Caves in Infdev 20100325 are tied to the biome so they will not generate if a different biome provider is used and they will still generate if a different chunk provider is used.
- Added a built-in datapack that raises the minimum Y value from -64 to 0 and restores pre-1.18 ore generation.
  - This datapack can be accessed through the datapacks menu on world creation.
- Added plenty of options for Indev and Classic worlds:
  - indevNoiseScale: Scale for the main terrain noise
  - indevSelectorScale: Scale for selector noise
  - indevMinHeightDamp and indevMaxHeightDamp: Reduced steepness of the terrain
  - indevMinHeightBoost and indevMaxHeightBoost: Base height of the terrain
  - indevHeightUnderDamp: Reduced steepness of terrain below sea level
  - indevCaveRarity: Rarity and spread of caves
  - indevSandBeachThreshold and indevGravelBeachThreshold: How much beach there is
  - indevSandBeachUnderAir and indevGravelBeachUnderAir: Generate beaches that are exposed to air above them
  - indevSandBeachUnderFluid and indevGravelBeachUnderFluid: Generate beaches that are exposed to water or lava above them
  - indevWaterRarity: Rarity of water pools
  - indevLavaRarity: Rarity of lava pools
  - indevSpawnHouse: Generate a house at the spawn location

## Changes
- Hills variants of savannas now uses the savanna plateau biome.
  - That means that plateau generation outside of 1.12 and 1.17 is broken, but honestly I'm too lazy to fix that until the next release.
- Late Beta Taigas and Early Release Taigas no longer generate with ferns.
- Tree noise now uses 64 bit seeds instead of 32 bit seeds.
  - Generation of worlds that use tree noise (anything between Infdev 325 and Beta 1.7.3) has therefore been slightly altered with changes being most apparent in Alpha and Infdev.

# 1.2.3
Yet another slight oversight (I release updates too often, don't I?)

## Fixes
- Fractal climatic biomes now generate correctly with oceans disabled
- Some biomes now have proper names

# 1.2.2
Oopsies

## Fixes
- Windswept Gravelly Hills now have the correct height values

# 1.2.1
Just two minor details I just noticed

## Fixes
- Gravel now generates below sea level in 1.7+.
- Erosion noise in Beta 1.8 - 1.6.4 no longer suffers from floating point imprecision.

# 1.2
Feature update! New presets!

## Additions
- Added 1.17.1 preset with ocean biomes and bamboo jungles.
- Added various presets from Old Customized as well as beta variants.
- Added chunk generation option seaLevelOffset to adjust the sea level.

# 1.1.2
Just one slight oversight

## Fixes
- Some biomes now have the correct height values in the 1.6.4 preset

# 1.1.1
Fixing just a few more bugs

## Changes
- The Beta Vanilla preset now uses fixed caves

## Fixes
- Gravel beaches now generate correctly with surface rules
- The useFixedCaves chunk generator option now correctly saves

# 1.1
This update fixes some issues with the first Moderner Beta release.

## Additions
- Added Beta 1.1_02 and Beta 1.9 Prerelease 3 presets.
  - Beta 1.1_02 generates only oak trees.
  - Beta 1.9 Prerelease 3 does not generate short grass or flowers in ice plains.

## Fixes
- Snow biomes are now shape correctly in Beta 1.9 - Release 1.6.4.
- Features of biomes only available as hills, edges or mutations now generate correctly.
  - Therefore, every missing mutated biome from 1.12.2 has been added back into world generation.

# 1.0
This is the first release of Moderner Beta and it introduces terrain generation from early release versions of Minecraft.

## Additions
- Added Beta 1.8, 1.0.0, 1.1, 1.2.5, 1.6.4, 1.12.2, Beta Hybrid and Release Hybrid presets.
  - There are some inaccuracies, like 1.0.0-1.6.4 having differently shaped snowy biomes and 1.12.2 missing some mutated variants of biomes.
- Added Early Release (Beta 1.8 - 1.6.4) and Major Release (1.7 - 1.17) chunk generators.
- Added Fractal biome provider.
  - There are a total of 24 settings for this biome provider including adjusting the biome scale, replacing certain biomes, adding/removing certain features and adding in variants of biomes.
- Added chunk generation option releaseHeightOverrides to adjust the height of different biomes.
- Added chunk generation option useSurfaceRules to generate surfaces by vanilla means.
  - This fixes biomes such as badlands and old growth taigas generating with incorrect blocks.
  - Additional surface features such as beaches and erosion are preserved.
- Added chunk generation options useFixedCaves that fixes harsh cave borders and forceBetaCaves to force every biome to use beta cave carvers.
- Custom presets now have icons.
