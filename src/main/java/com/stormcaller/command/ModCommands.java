package com.stormcaller.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.stormcaller.block.ModBlocks;
import com.stormcaller.entity.ModEntities;
import com.stormcaller.entity.custom.StormWispEntity;
import com.stormcaller.item.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Debug/dev commands for testing Stormcaller without waiting on RNG or real storms.
 * All of these require permission level 2 (operator). Root command: /stormcallerdebug
 */
public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stormcallerdebug")
                .requires(source -> source.hasPermission(2))

                // 1
                .then(Commands.literal("help").executes(ModCommands::help))
                // 2
                .then(Commands.literal("info").executes(ModCommands::info))
                // 3
                .then(Commands.literal("give_charge")
                        .executes(ctx -> giveCharge(ctx, 1))
                        .then(Commands.argument("count", IntegerArgumentType.integer(1, 64))
                                .executes(ctx -> giveCharge(ctx, IntegerArgumentType.getInteger(ctx, "count")))))
                // 4
                .then(Commands.literal("give_orb").executes(ModCommands::giveOrb))
                // 5
                .then(Commands.literal("give_altar").executes(ModCommands::giveAltar))
                // 6
                .then(Commands.literal("give_all").executes(ModCommands::giveAll))
                // 7
                .then(Commands.literal("spawn_wisp").executes(ModCommands::spawnWisp))
                // 8
                .then(Commands.literal("spawn_wisp_count")
                        .then(Commands.argument("count", IntegerArgumentType.integer(1, 50))
                                .executes(ctx -> spawnWispCount(ctx, IntegerArgumentType.getInteger(ctx, "count")))))
                // 9
                .then(Commands.literal("kill_wisps").executes(ModCommands::killWisps))
                // 10
                .then(Commands.literal("count_wisps").executes(ModCommands::countWisps))
                // 11
                .then(Commands.literal("strike_lightning").executes(ModCommands::strikeLightning))
                // 12
                .then(Commands.literal("strike_lightning_at")
                        .then(Commands.argument("x", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("y", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("z", DoubleArgumentType.doubleArg())
                                                .executes(ModCommands::strikeLightningAt)))))
                // 13
                .then(Commands.literal("force_storm")
                        .executes(ctx -> forceStorm(ctx, 12000))
                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 1000000))
                                .executes(ctx -> forceStorm(ctx, IntegerArgumentType.getInteger(ctx, "ticks")))))
                // 14
                .then(Commands.literal("clear_weather").executes(ModCommands::clearWeather))
                // 15
                .then(Commands.literal("weather_status").executes(ModCommands::weatherStatus))
                // 16
                .then(Commands.literal("simulate_backfire").executes(ModCommands::simulateBackfire))
                // 17
                .then(Commands.literal("simulate_brew").executes(ModCommands::simulateBrew))
                // 18
                .then(Commands.literal("reset_orb_cooldown").executes(ModCommands::resetOrbCooldown))
                // 19
                .then(Commands.literal("orb_cooldown_status").executes(ModCommands::orbCooldownStatus))
                // 20
                .then(Commands.literal("test_particles").executes(ModCommands::testParticles))
        );
    }

    private static ServerPlayer player(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return ctx.getSource().getPlayerOrException();
    }

    private static void feedback(CommandContext<CommandSourceStack> ctx, String message) {
        ctx.getSource().sendSuccess(() -> Component.literal(message), false);
    }

    // 1
    private static int help(CommandContext<CommandSourceStack> ctx) {
        feedback(ctx, "--- Stormcaller debug commands ---");
        String[] commands = {
                "help", "info", "give_charge [count]", "give_orb", "give_altar", "give_all",
                "spawn_wisp", "spawn_wisp_count <count>", "kill_wisps", "count_wisps",
                "strike_lightning", "strike_lightning_at <x> <y> <z>", "force_storm [ticks]",
                "clear_weather", "weather_status", "simulate_backfire", "simulate_brew",
                "reset_orb_cooldown", "orb_cooldown_status", "test_particles"
        };
        for (String command : commands) {
            feedback(ctx, "/stormcallerdebug " + command);
        }
        return commands.length;
    }

    // 2
    private static int info(CommandContext<CommandSourceStack> ctx) {
        feedback(ctx, "Stormcaller - Tame the Tempest (debug build)");
        feedback(ctx, "Mod id: stormcaller | Target: Minecraft 1.21.11");
        return 1;
    }

    // 3
    private static int giveCharge(CommandContext<CommandSourceStack> ctx, int count) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        p.addItem(new ItemStack(ModItems.LIGHTNING_CHARGE, count));
        feedback(ctx, "Gave " + count + " Lightning Charge(s).");
        return count;
    }

    // 4
    private static int giveOrb(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        player(ctx).addItem(new ItemStack(ModItems.STORMCALLER_ORB));
        feedback(ctx, "Gave 1 Stormcaller Orb.");
        return 1;
    }

    // 5
    private static int giveAltar(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        player(ctx).addItem(new ItemStack(ModBlocks.WEATHER_ALTAR.asItem()));
        feedback(ctx, "Gave 1 Weather Altar.");
        return 1;
    }

    // 6
    private static int giveAll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        p.addItem(new ItemStack(ModItems.LIGHTNING_CHARGE, 4));
        p.addItem(new ItemStack(ModItems.STORMCALLER_ORB));
        p.addItem(new ItemStack(ModBlocks.WEATHER_ALTAR.asItem()));
        p.addItem(new ItemStack(ModItems.STORM_WISP_SPAWN_EGG));
        feedback(ctx, "Gave one of every Stormcaller item.");
        return 1;
    }

    // 7
    private static int spawnWisp(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        ServerLevel level = p.serverLevel();
        StormWispEntity wisp = ModEntities.STORM_WISP.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        if (wisp != null) {
            wisp.setPos(p.getX(), p.getY(), p.getZ());
            level.addFreshEntity(wisp);
        }
        feedback(ctx, "Spawned 1 Storm Wisp.");
        return 1;
    }

    // 8
    private static int spawnWispCount(CommandContext<CommandSourceStack> ctx, int count) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        ServerLevel level = p.serverLevel();
        for (int i = 0; i < count; i++) {
            StormWispEntity wisp = ModEntities.STORM_WISP.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
            if (wisp != null) {
                double dx = (level.getRandom().nextDouble() - 0.5) * 6;
                double dz = (level.getRandom().nextDouble() - 0.5) * 6;
                wisp.setPos(p.getX() + dx, p.getY() + 1, p.getZ() + dz);
                level.addFreshEntity(wisp);
            }
        }
        feedback(ctx, "Spawned " + count + " Storm Wisps.");
        return count;
    }

    // 9
    private static int killWisps(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerLevel level = player(ctx).serverLevel();
        List<StormWispEntity> wisps = level.getEntitiesOfClass(StormWispEntity.class, new AABB(-3.0E7, -2048, -3.0E7, 3.0E7, 2048, 3.0E7));
        int count = wisps.size();
        wisps.forEach(StormWispEntity::discard);
        feedback(ctx, "Removed " + count + " Storm Wisp(s) from this dimension.");
        return count;
    }

    // 10
    private static int countWisps(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerLevel level = player(ctx).serverLevel();
        int count = level.getEntitiesOfClass(StormWispEntity.class, new AABB(-3.0E7, -2048, -3.0E7, 3.0E7, 2048, 3.0E7)).size();
        feedback(ctx, "There are " + count + " Storm Wisp(s) in this dimension.");
        return count;
    }

    // 11
    private static int strikeLightning(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        spawnBolt(p.serverLevel(), p.getX(), p.getY(), p.getZ());
        feedback(ctx, "Struck lightning at your position.");
        return 1;
    }

    // 12
    private static int strikeLightningAt(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        double x = DoubleArgumentType.getDouble(ctx, "x");
        double y = DoubleArgumentType.getDouble(ctx, "y");
        double z = DoubleArgumentType.getDouble(ctx, "z");
        spawnBolt(ctx.getSource().getLevel(), x, y, z);
        feedback(ctx, String.format("Struck lightning at %.1f, %.1f, %.1f.", x, y, z));
        return 1;
    }

    private static void spawnBolt(ServerLevel level, double x, double y, double z) {
        LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        bolt.setPos(x, y, z);
        level.addFreshEntity(bolt);
    }

    // 13
    private static int forceStorm(CommandContext<CommandSourceStack> ctx, int ticks) {
        ctx.getSource().getLevel().setWeatherParameters(0, ticks, true, true);
        feedback(ctx, "Forced a thunderstorm for " + ticks + " ticks.");
        return ticks;
    }

    // 14
    private static int clearWeather(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().getLevel().setWeatherParameters(6000, 0, false, false);
        feedback(ctx, "Cleared the weather.");
        return 1;
    }

    // 15
    private static int weatherStatus(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        feedback(ctx, "raining=" + level.isRaining() + " thundering=" + level.isThundering());
        return 1;
    }

    // 16
    private static int simulateBackfire(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        spawnBolt(p.serverLevel(), p.getX(), p.getY(), p.getZ());
        p.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
        p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        feedback(ctx, "Simulated a Stormcaller Orb Backfire on yourself.");
        return 1;
    }

    // 17
    private static int simulateBrew(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        p.serverLevel().setWeatherParameters(0, 12000, true, true);
        p.serverLevel().sendParticles(ParticleTypes.CLOUD, p.getX(), p.getY() + 1.5, p.getZ(), 30, 0.6, 0.4, 0.6, 0.02);
        feedback(ctx, "Simulated a safe Stormcaller Orb brew.");
        return 1;
    }

    // 18
    private static int resetOrbCooldown(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        ItemStack held = p.getMainHandItem();
        p.getCooldowns().removeCooldown(p.getCooldowns().getCooldownGroup(held));
        feedback(ctx, "Cleared cooldown for the item in your main hand.");
        return 1;
    }

    // 19
    private static int orbCooldownStatus(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        ItemStack held = p.getMainHandItem();
        boolean onCooldown = p.getCooldowns().isOnCooldown(held);
        float percent = p.getCooldowns().getCooldownPercent(held, 0f);
        feedback(ctx, "onCooldown=" + onCooldown + " remaining=" + Math.round(percent * 100) + "%");
        return 1;
    }

    // 20
    private static int testParticles(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer p = player(ctx);
        Vec3 pos = p.position();
        p.serverLevel().sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y + 1.0, pos.z, 60, 0.5, 0.8, 0.5, 0.04);
        feedback(ctx, "Spawned a burst of debug particles at your position.");
        return 1;
    }
}
