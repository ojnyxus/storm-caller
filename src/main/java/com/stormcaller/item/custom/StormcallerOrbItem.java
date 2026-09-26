package com.stormcaller.item.custom;

import com.stormcaller.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StormcallerOrbItem extends Item {

    /** 60 second cooldown (20 ticks per second). */
    private static final int COOLDOWN_TICKS = 20 * 60;
    private static final float BACKFIRE_CHANCE = 0.25f;
    private final RandomSource random = RandomSource.create();

    public StormcallerOrbItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        // Only run the logic on the server, to avoid a client/server desync.
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (user.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        user.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);

        if (random.nextFloat() < BACKFIRE_CHANCE) {
            backfire(serverLevel, user);
        } else {
            brewStorm(serverLevel, user);
        }

        level.playSound(null, user.blockPosition(), ModSounds.ORB_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
        return InteractionResult.CONSUME;
    }

    private void brewStorm(ServerLevel level, Player user) {
        int rainTicks = 6000 + level.getRandom().nextInt(12000);
        level.setWeatherParameters(0, rainTicks, true, true);
        level.sendParticles(ParticleTypes.CLOUD, user.getX(), user.getY() + 1.5, user.getZ(), 30, 0.6, 0.4, 0.6, 0.02);
    }

    private void backfire(ServerLevel level, Player user) {
        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        bolt.setPos(user.getX(), user.getY(), user.getZ());
        level.addFreshEntity(bolt);

        level.playSound(null, user.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f);
        level.playSound(null, user.blockPosition(), ModSounds.ORB_BACKFIRE, SoundSource.PLAYERS, 1.0f, 1.0f);

        user.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
        user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
    }
}
