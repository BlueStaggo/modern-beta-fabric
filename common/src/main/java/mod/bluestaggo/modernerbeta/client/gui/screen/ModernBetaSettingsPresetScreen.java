package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.bluestaggo.modernerbeta.ModernerBeta;
import mod.bluestaggo.modernerbeta.api.registry.ModernBetaRegistries;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPreset;
import mod.bluestaggo.modernerbeta.settings.ModernBetaSettingsPresetCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
    private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
    private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
    private static final String TEXT_PRESET_DESC = "createWorld.customize.modern_beta.preset.desc";
    private static final String TEXT_PRESET_CATEGORY_NAME = "createWorld.customize.modern_beta.preset_category.name";
    private static final String TEXT_PRESET_CATEGORY_DESC = "createWorld.customize.modern_beta.preset_category.desc";
    
    //private static final Identifier TEXTURE_PRESET_DEFAULT = createTextureId("default");
    private static final Identifier TEXTURE_PRESET_CUSTOM = createTextureId("custom");
    
    private final ModernBetaWorldScreen worldScreen;
    private final List<String> presets;
    private final boolean displayCategories;

    private ModernBetaSettingsPreset preset;
    private PresetsListWidget listWidget;
    private ButtonWidget selectPresetButton;

    public ModernBetaSettingsPresetScreen(
        ModernBetaScreen parent,
        List<String> presets,
        ModernBetaSettingsPreset preset,
        boolean displayCategories
    ) {
        super(Text.translatable(TEXT_TITLE), parent);
        
        this.presets = presets;
        this.preset = preset;
        this.displayCategories = displayCategories;

        Screen worldScreen = parent;
        while (!(worldScreen instanceof ModernBetaWorldScreen)) {
            if (!(worldScreen instanceof ModernBetaScreen modernBetaScreen)) {
                worldScreen = null;
                break;
            }
            worldScreen = modernBetaScreen.parent;
        }
        this.worldScreen = (ModernBetaWorldScreen)worldScreen;
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.listWidget = new PresetsListWidget(this.presets);
        this.addSelectableChild(this.listWidget);

        this.selectPresetButton = this.addDrawableChild(ButtonWidget.builder(
            Text.translatable("createWorld.customize.presets.select"),
            onPress -> {
                this.worldScreen.setPreset(this.preset);
                this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());
        this.selectPresetButton.active = !this.displayCategories;

        this.addDrawableChild(ButtonWidget.builder(
            this.displayCategories ? ScreenTexts.CANCEL : ScreenTexts.BACK,
            button -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());

        this.updateSelectButton(this.listWidget.getSelectedOrNull() instanceof PresetsListWidget.PresetEntry);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.listWidget.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    protected void renderBackgroundWithOverlay(DrawContext context) {}

    private void updateSelectButton(boolean hasSelected) {
        this.selectPresetButton.active = hasSelected;
    }
    
    private static Identifier createTextureId(String id) {
        return ModernerBeta.createId("textures/gui/preset_" + id + ".png");
    }

    private static Identifier createPresetTextureId(String id) {
        Identifier idObj = createTextureId(id);
        return MinecraftClient.getInstance().getResourceManager().getResource(idObj).isPresent()
            ? idObj : TEXTURE_PRESET_CUSTOM;
    }

    private class PresetsListWidget extends AlwaysSelectedEntryListWidget<PresetsListWidget.AbstractPresetEntry> {
        private static final int ITEM_HEIGHT = 60;
        private static final int ICON_SIZE = 56;

        public PresetsListWidget(List<String> presets) {
            super(
                ModernBetaSettingsPresetScreen.this.client,
                ModernBetaSettingsPresetScreen.this.width,
                ModernBetaSettingsPresetScreen.this.height,
                32,
                ModernBetaSettingsPresetScreen.this.height - 32,
                ITEM_HEIGHT
            );

            if (ModernBetaSettingsPresetScreen.this.displayCategories) {
                presets.forEach(key -> {
                    this.addEntry(new PresetCategoryEntry(
                        key,
                        ModernBetaRegistries.SETTINGS_PRESET_CATEGORY.get(key)
                    ));
                });
            } else {
                presets.forEach(key -> {
                    this.addEntry(new PresetEntry(
                        key,
                        ModernBetaRegistries.SETTINGS_PRESET.get(key)
                    ));
                });
            }
        }
        
        @Override
        public void setSelected(AbstractPresetEntry entry) {
            super.setSelected(entry);

            ModernBetaSettingsPresetScreen.this.updateSelectButton(entry instanceof PresetEntry);
        }
        
        @Override
        protected int getScrollbarPositionX() {
            return super.getScrollbarPositionX() + 30;
        }
        
        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 85;
        }
        
        private abstract class AbstractPresetEntry extends AlwaysSelectedEntryListWidget.Entry<AbstractPresetEntry> {
            private static final Identifier TEXTURE_WORLD_SELECT = new Identifier("textures/gui/world_selection.png");
            private static final int TEXTURE_WORLD_SELECT_ATLAS_SIZE = 256;
            private static final int TEXTURE_WORLD_SELECT_SIZE= 32;
            
            private static final int TEXT_SPACING = 11;
            private static final int TEXT_LENGTH = 240;
            
            private final Identifier presetTexture;
            private final MutableText presetName;
            private final MutableText presetDesc;

            private long time;
            
            public AbstractPresetEntry(String presetName) {
                this.presetTexture = this.getPresetTexture(presetName);
                this.presetName = this.getPresetName(presetName);
                this.presetDesc = this.getPresetDesc(presetName);
            }

            protected abstract void setPreset();

            protected abstract void selectPreset();

            protected Identifier getPresetTexture(String presetName) {
                return createPresetTextureId(presetName);
            }

            protected MutableText getPresetName(String presetName) {
                return Text.translatable(TEXT_PRESET_NAME + "." + presetName);
            }

            protected MutableText getPresetDesc(String presetName) {
                return Text.translatable(TEXT_PRESET_DESC + "." + presetName);
            }

            protected Formatting getTextFormatting() {
                return Formatting.YELLOW;
            }

            @Override
            public Text getNarration() {
                return Text.empty();
            }

            @Override
            public void render(DrawContext context,int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                TextRenderer textRenderer = ModernBetaSettingsPresetScreen.this.textRenderer;
                
                MutableText presetNameText = this.presetName.formatted(this.getTextFormatting());
                
                List<OrderedText> presetDescTexts = this.splitText(textRenderer, this.presetDesc);

                int textStartX = x + ICON_SIZE + 3;
                int textStartY = 1;
                
                context.drawText(textRenderer, presetNameText, textStartX, y + textStartY, Colors.WHITE, false);
                
                int descSpacing = TEXT_SPACING + textStartY + 1;
                for (OrderedText line : presetDescTexts) {
                    context.drawText(textRenderer, line, textStartX, y + descSpacing, Colors.GRAY, false);
                    descSpacing += TEXT_SPACING;
                }

                this.draw(context, x, y, this.presetTexture);

                if (ModernBetaSettingsPresetScreen.this.client.options.getTouchscreen().getValue() || hovered) {
                    boolean isMouseHovering = (mouseX - x) < ICON_SIZE;
                    float v = isMouseHovering ? TEXTURE_WORLD_SELECT_SIZE : 0;
                    
                    context.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
                    context.drawTexture(
                        TEXTURE_WORLD_SELECT,
                        x,
                        y,
                        ICON_SIZE,
                        ICON_SIZE,
                        0.0f,
                        v,
                        TEXTURE_WORLD_SELECT_SIZE,
                        TEXTURE_WORLD_SELECT_SIZE,
                        TEXTURE_WORLD_SELECT_ATLAS_SIZE,
                        TEXTURE_WORLD_SELECT_ATLAS_SIZE
                    );
                }
            }
            
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button != 0) {
                    return false;
                }
                
                this.setPreset();
                
                if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE) {
                    this.selectPreset();
                }
                
                if (Util.getMeasuringTimeMs() - this.time < 250L) {
                    this.selectPreset();
                }
                
                this.time = Util.getMeasuringTimeMs();
                
                return true;
            }

            private void draw(DrawContext context, int x, int y, Identifier textureId) {
                RenderSystem.enableBlend();
                context.drawTexture(textureId, x, y, 0.0f, 0.0f, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
                RenderSystem.disableBlend();
            }
            
            private List<OrderedText> splitText(TextRenderer textRenderer, Text text) {
                return textRenderer.wrapLines(text, TEXT_LENGTH);
            }
        }

        private class PresetEntry extends AbstractPresetEntry {
            private final ModernBetaSettingsPreset preset;

            public PresetEntry(String presetName, ModernBetaSettingsPreset preset) {
                super(presetName);
                this.preset = preset;
            }

            @Override
            protected void setPreset() {
                PresetsListWidget.this.setSelected(this);
                ModernBetaSettingsPresetScreen.this.preset = this.preset.copy();
            }

            @Override
            protected void selectPreset() {
                ModernBetaSettingsPresetScreen presetScreen = ModernBetaSettingsPresetScreen.this;
                MinecraftClient minecraftClient = presetScreen.client;

                minecraftClient.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

                presetScreen.worldScreen.setPreset(this.preset);

                while (minecraftClient.currentScreen instanceof ModernBetaSettingsPresetScreen subPresetScreen) {
                    minecraftClient.setScreen(subPresetScreen.parent);
                }
            }
        }

        private class PresetCategoryEntry extends AbstractPresetEntry {
            private final ModernBetaSettingsPresetCategory presetCategory;

            public PresetCategoryEntry(String presetName, ModernBetaSettingsPresetCategory presetCategory) {
                super(presetName);
                this.presetCategory = presetCategory;
            }

            @Override
            protected Identifier getPresetTexture(String presetName) {
                presetName = ModernBetaRegistries.SETTINGS_PRESET_CATEGORY.get(presetName).defaultIcon();
                return super.getPresetTexture(presetName);
            }

            @Override
            protected MutableText getPresetName(String presetName) {
                return Text.translatable(TEXT_PRESET_CATEGORY_NAME + "." + presetName);
            }

            @Override
            protected MutableText getPresetDesc(String presetName) {
                return Text.translatable(TEXT_PRESET_CATEGORY_DESC + "." + presetName);
            }

            @Override
            protected Formatting getTextFormatting() {
                return Formatting.AQUA;
            }

            @Override
            protected void setPreset() {
                PresetsListWidget.this.setSelected(this);
            }

            @Override
            protected void selectPreset() {
                ModernBetaSettingsPresetScreen presetScreen = ModernBetaSettingsPresetScreen.this;

                presetScreen.client.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f)
                );

                presetScreen.client.setScreen(new ModernBetaSettingsPresetScreen(
                    presetScreen,
                    presetCategory.presets(),
                    presetScreen.preset,
                    false
                ));
            }
        }
    }
}
