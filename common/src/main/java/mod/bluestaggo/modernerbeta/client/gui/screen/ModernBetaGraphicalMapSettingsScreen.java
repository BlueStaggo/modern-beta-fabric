package mod.bluestaggo.modernerbeta.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class ModernBetaGraphicalMapSettingsScreen extends ModernBetaGraphicalCompoundSettingsScreen {
    protected static final String KEY = "$MB MAP KEY$.";
    protected static final String VALUE = "$MB MAP VALUE$.";

    private final NbtCompound keys = new NbtCompound();
    private final NbtCompound values = new NbtCompound();

    public ModernBetaGraphicalMapSettingsScreen(
        String title,
        Screen parent,
        GeneratorOptionsHolder generatorOptionsHolder,
        NbtCompound settings,
        Consumer<NbtCompound> onDone
    ) {
        super(title, parent, generatorOptionsHolder, "list", settings, onDone);

        int i = 0;
        for (String key : this.settings.getKeys()) {
            String si = String.valueOf(i);
            this.keys.putString(si, key);
            this.values.put(si, this.settings.get(key));
            i++;
        }
    }

    protected abstract List<SimpleOption<?>> getOptions(int i);

    protected abstract String getDefaultKey();

    protected abstract NbtElement getDefaultValue();

    @Override
    protected NbtCompound getResult() {
        NbtCompound compound = new NbtCompound();
        for (int i = 0; i < this.keys.getSize(); i++) {
            String si = String.valueOf(i);
            compound.put(this.keys.getString(si), this.values.get(si));
        }
        return compound;
    }

    @Override
    protected void addOptions(OptionListWidget list) {
        for (int i = 0; i < this.keys.getSize(); i++) {
            final int finalI = i;
            String si = String.valueOf(i);

            List<SimpleOption<?>> options = new ArrayList<>(this.getOptions(i));
            if (options.isEmpty()) {
                continue;
            }

            SimpleOption<?> removeButton = this.customButton(
                this.getText("remove"),
                () -> {
                    int size = this.keys.getSize();
                    this.keys.remove(si);
                    this.values.remove(si);

                    for (int j = finalI; j < size; j++) {
                        String sj = String.valueOf(j);
                        String sjm1 = String.valueOf(j - 1);

                        NbtElement keyV = this.keys.get(sj);
                        NbtElement valueV = this.values.get(sj);
                        this.keys.remove(sj);
                        this.values.remove(sj);
                        if (keyV != null && valueV != null) {
                            this.keys.put(sjm1, keyV);
                            this.values.put(sjm1, valueV);
                        }
                    }

                    this.clearAndInit();
                }
            );

            options.add(removeButton);
            if (options.size() % 2 == 1) {
                options.add(null);
            }

            for (int j = 0; j < options.size(); j += 2) {
                SimpleOption<?> left = options.get(j);
                SimpleOption<?> right = options.get(j + 1);

                if (right != null) {
                    list.addAll(left, right);
                } else {
                    list.addSingleOptionEntry(left);
                }
            }
        }

        list.addSingleOptionEntry(this.headerOption(Text.empty()));
        list.addSingleOptionEntry(this.customButton(
            this.getText("add"),
            () -> {
                String si = String.valueOf(this.keys.getSize());
                this.keys.putString(si, this.getDefaultKey());
                this.values.put(si, this.getDefaultValue());
                this.clearAndInit();
            }
        ));
    }

    @Override
    protected Pair<NbtCompound, String> resolveSettings(String key) {
        if (key.startsWith(KEY)) {
            return new Pair<>(keys, key.substring(KEY.length()));
        } else if (key.startsWith(VALUE)) {
            return new Pair<>(values, key.substring(VALUE.length()));
        }
        return super.resolveSettings(key);
    }

    @FunctionalInterface
    public interface Constructor {
        ModernBetaGraphicalMapSettingsScreen create(
            String title,
            Screen parent,
            GeneratorOptionsHolder generatorOptionsHolder,
            NbtCompound settings,
            Consumer<NbtCompound> onDone
        );
    }
}
