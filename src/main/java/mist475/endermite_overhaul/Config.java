package mist475.endermite_overhaul;

import java.io.File;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.util.MathHelper;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.github.bsideup.jabel.Desugar;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class Config {

    public static boolean enableEndermiteEggSpawning;
    public static boolean endermiteEggGenerationMode;
    public static TIntList endermiteEggGenerationCustomDimensionIds;
    public static boolean removeVanillaEndermenSpawn;
    public static int maxEndermiteEggGroupSize;
    public static int maxEndermiteEggHatchTime;
    public static int minEndermiteEggHatchTime;
    public static int endermenAggroRange;
    public static boolean endermiteEggAchievement;
    private static TIntObjectMap<DimSetting> endermiteEggGenerationConfig;
    private static DimSetting defaultEndermiteEggGenerationConfig;

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
            true,
            "Set to true to make endermite eggs the only way to obtain endermen (unless other mods add spawns). This does not effect endermen spawning in the end.");

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

        endermiteEggAchievement = configuration.getBoolean(
            "endermiteEggAchievement",
            "main",
            true,
            "Adds an achievement to locate an endermen spawned by an endermite egg between the \"Into Fire\" and the \"The End?\" Achievements");

        endermiteEggGenerationConfig = new TIntObjectHashMap<>();
        endermiteEggGenerationConfig.putAll(
            Arrays.stream(
                configuration.getStringList(
                    "endermiteEggGenerationConfig",
                    "main",
                    ArrayUtils.toArray("10,0,128,false,-1", "10,0,128,false,0", "10,0,128,true,1"),
                    "Per dimension generation settings for endermite egg spawning.\nAny entry not mentioned uses the default config.\nSettings are comma separated. The spawn chance = 1 / n \nformat=endermiteEggSpawnChance,minY,maxY,spawnOnSurface,dimensionId (int,int,int,boolean,int)"))
                .map(s -> DimSetting.fromStringList(s.split(","), false))
                .collect(Collectors.toMap(DimSetting::dimensionId, Function.identity())));

        defaultEndermiteEggGenerationConfig = DimSetting.fromStringList(
            configuration.getString(
                "defaultEndermiteEggGenerationConfig",
                "main",
                "10,0,128,false",
                "default generation for endermite eggs (used when there is no config)\nThe spawn chance = 1 / n \nformat=endermiteEggSpawnChance,minY,maxY,spawnOnSurface (int,int,int,boolean)")
                .split(","),
            true);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static DimSetting getDimSettingOrDefault(int dimId) {
        if (endermiteEggGenerationConfig.containsKey(dimId)) {
            endermiteEggGenerationConfig.get(dimId);
        }
        return defaultEndermiteEggGenerationConfig;
    }

    @Desugar
    public record DimSetting(int dimensionId, int endermiteEggSpawnChance, int minY, int maxY, boolean spawnOnSurface) {

        public static DimSetting fromStringList(String[] input, boolean isDefault) {
            if (input.length != (isDefault ? 4 : 5) || !NumberUtils.isNumber(input[0])
                || !NumberUtils.isNumber(input[1])
                || !NumberUtils.isNumber(input[2])
                || (!input[3].equals("true") && !input[3].equals("false"))
                || (!isDefault && !NumberUtils.isNumber(input[4]))) {
                throw new IllegalArgumentException(
                    "input string is not splittable to int,int,int,boolean, int (only if not default). e.g. (1,1,1,1,false,0)");
            }
            // set between 0 and 100
            var spawnChance = MathHelper.clamp_int(NumberUtils.createInteger(input[0]), 0, 100);
            var minY = NumberUtils.createInteger(input[1]);
            var maxY = NumberUtils.createInteger(input[2]);
            var spawnOnSurface = input[3].equals("true");
            var dimId = isDefault ? 0 : Integer.parseInt(input[4]);
            return new DimSetting(dimId, spawnChance, minY, maxY, spawnOnSurface);
        }
    }
}
