package mist475.endermite_overhaul;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import ganymedes01.etfuturum.entities.EntityEndermite;

public class TileEntityEndermiteEgg extends TileEntity {

    // age at which the egg hatches
    private int hatchTime;
    // age of the egg
    private int age;

    @SuppressWarnings("unused")
    public TileEntityEndermiteEgg() {

    }

    public TileEntityEndermiteEgg(int hatchTime, int age) {
        this.hatchTime = hatchTime;
        this.age = age;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        hatchTime = compound.getInteger("hatchTime");
        age = compound.getInteger("age");
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("hatchTime", hatchTime);
        compound.setInteger("age", age);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHatchTime() {
        return hatchTime;
    }

    public void setHatchTime(int hatchTime) {
        this.hatchTime = hatchTime;
    }

    @Override
    public void updateEntity() {
        int radius = 32;
        if (!worldObj.isRemote) {
            var hatchTime = getHatchTime();
            if (age >= hatchTime) {
                worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
                var amountOfEndermites = worldObj.rand.nextInt(4);
                for (int i = 0; i < amountOfEndermites; i++) {
                    var mite = new EntityEndermite(worldObj);
                    mite.setLocationAndAngles(
                        xCoord + 0.5 + ((double) (worldObj.rand.nextInt() - 50) / 100),
                        yCoord,
                        zCoord + 0.5 + ((double) (worldObj.rand.nextInt() - 50) / 100),
                        0.0F,
                        0.0F);
                    worldObj.spawnEntityInWorld(mite);
                }
            } else {
                setAge(age + 1);
                markDirty();

                // rarely spawn endermen close to the egg if none are within range
                if (worldObj.rand.nextInt(8000) == 0) {
                    var enderman = worldObj.getEntitiesWithinAABB(
                        EntityEnderman.class,
                        AxisAlignedBB.getBoundingBox(
                            xCoord - radius,
                            yCoord - 4,
                            zCoord - radius,
                            xCoord + radius,
                            yCoord + 4,
                            zCoord + radius));
                    if (enderman.size() < 4) {
                        EndermiteOverhaulUtils.spawnEndermenCloseToEgg(worldObj, xCoord, yCoord, zCoord);
                    }
                }
            }
        }
    }

    private ItemStack writeToStack(ItemStack stack) {
        var compound = new NBTTagCompound();
        writeToNBT(compound);
        stack.setTagCompound(compound);
        return stack;
    }

    public ItemStack createItemStack(EntityPlayer player) {
        if (player != null && worldObj.getGameRules()
            .getGameRuleBooleanValue("doTileDrops")) {
            return writeToStack(new ItemStack(ModBlocks.ENDERMITE_EGG.get(), 1, 0));
        }
        return null;
    }
}
