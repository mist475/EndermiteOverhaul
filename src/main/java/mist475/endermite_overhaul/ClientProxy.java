package mist475.endermite_overhaul;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mist475.endermite_overhaul.rendering.ItemEndermiteEggRender;
import mist475.endermite_overhaul.rendering.RenderTileEntityEndermiteEgg;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        GameRegistry.registerTileEntity(TileEntityEndermiteEgg.class, "tileEndermiteEgg");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEndermiteEgg.class, new RenderTileEntityEndermiteEgg());
        MinecraftForgeClient
            .registerItemRenderer(Item.getItemFromBlock(ModBlocks.ENDERMITE_EGG.get()), new ItemEndermiteEggRender());
    }
}
