package com.stormcaller.block.custom;

import com.mojang.serialization.MapCodec;
import com.stormcaller.item.ModItems;
import com.stormcaller.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Ritual block. Right-click while holding a Lightning Charge to load it (consumed).
 * Right-click again while charged to safely trigger a thunderstorm - unlike the
 * Stormcaller Orb, the Weather Altar never Backfires.
 */
public class WeatherAltarBlock extends Block {

    public static final BooleanProperty CHARGED = BooleanProperty.create("charged");
    public static final MapCodec<WeatherAltarBlock> CODEC = simpleCodec(WeatherAltarBlock::new);

    public WeatherAltarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(CHARGED, false));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGED);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        boolean charged = state.getValue(CHARGED);
        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!charged) {
            if (heldStack.is(ModItems.LIGHTNING_CHARGE)) {
                heldStack.shrink(1);
                level.setBlockAndUpdate(pos, state.setValue(CHARGED, true));
                level.playSound(null, pos, ModSounds.ALTAR_CHARGE, SoundSource.BLOCKS, 1.0f, 1.0f);
                spawnSparkParticles((ServerLevel) level, pos, 20);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        // Altar is charged: safely trigger a storm, no Backfire chance.
        level.setBlockAndUpdate(pos, state.setValue(CHARGED, false));
        triggerSafeStorm((ServerLevel) level);
        level.playSound(null, pos, ModSounds.ALTAR_ACTIVATE, SoundSource.BLOCKS, 1.0f, 0.9f);
        spawnSparkParticles((ServerLevel) level, pos, 60);
        return InteractionResult.SUCCESS;
    }

    private void triggerSafeStorm(ServerLevel level) {
        int rainTicks = 12000 + level.getRandom().nextInt(12000);
        level.setWeatherParameters(0, rainTicks, true, true);
    }

    private void spawnSparkParticles(ServerLevel level, BlockPos pos, int count) {
        level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                count, 0.4, 0.6, 0.4, 0.03);
    }
}
