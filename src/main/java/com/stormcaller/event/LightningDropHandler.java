package com.stormcaller.event;

import com.stormcaller.item.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Lightning Charge only drops when a living entity's killing blow is a direct
 * lightning bolt strike - being caught in a fire the bolt started, or dying
 * shortly after being struck, does not count.
 */
public class LightningDropHandler {

    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(LightningDropHandler::onLivingEntityDeath);
    }

    private static void onLivingEntityDeath(LivingEntity entity, DamageSource damageSource) {
        Entity directCause = damageSource.getDirectEntity();

        if (!(directCause instanceof LightningBolt)) {
            return;
        }

        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack drop = new ItemStack(ModItems.LIGHTNING_CHARGE);
        ItemEntity itemEntity = new ItemEntity(serverLevel, entity.getX(), entity.getY(), entity.getZ(), drop);
        itemEntity.setDefaultPickUpDelay();
        serverLevel.addFreshEntity(itemEntity);
    }
}
