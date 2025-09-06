package mist475.endermite_overhaul.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenMushroomIsland;
import net.minecraft.world.gen.feature.WorldGenerator;

import mist475.endermite_overhaul.Config;
import mist475.endermite_overhaul.EndermiteOverhaulUtils;
import mist475.endermite_overhaul.ModBlocks;

public class WorldGenEndermiteEgg extends WorldGenerator {

    private final Block endermiteEggBlock;

    public WorldGenEndermiteEgg() {
        this(ModBlocks.ENDERMITE_EGG.get());
    }

    public WorldGenEndermiteEgg(Block block) {
        this.endermiteEggBlock = block;
    }

    public boolean generate(World world, Random random, int chunkCentreX, int ignored, int chunkCentreZ) {
        /*
         * Whitelist mode: only generate if in list
         * Blacklist mode: only generate if not in list
         */
        if (Config.endermiteEggGenerationCustomDimensionIds.contains(world.provider.dimensionId)
            == Config.endermiteEggGenerationMode) {
            var setting = Config.getDimSettingOrDefault(world.provider.dimensionId);
            if (random.nextInt(setting.endermiteEggSpawnChance()) == 0
                && !(world.getBiomeGenForCoords(chunkCentreX, chunkCentreZ) instanceof BiomeGenMushroomIsland)) {
                int randomX = chunkCentreX - random.nextInt(8) + random.nextInt(8);
                int randomZ = chunkCentreZ - random.nextInt(8) + random.nextInt(8);
                for (int y = setting.minY(); y < setting.maxY(); ++y) {
                    if (world.isAirBlock(randomX, y, randomZ)) {
                        var belowY = y - 1;
                        var below = world.getBlock(randomX, belowY, randomZ);
                        // in the end, allow egg spawns regardless
                        if (below.isOpaqueCube() && below.getMaterial() == Material.rock
                            && (setting.spawnOnSurface()
                                || belowY != world.getTopSolidOrLiquidBlock(randomX, randomZ))) {
                            world.setBlock(randomX, y, randomZ, this.endermiteEggBlock, 0, 2);
                            // start from the middle of the chunk
                            spawnAdditionalEggs(world, random, chunkCentreX, y, chunkCentreZ);
                            // only add 1 egg group per chunk
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Once a spot has been found, try to spawn between 3 and 1 extra eggs within 6 blocks of the egg
     */
    private void spawnAdditionalEggs(World world, Random random, int x, int y, int z) {
        var amountToTry = random.nextInt(Config.maxEndermiteEggGroupSize - 1) + 1;
        var positions = EndermiteOverhaulUtils.getEndermiteEggSpawnPositions(random);
        for (var pos : positions) {
            if (amountToTry == 0) {
                break;
            }
            var i = pos.getLeft() + x;
            var j = pos.getMiddle() + y;
            var k = pos.getRight() + z;
            if (!world.isAirBlock(i, j, k)) {
                continue;
            }
            var below = world.getBlock(i, j - 1, k);
            if (below.isOpaqueCube() && below.getMaterial() == Material.rock) {
                world.setBlock(i, j, k, this.endermiteEggBlock, 0, 2);
                amountToTry--;
            }
        }
    }
}
