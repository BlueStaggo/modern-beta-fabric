package mod.bluestaggo.modernerbeta.mixin.client;

import net.minecraft.client.gui.widget.TextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextWidget.class)
public interface AccessorTextWidget {
    @Invoker
    TextWidget invokeAlign(float value);
}
