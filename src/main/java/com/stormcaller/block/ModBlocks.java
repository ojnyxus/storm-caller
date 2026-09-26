package com.stormcaller.block;

import com.stormcaller.StormcallerMod;
import com.stormcaller.block.custom.WeatherAltarBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {

    /** Load it with a Lightning Charge, then activate it: always safe, no Backfire risk. */
    public static final Block WEATHER_ALTAR = register(
            "weather_altar",
            WeatherAltarBlock::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.DEEPSLATE_BRICKS)
                    .strength(3.5f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .noOcclusion(),
            true
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);
            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, StormcallerMod.id(name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, StormcallerMod.id(name));
    }

    public static void initialize() {
        StormcallerMod.LOGGER.info("[Stormcaller] Registering blocks");
    }
}
