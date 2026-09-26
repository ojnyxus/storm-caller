package com.stormcaller;

import com.stormcaller.block.ModBlocks;
import com.stormcaller.command.ModCommands;
import com.stormcaller.entity.ModEntities;
import com.stormcaller.event.LightningDropHandler;
import com.stormcaller.item.ModItems;
import com.stormcaller.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stormcaller - Tame the Tempest
 * <p>
 * Adds the Lightning Charge (a rare drop from mobs slain directly by a lightning bolt),
 * the Stormcaller Orb (a risky item that can summon a storm - or backfire on the user),
 * the Weather Altar (a safe, ritual way to trigger a storm once charged), and the
 * Storm Wisp - a small elemental creature that appears during thunderstorms.
 */
public class StormcallerMod implements ModInitializer {

    public static final String MOD_ID = "stormcaller";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("[Stormcaller] Gathering the clouds...");

        ModSounds.initialize();
        ModItems.initialize();
        ModBlocks.initialize();
        ModEntities.initialize();
        ModEntities.registerAttributes();

        LightningDropHandler.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.LIGHTNING_CHARGE));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.STORMCALLER_ORB));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .register(entries -> entries.accept(ModBlocks.WEATHER_ALTAR.asItem()));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(entries -> entries.accept(ModItems.STORM_WISP_SPAWN_EGG));

        LOGGER.info("[Stormcaller] Ready. The sky is listening.");
    }
}
