The highly anticipated graphical customization menu has made a return! Every single option can be configured from graphical menus with JSON editing still available as an option.

## Additions
- Added graphical configuration menus for chunk, biome and cave biome providers.
  - Settings can still be edited as JSON by clicking any of the pencil icons to the right of the "Settings" buttons.
- Presets are now sorted into categories, allowing for easier browsing.
- Added large biomes presets for release versions.

## Changes
- BREAKING CHANGE: Reworked fractal sub-variants
  - `fractalSubVariants` used to be `Map<BiomeInfo, List<BiomeInfo>>` and controlled by `fractalSubVariantScale`,
    but now it is of the type `Map<Integer, Map<BiomeInfo, List<BiomeInfo>>>` with integers surrounded in quotes.
    Here's an example of the change:
    ```json5
    [
      // Before
      {
        "fractalSubVariants": {
          "minecraft:jungle": [
            "minecraft:plains",
            "minecraft:sparse_jungle",
            "minecraft:jungle"
          ]
        },
        "fractalSubVariantScale": 1,
        "fractalSubVariantSeed": 3000
      },
      // After
      {
        "fractalSubVariants": {
          "1": { // 1 formerly "fractalSubVariantScale"
            "minecraft:jungle": [
              "minecraft:plains",
              "minecraft:sparse_jungle",
              "minecraft:jungle"
            ]
          },
        },
        "fractalSubVariantSeed": 2999 // Subtract scale from seed accordingly
      }
    ]
    ```
  - This allows for a wider variety of customization for fractal biome generation.
  - Old presets will be automatically updated to use the new sub-variant system.
- Changed up biome distribution in the Release Hybrid preset to be more like 1.6.4 with additional biome mutations.
- The default block for Badlands soil has been changed from White Terracotta to Orange Terracotta.

## Fixes
- Added Ice Spikes to 1.12.2+ presets.
- Infdev 20100325 caves now overwrite ores with air.
- Fixed some deep oceans having the incorrect height values.
- Mutated variants now save properly.

## Removals
- Removed Beta Hybrid preset.
