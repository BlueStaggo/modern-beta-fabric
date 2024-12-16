package mod.bluestaggo.modernerbeta.client.gui.screen;

import com.ibm.icu.text.Collator;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class ModernBetaSelectBiomeScreen extends Screen {
    private final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final Screen parent;
    private final Consumer<RegistryEntry<Biome>> onDone;
    final Registry<Biome> biomeRegistry;
    private BiomeListWidget biomeSelectionList;
    RegistryEntry<Biome> biome;
    private ButtonWidget confirmButton;

    public ModernBetaSelectBiomeScreen(Screen parent, GeneratorOptionsHolder generatorOptionsHolder, Consumer<RegistryEntry<Biome>> onDone) {
        super(Text.translatable("createWorld.customize.modern_beta.title.biome_picker"));
        this.parent = parent;
        this.onDone = onDone;
        this.biomeRegistry = generatorOptionsHolder.getCombinedRegistryManager().getOrThrow(RegistryKeys.BIOME);
        RegistryEntry<Biome> registryEntry = this.biomeRegistry
                .getOptional(BiomeKeys.PLAINS)
                .or(() -> this.biomeRegistry.streamEntries().findAny())
                .orElseThrow();
        this.biome = generatorOptionsHolder.selectedDimensions()
                .getChunkGenerator()
                .getBiomeSource()
                .getBiomes()
                .stream()
                .findFirst()
                .orElse(registryEntry);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    protected void init() {
        DirectionalLayoutWidget header = this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(8));
        header.getMainPositioner().alignHorizontalCenter();
        header.add(new TextWidget(this.getTitle(), this.textRenderer));
        this.biomeSelectionList = this.layout.addBody(new ModernBetaSelectBiomeScreen.BiomeListWidget());
        DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        this.confirmButton = footer.add(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(this.biome);
            this.close();
        }).build());
        footer.add(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.close()).build());
        this.biomeSelectionList.setSelected(this.biomeSelectionList
                .children()
                .stream()
                .filter(entry -> Objects.equals(entry.biome, this.biome))
                .findFirst()
                .orElse(null));
        this.layout.forEachChild(this::addDrawableChild);
        this.refreshWidgetPositions();
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        this.biomeSelectionList.position(this.width, this.layout);
    }

    void refreshConfirmButton() {
        this.confirmButton.active = this.biomeSelectionList.getSelectedOrNull() != null;
    }

    class BiomeListWidget extends AlwaysSelectedEntryListWidget<BiomeListWidget.BiomeItem> {
        BiomeListWidget() {
            super(ModernBetaSelectBiomeScreen.this.client, ModernBetaSelectBiomeScreen.this.width, ModernBetaSelectBiomeScreen.this.height - 77, 40, 16);
            Collator collator = Collator.getInstance(Locale.getDefault());
            this.addEntry(new BiomeItem());
            ModernBetaSelectBiomeScreen.this.biomeRegistry
                    .streamEntries()
                    .map(BiomeItem::new)
                    .sorted(Comparator.comparing(biome -> biome.text.getString(), collator))
                    .forEach(this::addEntry);
        }

        public void setSelected(@Nullable BiomeListWidget.BiomeItem buffetBiomeItem) {
            super.setSelected(buffetBiomeItem);
            if (buffetBiomeItem != null) {
                ModernBetaSelectBiomeScreen.this.biome = buffetBiomeItem.biome;
            }

            ModernBetaSelectBiomeScreen.this.refreshConfirmButton();
        }

        class BiomeItem extends AlwaysSelectedEntryListWidget.Entry<BiomeListWidget.BiomeItem> {
            final RegistryEntry.Reference<Biome> biome;
            final Text text;

            public BiomeItem() {
                this.biome = null;
                this.text = Text.translatable("gui.none").formatted(Formatting.ITALIC);
            }

            public BiomeItem(final RegistryEntry.Reference<Biome> biome) {
                this.biome = biome;
                Identifier id = biome.registryKey().getValue();
                String name = id.toTranslationKey("biome");
                if (Language.getInstance().hasTranslation(name)) {
                    this.text = Text.translatable(name);
                } else {
                    this.text = Text.literal(id.toString());
                }
            }

            @Override
            public Text getNarration() {
                return Text.translatable("narrator.select", this.text);
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                context.drawTextWithShadow(ModernBetaSelectBiomeScreen.this.textRenderer, this.text, x + 5, y + 2, 0xFFFFFF);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                ModernBetaSelectBiomeScreen.BiomeListWidget.this.setSelected(this);
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }
    }
}
