package mod.bluestaggo.modernerbeta.mixin.client;

import mod.bluestaggo.modernerbeta.api.world.chunk.ChunkProviderFinite;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelLoadingScreen.class)
public abstract class MixinLevelLoadingScreen extends Screen {
    protected MixinLevelLoadingScreen(Text title) {
        super(title);
    }

    @Inject(method = " <init>(Lnet/minecraft/client/gui/WorldGenerationProgressTracker;)V", at = @At("TAIL"))
    private void injectInit(WorldGenerationProgressTracker progressProvider, CallbackInfo info) {
        ChunkProviderFinite.resetPhase();
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void injectRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        String phase = ChunkProviderFinite.getPhase();
        
        if (!phase.isBlank()) {
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                phase,
                this.width / 2,
                (this.height / 2) + 90,
                0xFFFFFF
            );
        }
    }
}
