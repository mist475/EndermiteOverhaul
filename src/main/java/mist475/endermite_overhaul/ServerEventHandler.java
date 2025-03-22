package mist475.endermite_overhaul;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mist475.endermite_overhaul.worldgen.WorldGenEndermiteEgg;

public class ServerEventHandler {

    public static final ServerEventHandler INSTANCE = new ServerEventHandler();
    private static final WorldGenEndermiteEgg worldGenEndermiteEgg = new WorldGenEndermiteEgg();
    public Achievement endermiteEggAchievement;

    @SubscribeEvent
    public void decorate(PopulateChunkEvent.Post decorationEvent) {
        if (Config.enableEndermiteEggSpawning) {
            // y is handled in generate itself
            worldGenEndermiteEgg.generate(
                decorationEvent.world,
                decorationEvent.rand,
                (decorationEvent.chunkX << 4) + 8,
                0,
                (decorationEvent.chunkZ << 4) + 8);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && Config.removeVanillaEndermenSpawn
            && Config.endermiteEggAchievement
            && event.entity instanceof EntityEnderman
            && event.source.getEntity() instanceof EntityPlayer entityPlayer) {
            entityPlayer.triggerAchievement(endermiteEggAchievement);
        }
    }
}
