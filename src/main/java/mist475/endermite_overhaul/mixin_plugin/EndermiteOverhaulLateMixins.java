package mist475.endermite_overhaul.mixin_plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

/**
 * Late Mixin loader, adapted from BugTorch
 */
@LateMixin
public class EndermiteOverhaulLateMixins implements ILateMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.endermite_overhaul.late.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedMods) {
        var mixins = new ArrayList<String>();
        mixins.add("MixinEntityEndermite");
        return mixins;
    }

}
