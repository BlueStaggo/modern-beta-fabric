package mod.bluestaggo.modernerbeta.client.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public abstract class ModernBetaScreen extends Screen {
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_LENGTH = 150;
    public static final int BUTTON_LENGTH_PRESET = 200;
    public static final int BUTTON_HEIGHT_PRESET = 20;
    
    protected final Screen parent;
    protected int overlayLeft;
    protected int overlayRight;
    protected int overlayTop;
    protected int overlayBottom;
    
    public ModernBetaScreen(Text title, Screen parent) {
        super(title);
        
        this.parent = parent;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFF);
    }
    
    @Override
    protected void init() {
        this.overlayLeft = 0;
        this.overlayRight = this.width;
        this.overlayTop = 32;
        this.overlayBottom = this.height - 32;
    }
    
    protected GridWidget createGridWidget() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter().alignTop();
        
        return gridWidget;
    }
    
    protected void addGridTextButtonPair(GridWidget.Adder adder, String text, ButtonWidget buttonWidget) {
        adder.add(new TextWidget(Text.translatable(text), this.textRenderer));
        adder.add(buttonWidget);
    }

    protected void addGridTextButtonTriplet(GridWidget.Adder adder, String text, ButtonWidget buttonWidget, ButtonWidget buttonWidget2) {
        adder.add(new TextWidget(Text.translatable(text), this.textRenderer));
        adder.add(buttonWidget);
        adder.add(buttonWidget2);
    }
}
