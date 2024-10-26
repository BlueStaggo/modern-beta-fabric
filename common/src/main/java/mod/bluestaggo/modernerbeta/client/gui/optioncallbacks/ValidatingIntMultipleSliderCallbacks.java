package mod.bluestaggo.modernerbeta.client.gui.optioncallbacks;

import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public record ValidatingIntMultipleSliderCallbacks(int minInclusive, int maxInclusive, int multiple) implements SimpleOption.IntSliderCallbacks {
    public Optional<Integer> validate(Integer integer) {
        return integer.compareTo(this.minInclusive()) >= 0 && integer.compareTo(this.maxInclusive()) <= 0 ? Optional.of(integer) : Optional.empty();
    }

    public Codec<Integer> codec() {
        return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
    }

    @Override
    public Integer toValue(double d) {
        return MathHelper.roundDownToMultiple(MathHelper.map(d, 0.0, 1.0, (double)this.minInclusive(), (double)this.maxInclusive()), multiple);
    }
}