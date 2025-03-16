package mist475.endermite_overhaul.mixins.early;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.biome.BiomeGenBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Disable vanilla enderman spawning to force the player to find endermite eggs (if this mixin is enabled)
 */
@Mixin(BiomeGenBase.class)
public class MixinBiomeGenBase {

    @Shadow
    protected List<net.minecraft.world.biome.BiomeGenBase.SpawnListEntry> spawnableMonsterList;

    @Inject(method = "<init>(IZ)V", at = @At(value = "TAIL"))
    private void RemoveEndermanFromSpawnList(int p_i1971_1_, boolean register, CallbackInfo ci) {
        spawnableMonsterList = spawnableMonsterList.stream()
            .filter(entry -> entry.entityClass != EntityEnderman.class)
            .collect(Collectors.toList());
    }
}
