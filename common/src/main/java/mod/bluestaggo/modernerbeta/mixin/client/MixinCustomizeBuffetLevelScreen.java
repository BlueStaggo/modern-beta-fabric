package mod.bluestaggo.modernerbeta.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaGraphicalSettingsScreen;
import mod.bluestaggo.modernerbeta.client.gui.screen.ModernBetaScreen;
import net.minecraft.client.gui.screen.world.CustomizeBuffetLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CustomizeBuffetLevelScreen.class)
public abstract class MixinCustomizeBuffetLevelScreen {
    @Unique private static final Text MODERN_BETA_BIOME_TEXT =
        Text.translatable("createWorld.customize.modern_beta.settings.biome_picker_subtitle");

    @Shadow @Final private static Text BUFFET_BIOME_TEXT;

    @Shadow @Final private Screen parent;

    @ModifyConstant(
        method = "<init>",
        constant = @Constant(stringValue = "createWorld.customize.buffet.title")
    )
    private static String modifyTitle(String constant, @Local(argsOnly = true, ordinal = 0) Screen parent) {
        if (parent instanceof ModernBetaGraphicalSettingsScreen<?>) {
            return "createWorld.customize.modern_beta.title.biome_picker";
        }
        return constant;
    }

    @Redirect(
        method = "init",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/world/CustomizeBuffetLevelScreen;BUFFET_BIOME_TEXT:Lnet/minecraft/text/Text;"
        )
    )
    private Text modifySubtitle() {
        if (this.parent instanceof ModernBetaGraphicalSettingsScreen<?>) {
            return MODERN_BETA_BIOME_TEXT;
        }
        return BUFFET_BIOME_TEXT;
    }
}
