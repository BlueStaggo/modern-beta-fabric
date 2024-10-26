package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.ModernBetaBuiltInTypes;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsChunk;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.world.biome.ModernBetaBiomeSource;
import mod.bluestaggo.modernerbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Random;

public class ModernBetaWorldScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title"; 
    private static final String TEXT_TITLE_CHUNK = "createWorld.customize.modern_beta.title.chunk"; 
    private static final String TEXT_TITLE_BIOME = "createWorld.customize.modern_beta.title.biome"; 
    private static final String TEXT_TITLE_CAVE_BIOME = "createWorld.customize.modern_beta.title.cave_biome"; 
    
    private static final String TEXT_PRESET = "createWorld.customize.modern_beta.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_CUSTOM = "createWorld.customize.modern_beta.preset.custom";
    
    private static final String TEXT_CHUNK = "createWorld.customize.modern_beta.chunk";
    private static final String TEXT_BIOME = "createWorld.customize.modern_beta.biome";
    private static final String TEXT_CAVE_BIOME = "createWorld.customize.modern_beta.cave_biome";

    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_SETTINGS_JSON = "createWorld.customize.modern_beta.settings.json";
    private static final String TEXT_SETTINGS_RESET = "createWorld.customize.modern_beta.settings.reset";
    private static final String TEXT_SETTINGS_RESET_MESSAGE = "createWorld.customize.modern_beta.settings.reset.message";
    //private static final String TEXT_INVALID_SETTINGS = "createWorld.customize.modern_beta.invalid_settings";
    
    private static final String[] TEXT_HINTS = new String[] {
        "createWorld.customize.modern_beta.hint.settings"
    };
    
    private final TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone;
    private final String hintString;
    private final GeneratorOptionsHolder generatorOptionsHolder;

    private ModernBetaSettingsPreset preset;
    private ButtonWidget buttonPreset;

    public ModernBetaWorldScreen(Screen parent, GeneratorOptionsHolder generatorOptionsHolder, TriConsumer<NbtCompound, NbtCompound, NbtCompound> onDone) {
        super(Text.translatable(TEXT_TITLE), parent);
        
        ChunkGenerator chunkGenerator = generatorOptionsHolder.selectedDimensions().getChunkGenerator();
        ModernBetaChunkGenerator modernBetaChunkGenerator = (ModernBetaChunkGenerator)chunkGenerator;
        ModernBetaBiomeSource modernBetaBiomeSource = (ModernBetaBiomeSource)modernBetaChunkGenerator.getBiomeSource();
        
        this.onDone = onDone;
        this.hintString = TEXT_HINTS[new Random().nextInt(TEXT_HINTS.length)];
        
        this.preset = new ModernBetaSettingsPreset(
            modernBetaChunkGenerator.getChunkSettings(),
            modernBetaBiomeSource.getBiomeSettings(),
            modernBetaBiomeSource.getCaveBiomeSettings()
        );
        this.generatorOptionsHolder = generatorOptionsHolder;
    }
    
    public void setPreset(ModernBetaSettingsPreset preset) {
        this.preset = preset;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(
                this.preset.settingsChunk().toCompound(),
                this.preset.settingsBiome().toCompound(),
                this.preset.settingsCaveBiome().toCompound()
            );
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 154, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 4, this.height - 26, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        Text hintText = Text.translatable(this.hintString).formatted(Formatting.GRAY);
        int hintTextWidth = this.textRenderer.getWidth(hintText.asOrderedText());
        int hintTextHeight = this.textRenderer.fontHeight;
        
        this.addDrawableChild(new TextWidget(
            this.width / 2 - hintTextWidth / 2,
            this.height - 46,
            hintTextWidth,
            hintTextHeight,
            hintText,
            this.textRenderer
        ));

        MutableText presetText = Text.translatable(TEXT_PRESET).append(": ");
        presetText.append(this.isPresetCustom() ?
            Text.translatable(TEXT_PRESET_CUSTOM) :
            Text.translatable(TEXT_PRESET_NAME + "." + this.getPresetKey()).formatted(Formatting.YELLOW)
        );
            
        this.buttonPreset = ButtonWidget.builder(
            presetText,
            button -> this.client.setScreen(new ModernBetaSettingsPresetScreen(
                this,
                ModernBetaRegistries.SETTINGS_PRESET_CATEGORY.getKeySet().stream().toList(),
                this.preset,
                true
            ))
        ).dimensions(0, 0, BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

        ButtonWidget buttonChunk = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalChunkSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.settingsChunk().toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set(nbtCompound, null, null);
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).build();

        ButtonWidget buttonChunkAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.preset.settingsChunk(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set(string, "", "");
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();

        ButtonWidget buttonBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalBiomeSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.settingsBiome().toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set(null, nbtCompound, null);
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).build();
        
        ButtonWidget buttonBiomeAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_BIOME,
                this,
                this.preset.settingsBiome(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set("", string, "");
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();

        ButtonWidget buttonCaveBiome = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS),
            button -> this.client.setScreen(new ModernBetaGraphicalCaveBiomeSettingsScreen(
                TEXT_TITLE_CHUNK,
                this,
                this.generatorOptionsHolder,
                this.preset.settingsCaveBiome().toCompound(),
                nbtCompound -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set(null, null, nbtCompound);
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).build();
        
        ButtonWidget buttonCaveBiomeAdvanced = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_JSON),
            button -> this.client.setScreen(new ModernBetaSettingsScreen(
                TEXT_TITLE_CAVE_BIOME,
                this,
                this.preset.settingsCaveBiome(),
                string -> {
                    Pair<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set("", "", string);
                    this.preset = updatedPreset.getLeft();
                }
            ))
        ).size(20, 20).build();
        
        ButtonWidget buttonReset = ButtonWidget.builder(
            Text.translatable(TEXT_SETTINGS_RESET),
            button -> this.client.setScreen(new ModernBetaSettingsConfirmScreen(
                this,
                this::resetPreset,
                Text.translatable(TEXT_SETTINGS_RESET_MESSAGE),
                Text.translatable(TEXT_SETTINGS_RESET)
            ))
        ).build();
        
        GridWidget gridWidgetMain = this.createGridWidget();
        GridWidget gridWidgetSettings = this.createGridWidget();
        
        GridWidget.Adder gridAdderMain = gridWidgetMain.createAdder(1);
        GridWidget.Adder gridAdderSettings = gridWidgetSettings.createAdder(3);
        gridAdderSettings.getMainPositioner().alignVerticalCenter();
        
        gridAdderMain.add(this.buttonPreset);
        gridAdderMain.add(gridWidgetSettings);
        gridAdderMain.add(buttonReset);
        
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CHUNK, buttonChunk, buttonChunkAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_BIOME, buttonBiome, buttonBiomeAdvanced);
        this.addGridTextButtonTriplet(gridAdderSettings, TEXT_CAVE_BIOME, buttonCaveBiome, buttonCaveBiomeAdvanced);
        
        gridWidgetMain.refreshPositions();
        SimplePositioningWidget.setPos(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidgetMain.forEachChild(this::addDrawableChild);
        
        this.onPresetChange();
    }
    
    private void onPresetChange() {
        if (this.isPresetCustom()) {
            this.buttonPreset.active = false;
        } else {
            this.buttonPreset.active = true;
        }
    }
    
    private void resetPreset() {
        this.preset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Preset.BETA_1_7_3.id);
        this.onPresetChange();
    }
    
    private boolean isPresetCustom() {
        return !ModernBetaRegistries.SETTINGS_PRESET.contains(this.preset);
    }
    
    private String getPresetKey() {
        if (ModernBetaRegistries.SETTINGS_PRESET.contains(this.preset))
            return ModernBetaRegistries.SETTINGS_PRESET.getKey(this.preset);

        return null;
    }
}
