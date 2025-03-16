package mist475.endermite_overhaul;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = EndermiteOverhaul.MODID,
    version = Tags.VERSION,
    name = "EndermiteOverhaul",
    acceptedMinecraftVersions = "[1.7.10]")
public class EndermiteOverhaul {

    public static final String MODID = "endermite_overhaul";

    @SidedProxy(
        clientSide = "mist475.endermite_overhaul.ClientProxy",
        serverSide = "mist475.endermite_overhaul.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
