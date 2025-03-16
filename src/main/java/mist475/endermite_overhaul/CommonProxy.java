package mist475.endermite_overhaul;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
}
