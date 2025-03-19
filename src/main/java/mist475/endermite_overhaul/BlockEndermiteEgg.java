package mist475.endermite_overhaul;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.etfuturum.client.sound.ModSounds;
import ganymedes01.etfuturum.entities.EntityEndermite;
import mist475.endermite_overhaul.rendering.BlockBreakParticleMessage;

public class BlockEndermiteEgg extends Block implements ITileEntityProvider {

    private static final Random rand = new Random();

    protected BlockEndermiteEgg() {
        super(Material.dragonEgg);
        this.setTickRandomly(true);
        setHardness(1.5F);
        setResistance(10.0F);
        setStepSound(ModSounds.soundSlime);
        setBlockTextureName("endermite_overhaul:endermite_egg");
        setBlockName(EndermiteOverhaulUtils.getUnlocalisedName("endermite_egg"));
        setCreativeTab(CreativeTabs.tabMisc);
    }

    // RENDERING

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        for (var l = x - 2; l <= x + 2; ++l) {
            for (var i1 = z - 2; i1 <= z + 2; ++i1) {
                if (l > x - 2 && l < x + 2 && i1 == z - 1) {
                    i1 = z + 2;
                }

                if (random.nextInt(16) == 0) {
                    for (var j1 = y; j1 <= y + 1; ++j1) {
                        if (!world.isAirBlock((l - x) / 2 + x, j1, (i1 - z) / 2 + z)) {
                            break;
                        }
                        world.spawnParticle(
                            "portal",
                            (double) x + 0.5D,
                            (double) y + 0.5D,
                            (double) z + 0.5D,
                            (double) ((float) (l - x) + random.nextFloat()) - 0.5D,
                            (float) (j1 - y) - random.nextFloat() - 1.0F,
                            (double) ((float) (i1 - z) + random.nextFloat()) - 0.5D);
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(getTextureName());

    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    // COMPARATOR

    public boolean hasComparatorInputOverride() {
        return true;
    }

    /**
     * Return quartile of age/hatchTime (new eggs get 0, eggs close to hatching 3)
     */
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        var te = (TileEntityEndermiteEgg) world.getTileEntity(x, y, z);
        return te.getAge() / te.getHatchTime() * 3;
    }

    // BREAKING / PLACING

    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float fallDistance) {
        if (!world.isRemote && !entity.isSneaking() && fallDistance > 0.5F) {
            EndermiteOverhaulUtils.aggroEndermenOnAttacker(world, x, y, z, 32, entity);
            if (!(entity instanceof EntityPlayer) && !world.getGameRules()
                .getGameRuleBooleanValue("mobGriefing")) {
                return;
            }
            destroyBlockEffects(world, x, y, z);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        // every egg will hatch between 10 minutes and 30 minutes after creation
        return new TileEntityEndermiteEgg(rand.nextInt(24000) + 12000, 0);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        EndermiteOverhaulUtils.aggroEndermenOnAttacker(world, x, y, z, 32, player);
        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    /**
     * Destroy the tile entity, maybe spawn endermites, aggro endermen, spawn endermen if not present
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {
        if (!world.isRemote) {
            var te = (TileEntityEndermiteEgg) world.getTileEntity(x, y, z);
            var percentage = te.getAge() / te.getHatchTime();
            if (percentage > 0.50 && rand.nextInt(4) == 0) {
                var mite = new EntityEndermite(world);
                mite.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                world.spawnEntityInWorld(mite);
            }
        }
        super.breakBlock(world, x, y, z, blockBroken, meta);
    }

    /**
     * Fixes double drop
     */
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);
        // don't fire extra harvesting event, block already drops
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        harvesters.set(player);
        var fortune = EnchantmentHelper.getFortuneModifier(player);
        var drops = getDrops(world, x, y, z, meta, fortune);
        if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player)
            && ForgeEventFactory.fireBlockHarvesting(
                drops,
                world,
                this,
                x,
                y,
                z,
                meta,
                fortune,
                1.0F,
                EnchantmentHelper.getSilkTouchModifier(player),
                player) > 0.0F) {
            for (var stack : drops) {
                if (stack != null) {
                    dropBlockAsItem(world, x, y, z, stack);
                }
            }

        }

        harvesters.remove();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        var ret = new ArrayList<ItemStack>();
        var tileEntityEndermiteEgg = (TileEntityEndermiteEgg) world.getTileEntity(x, y, z);
        if (tileEntityEndermiteEgg != null) {
            ret.add(tileEntityEndermiteEgg.createItemStack(harvesters.get()));
        }
        return ret;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
        return MathHelper.getRandomIntegerInRange(rand, 10, 20);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {}

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        // set the values if required, otherwise use default data
        if (itemIn.stackTagCompound != null && !itemIn.stackTagCompound.hasNoTags()) {
            var hatchTime = itemIn.stackTagCompound.getInteger("hatchTime");
            var age = itemIn.stackTagCompound.getInteger("age");
            worldIn.setTileEntity(x, y, z, new TileEntityEndermiteEgg(hatchTime, age));
        }
    }

    /**
     * Destroy the block when something is thrown at it and attack the thrower
     */
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote) {
            if (entity instanceof EntityThrowable entityThrowable) {
                destroyBlockEffects(world, x, y, z);
                if (entityThrowable.getThrower() != null) {
                    EndermiteOverhaulUtils.aggroEndermenOnAttacker(world, x, y, z, 32, entityThrowable.getThrower());
                }
            } else if (entity instanceof EntityArrow entityArrow) {
                destroyBlockEffects(world, x, y, z);
                if (entityArrow.shootingEntity != null) {
                    EndermiteOverhaulUtils.aggroEndermenOnAttacker(world, x, y, z, 32, entityArrow.shootingEntity);
                }
            }
        }
    }

    /**
     * Plays the destroy sound <br>
     * Sets the block to air<br>
     */
    private void destroyBlockEffects(World world, int x, int y, int z) {
        EndermiteOverhaul.networkWrapper.sendToAllAround(
            new BlockBreakParticleMessage(x, y, z),
            new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));
        world.setBlockToAir(x, y, z);
        world.playSoundEffect(
            x,
            y,
            z,
            ModSounds.soundSlime.getBreakSound(),
            ModSounds.soundSlime.volume,
            ModSounds.soundSlime.getPitch());
    }
}
