package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.imixin.ModernBetaClearableWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ThreePartsLayoutWidget.class)
public class MixinThreePartsLayoutWidget implements ModernBetaClearableWidget {
    @Shadow @Final private SimplePositioningWidget header;
    @Shadow @Final private SimplePositioningWidget body;
    @Shadow @Final private SimplePositioningWidget footer;

    @Override
    public void modernBeta$clear() {
        ((ModernBetaClearableWidget)this.header).modernBeta$clear();
        ((ModernBetaClearableWidget)this.body).modernBeta$clear();
        ((ModernBetaClearableWidget)this.footer).modernBeta$clear();
    }
}
