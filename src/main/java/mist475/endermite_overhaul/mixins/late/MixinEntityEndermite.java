package mist475.endermite_overhaul.mixins.late;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import ganymedes01.etfuturum.entities.EntityEndermite;
import mist475.endermite_overhaul.EndermiteOverhaulUtils;
import mist475.endermite_overhaul.ModBlocks;

/**
 * Extend the efr Endermite class to get our desired behaviour
 */
@Mixin(value = EntityEndermite.class, remap = false)
public abstract class MixinEntityEndermite extends EntityMob {

    public MixinEntityEndermite(World p_i1738_1_) {
        super(p_i1738_1_);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (worldObj.isRemote) {
            for (int i = 0; i < 2; i++) worldObj.spawnParticle(
                "portal",
                posX + (rand.nextDouble() - 0.5D) * width,
                posY + rand.nextDouble() * height,
                posZ + (rand.nextDouble() - 0.5D) * width,
                (rand.nextDouble() - 0.5D) * 2.0D,
                -rand.nextDouble(),
                (rand.nextDouble() - 0.5D) * 2.0D);
        }
    }

    /**
     * @author Mist475
     * @reason Unlike vanilla/ efr we don't want endermen to attack endermites
     */
    @Overwrite
    public void aggroEndermen(int range) {}

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        // aggro endermen on target
        endermiteOverhaul$aggroEndermenOnAttacker(32, source);
        return super.attackEntityFrom(source, amount);
    }

    /**
     * @author Mist475
     * @reason Drop eggs (the older the mite, the more likely it drops an egg)
     */
    @Overwrite
    protected Item getDropItem() {
        if (this.rand.nextDouble() <= MathHelper.clamp_double((double) ticksExisted / 12000, 0, 1)) {
            return Item.getItemFromBlock(ModBlocks.ENDERMITE_EGG.get());
        }
        return null;
    }

    @Unique
    private void endermiteOverhaul$aggroEndermenOnAttacker(int range, DamageSource source) {
        EndermiteOverhaulUtils.aggroEndermenOnAttacker(worldObj, posX, posY, posZ, range, source.getEntity());
    }
}
