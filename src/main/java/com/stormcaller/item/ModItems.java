package com.stormcaller.item;

import com.stormcaller.StormcallerMod;
import com.stormcaller.entity.ModEntities;
import com.stormcaller.item.custom.StormcallerOrbItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Function;

public class ModItems {

    /** Rare drop from a mob killed directly by a lightning bolt strike. */
    public static final Item LIGHTNING_CHARGE = register("lightning_charge",
            Item::new,
            new Item.Properties().stacksTo(16));

    /** Right-click to attempt to brew a storm - 25% chance to backfire on the user. */
    public static final Item STORMCALLER_ORB = register("stormcaller_orb",
            StormcallerOrbItem::new,
            new Item.Properties().stacksTo(1));

    public static final Item STORM_WISP_SPAWN_EGG = register("storm_wisp_spawn_egg",
            properties -> new SpawnEggItem(ModEntities.STORM_WISP, properties),
            new Item.Properties());

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, StormcallerMod.id(name));
        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
        StormcallerMod.LOGGER.info("[Stormcaller] Registering items");
    }
}
