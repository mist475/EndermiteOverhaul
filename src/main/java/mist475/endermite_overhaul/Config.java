package mist475.endermite_overhaul;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.ArrayUtils;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

public class Config {

    public static File file = new File(
        Launch.minecraftHome + File.separator + "config" + File.separator + "endermite_overhaul.cfg");
    public static boolean enableEndermiteEggSpawning;
    public static boolean endermiteEggGenerationMode;
    public static TIntList endermiteEggGenerationCustomDimensionIds;
    public static boolean removeVanillaEndermenSpawn;

    public static void synchronizeConfiguration(File configFile) {
        var configuration = new Configuration(configFile);

        enableEndermiteEggSpawning = configuration.getBoolean(
            "enableEndermiteEggSpawning",
            "main",
            true,
            "enable endermite eggs that spawn in caves and are dropped by mobs");

        endermiteEggGenerationMode = configuration.getBoolean(
            "endermiteEggGenerationMode",
            "main",
            true,
            "set true to use a whitelist for endermite egg generation, set to false for a blacklist");

        endermiteEggGenerationCustomDimensionIds = new TIntArrayList();
        endermiteEggGenerationCustomDimensionIds.addAll(
            Arrays.stream(
                configuration.getStringList(
                    "endermiteEggGenerationCustomDimensionIds",
                    "main",
                    ArrayUtils.toArray("-1", "0", "1"),
                    "Dimension ids of dimensions where endermite eggs should spawn underground (or where they should NOT spawn if blacklist is enabled"))
                .map(Integer::parseInt)
                .collect(Collectors.toList()));

        removeVanillaEndermenSpawn = configuration.getBoolean(
            "removeVanillaEndermenSpawn",
            "main",
            false,
            "Set to true to make endermite eggs the only way to obtain endermen (unless other mods add spawns). This does not effect endermen spawning in the end.");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
