package mist475.endermite_overhaul;

import java.util.stream.Collectors;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(Config.file);
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
    }
}
