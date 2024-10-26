package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public record FloatSliderCallbacks(float min, float max) implements SimpleOption.SliderCallbacks<Float> {
    public Optional<Float> validate(Float value) {
        return value >= this.min && value <= this.max ? Optional.of(value) : Optional.empty();
    }

    public double toSliderProgress(Float value) {
        return MathHelper.map(value, this.min, this.max, 0.0F, 1.0F);
    }

    public Float toValue(double value) {
        return MathHelper.map((float)value, 0.0F, 1.0F, this.min, this.max);
    }

    public Codec<Float> codec() {
        return Codec.FLOAT;
    }
}