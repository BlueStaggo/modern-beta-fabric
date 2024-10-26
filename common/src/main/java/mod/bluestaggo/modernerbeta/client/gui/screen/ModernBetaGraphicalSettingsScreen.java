package mod.bluestaggo.modernerbeta.client.gui.screen;

import mod.bluestaggo.modernerbeta.client.gui.optioncallbacks.*;
import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public abstract class ModernBetaGraphicalSettingsScreen<T extends NbtElement> extends GameOptionsScreen {
    protected static final String STRING_PREFIX = "createWorld.customize.modern_beta.settings.";

    protected final T settings;
    protected final GeneratorOptionsHolder generatorOptionsHolder;
    protected final Consumer<T> onDone;
    protected final String type;

    private double prevScroll = -1.0D;

    public ModernBetaGraphicalSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        String type,
        T settings,
        Consumer<T> onDone
    ) {
        super(parent, null, Text.translatable(title));

        this.onDone = onDone;
        this.type = type;
        this.settings = settings;
        this.generatorOptionsHolder = generatorOptionsHolder;
    }

    protected abstract void addOptions(OptionListWidget list);

    @Override
    protected void addOptions() {
    }

    @Override
    public void removed() {
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    protected void clearChildren() {
        if (this.body != null) {
            this.prevScroll = this.body.getScrollAmount();
        }

        super.clearChildren();

        ((ModernBetaClearableWidget)this.layout).modernBeta$clear();
    }

    @Override
    protected void initBody() {
        this.body = this.layout.addBody(new OptionListWidget(this.client, this.width, this));
        this.addOptions(this.body);
        if (this.prevScroll >= 0.0D && this.body != null) {
            this.body.setScrollAmount(this.prevScroll);
        }
    }

    @Override
    protected void initFooter() {
        GridWidget gridWidget = new GridWidget().setColumnSpacing(8);
        GridWidget.Adder gridWidgetAdder = gridWidget.createAdder(2);

        gridWidgetAdder.add(ButtonWidget.builder(
            Text.translatable("createWorld.customize.modern_beta.settings.save"),
            onPress -> {
                this.onDone.accept(this.getResult());
                this.client.setScreen(this.parent);
            }
        ).dimensions(this.width / 2 - 155, this.height - 28, 150, 20).build());

        gridWidgetAdder.add(ButtonWidget.builder(
            ScreenTexts.CANCEL,
            onPress -> this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, 150, 20).build());

        this.layout.addFooter(gridWidget);
    }

    protected T getResult() {
        return this.settings;
    }

    protected String getTextKey(String key) {
        return getTextKey(key, null);
    }

    protected String getTextKey(String key, String subKey) {
        String text = STRING_PREFIX + type + "." + key;
        if (subKey != null) {
            text += "." + subKey;
        }
        return text;
    }

    protected MutableText getText(String key) {
        return this.getText(key, null);
    }

    protected MutableText getText(String key, String subKey) {
        return Text.translatable(getTextKey(key, subKey));
    }

    protected SimpleOption<Void> headerOption(Text text) {
        return this.headerOption(text, 0.5F);
    }

    protected SimpleOption<Void> headerOption(Text text, float alignment) {
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> text,
            new TextLabelCallbacks(text, alignment),
            null,
            value -> {}
        );
    }

    protected SimpleOption<Void> placeholderOption(String key) {
        return this.headerOption(this.getText(key).formatted(Formatting.RED, Formatting.ITALIC));
    }

    protected SimpleOption<Void> customButton(Text text, Runnable onPress) {
        return new SimpleOption<>(
            "",
            SimpleOption.emptyTooltip(),
            (optionText, value) -> Text.empty(),
            new CustomButtonCallbacks(text, onPress),
            null,
            value -> {}
        );
    }
}
