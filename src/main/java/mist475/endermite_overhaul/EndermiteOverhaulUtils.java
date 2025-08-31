package mist475.endermite_overhaul;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Triple;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EndermiteOverhaulUtils {

    public static void aggroEndermenOnAttacker(World world, double posX, double posY, double posZ, int range,
        Entity source) {
        double radius = range / 2.0;
        world
            .getEntitiesWithinAABB(
                EntityEnderman.class,
                AxisAlignedBB
                    .getBoundingBox(posX - radius, posY - 4, posZ - radius, posX + radius, posY + 4, posZ + radius))
            .forEach(enderman -> {
                if (enderman.getEntityToAttack() == null) {
                    enderman.setTarget(source);
                    enderman.setScreaming(true);
                }
            });
    }

    public static String getUnlocalisedName(String name) {
        return EndermiteOverhaul.MODID + "." + name;
    }

    /**
     * Spawn endermen in a 16 block radius
     */
    public static void spawnEndermenCloseToEgg(World world, double posX, double posY, double posZ) {
        int x = (int) posX;
        int y = (int) posY;
        int z = (int) posZ;
        for (var triple : getEndermenSpawnPositions(world.rand)) {
            var enderman = new EntityEnderman(world);
            var i = triple.getLeft() + x;
            var j = triple.getMiddle() + y;
            var k = triple.getRight() + z;
            enderman.setLocationAndAngles(i, j, k, 0.0F, 0.0F);
            if (enderman.getCanSpawnHere()) {
                world.spawnEntityInWorld(enderman);

                short short1 = 128;
                for (int l = 0; l < short1; ++l) {
                    double d6 = (double) l / ((double) short1 - 1.0D);
                    float velocityX = (world.rand.nextFloat() - 0.5F) * 0.2F;
                    float velocityY = (world.rand.nextFloat() - 0.5F) * 0.2F;
                    float velocityZ = (world.rand.nextFloat() - 0.5F) * 0.2F;
                    double particleX = x + (enderman.posX - x) * d6
                        + (world.rand.nextDouble() - 0.5D) * (double) enderman.width * 2.0D;
                    double particleY = y + (enderman.posY - y) * d6
                        + world.rand.nextDouble() * (double) enderman.height;
                    double particleZ = z + (enderman.posZ - z) * d6
                        + (world.rand.nextDouble() - 0.5D) * (double) enderman.width * 2.0D;
                    world.spawnParticle("portal", particleX, particleY, particleZ, velocityX, velocityY, velocityZ);
                }

                world.playSoundEffect(i, j, k, "mob.endermen.portal", 1.0F, 1.0F);
                enderman.playSound("mob.endermen.portal", 1.0F, 1.0F);
                return;
            }
        }
    }

    /*
     * These functions were split up to reduce the number of allocations
     */

    private static final List<Triple<Integer, Integer, Integer>> endermenSpawnPositions = getPositions(16, 4, 16);

    public static List<Triple<Integer, Integer, Integer>> getEndermenSpawnPositions(Random rand) {
        var list = new ArrayList<>(endermenSpawnPositions);
        Collections.shuffle(list, rand);
        return list;
    }

    private static final List<Triple<Integer, Integer, Integer>> endermiteEggSpawnPositions = getPositions(6, 4, 6);

    public static List<Triple<Integer, Integer, Integer>> getEndermiteEggSpawnPositions(Random rand) {
        var list = new ArrayList<>(endermiteEggSpawnPositions);
        Collections.shuffle(list, rand);
        return list;
    }

    public static List<Triple<Integer, Integer, Integer>> getPositions(int xRange, int yRange, int zRange) {
        int x = 0, y = 0, z = 0;
        var list = new ArrayList<Triple<Integer, Integer, Integer>>();
        for (int j = y + yRange; j > y - yRange; j--) {
            for (int i = x - xRange; i < x + xRange; i++) {
                for (int k = z - zRange; k < z + zRange; k++) {
                    list.add(Triple.of(i, j, k));
                }
            }
        }
        return list;
    }

    @SideOnly(Side.CLIENT)
    public static void blockDestroyParticles(World world, int x, int y, int z) {
        var block = ModBlocks.ENDERMITE_EGG.get();
        world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (world.getBlockMetadata(x, y, z) << 12));
    }
}
