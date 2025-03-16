package mist475.endermite_overhaul.mixin_plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.IEarlyMixinLoader;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import mist475.endermite_overhaul.Config;

/**
 * Early Mixin loader, adapted from BugTorch
 */
@IFMLLoadingPlugin.Name("EndermiteOverhaulEarlyMixins")
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class EndermiteOverhaulEarlyMixins implements IFMLLoadingPlugin, IEarlyMixinLoader {

    @Override
    public String getMixinConfig() {
        return "mixins.endermite_overhaul.early.json";
    }

    @Override
    public List<String> getMixins(Set<String> loadedCoreMods) {
        Config.synchronizeConfiguration(Config.file);
        var mixins = new ArrayList<String>();
        if (Config.removeVanillaEndermenSpawn) {
            mixins.add("MixinBiomeGenBase");
        }
        return mixins;
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
