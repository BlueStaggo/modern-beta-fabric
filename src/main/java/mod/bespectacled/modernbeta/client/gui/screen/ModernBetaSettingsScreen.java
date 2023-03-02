package mod.bespectacled.modernbeta.client.gui.screen;

import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import mod.bespectacled.modernbeta.settings.ModernBetaSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class ModernBetaSettingsScreen extends ModernBetaScreen {
    private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
    private static final String TEXT_INVALID = "createWorld.customize.modern_beta.invalid";
    
    private final Consumer<String> onDone;
    private final Gson gson;
    private String settingsString;
    
    private ButtonWidget widgetDone;
    private EditBoxWidget widgetSettings;
    private TextWidget widgetInvalid;
    
    public ModernBetaSettingsScreen(String title, Screen parent, ModernBetaSettings settings, Consumer<String> onDone) {
        super(Text.translatable(title), parent);

        this.onDone = onDone;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.settingsString = this.gson.toJson(settings);
    }
    
    @Override
    protected void init() {
        super.init();
        
        this.widgetDone = ButtonWidget.builder(ScreenTexts.DONE, button -> {
            this.onDone.accept(this.settingsString);
            this.client.setScreen(this.parent);
        }).dimensions(this.width / 2 - 155, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build();
        
        this.addDrawableChild(this.widgetDone);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> 
            this.client.setScreen(this.parent)
        ).dimensions(this.width / 2 + 5, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());
        
        int editBoxWidth = (int)(this.width * 0.8);
        int editBoxHeight = (int)(this.height * 0.6);
        
        this.widgetSettings = new EditBoxWidget(this.textRenderer, 0, 0, editBoxWidth, editBoxHeight, Text.of(""), Text.translatable(TEXT_SETTINGS));
        this.widgetSettings.setText(this.settingsString);
        this.widgetSettings.setChangeListener(string -> {
            this.settingsString = string;
            this.onChange();
        });
        
        Text textInvalid = Text.translatable(TEXT_INVALID).fillStyle(Style.EMPTY.withColor(16711680));
        this.widgetInvalid = new TextWidget(textInvalid, this.textRenderer);
        
        GridWidget gridWidget = this.createGridWidget();
        
        GridWidget.Adder gridWidgetAdder = gridWidget.createAdder(1);
        gridWidgetAdder.add(this.widgetSettings);
        gridWidgetAdder.add(this.widgetInvalid);
        
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
        
        this.onChange();
    }
    
    private void onChange() {
        boolean isValid = this.isValidJson(this.settingsString);
        
        this.widgetDone.active = isValid;
        this.widgetInvalid.visible = !isValid;
    }
    
    private boolean isValidJson(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        
        return true;
    }
}

