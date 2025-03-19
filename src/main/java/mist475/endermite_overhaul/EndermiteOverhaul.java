package mist475.endermite_overhaul;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import mist475.endermite_overhaul.rendering.BlockBreakParticleHandler;
import mist475.endermite_overhaul.rendering.BlockBreakParticleMessage;

@Mod(
    modid = EndermiteOverhaul.MODID,
    version = Tags.VERSION,
    name = "EndermiteOverhaul",
    acceptedMinecraftVersions = "[1.7.10]")
public class EndermiteOverhaul {

    public static final String MODID = "endermite_overhaul";
    public static SimpleNetworkWrapper networkWrapper;

    @SidedProxy(
        clientSide = "mist475.endermite_overhaul.ClientProxy",
        serverSide = "mist475.endermite_overhaul.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(EndermiteOverhaul.MODID);
        networkWrapper
            .registerMessage(BlockBreakParticleHandler.class, BlockBreakParticleMessage.class, 0, Side.CLIENT);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
