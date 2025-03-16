package mist475.endermite_overhaul;

import net.minecraft.block.Block;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Adapted from efr, used for easy config handling
 */
public enum ModBlocks {

    ENDERMITE_EGG(new BlockEndermiteEgg().setHardness(0.0F));

    public static final ModBlocks[] VALUES = values();

    public static void init() {
        for (ModBlocks block : VALUES) {
            GameRegistry.registerBlock(
                block.get(),
                block.name()
                    .toLowerCase());
        }
    }

    private final Block theBlock;

    ModBlocks(Block block) {
        theBlock = block;
    }

    public Block get() {
        return theBlock;
    }
}
