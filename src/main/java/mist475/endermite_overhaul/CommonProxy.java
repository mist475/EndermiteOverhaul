package mist475.endermite_overhaul;

import java.io.File;
import java.util.stream.Collectors;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(
            new File(Launch.minecraftHome, "config" + File.separator + "endermite_overhaul.cfg"));
        ModBlocks.init();
    }

    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(ServerEventHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        GameRegistry.registerTileEntity(TileEntityEndermiteEgg.class, "EndermiteEgg");
    }

    public void postInit(FMLPostInitializationEvent event) {
        // get rid of efr nether endermen in case they're there
        if (Config.removeVanillaEndermenSpawn) {
            BiomeGenBase.hell.spawnableMonsterList = BiomeGenBase.hell.spawnableMonsterList.stream()
                .filter(entry -> entry.entityClass != EntityEnderman.class)
                .collect(Collectors.toList());
        }
        if (Config.removeVanillaEndermenSpawn && Config.endermiteEggAchievement) {
            ServerEventHandler.INSTANCE.endermiteEggAchievement = new Achievement(
                "achievement.endermiteEgg",
                "endermiteEgg",
                1,
                10,
                ModBlocks.ENDERMITE_EGG.get(),
                AchievementList.blazeRod).registerStat();
            AchievementList.theEnd.parentAchievement = ServerEventHandler.INSTANCE.endermiteEggAchievement;
        }
    }
}
