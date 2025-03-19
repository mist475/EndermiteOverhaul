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
    public static int endermiteEggSpawnChance;
    public static int endermiteEggUpperGenerationBound;
    public static int endermiteEggLowerGenerationBound;
    public static int maxEndermiteEggGroupSize;
    public static int maxEndermiteEggHatchTime;
    public static int minEndermiteEggHatchTime;
    public static int endermenAggroRange;

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

        endermiteEggSpawnChance = configuration.getInt(
            "endermiteEggSpawnChance",
            "main",
            10,
            1,
            100,
            "How often a group of endermite eggs occur ( one in n, so higher = rarer)");

        endermiteEggUpperGenerationBound = configuration.getInt(
            "endermiteEggUpperGenerationBound",
            "main",
            128,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Max y height for endermite eggs to spawn");

        endermiteEggLowerGenerationBound = configuration.getInt(
            "endermiteEggLowerGenerationBound",
            "main",
            0,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE,
            "Min y height for endermite eggs to spawn");

        maxEndermiteEggGroupSize = configuration
            .getInt("maxEndermiteEggGroupSize", "main", 3, 1, 256, "Max group size for endermite eggs");

        maxEndermiteEggHatchTime = configuration.getInt(
            "maxEndermiteEggHatchTime",
            "main",
            36000,
            0,
            Integer.MAX_VALUE,
            "Max time in ticks for endermites to hatch");

        minEndermiteEggHatchTime = configuration.getInt(
            "minEndermiteEggHatchTime",
            "main",
            12000,
            0,
            Integer.MAX_VALUE,
            "Max time in ticks for endermites to hatch");

        endermenAggroRange = configuration.getInt(
            "endermenAggroRange",
            "main",
            32,
            0,
            Integer.MAX_VALUE,
            "Radius where endermen aggro on you for hitting an endermite or destroying an egg");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
