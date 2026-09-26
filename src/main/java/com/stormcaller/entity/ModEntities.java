package com.stormcaller.entity;

import com.stormcaller.StormcallerMod;
import com.stormcaller.entity.custom.StormWispEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    /** A small elemental creature that appears during thunderstorms. */
    public static final EntityType<StormWispEntity> STORM_WISP = register(
            "storm_wisp",
            EntityType.Builder.<StormWispEntity>of(StormWispEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.5f)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, StormcallerMod.id(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void initialize() {
        StormcallerMod.LOGGER.info("[Stormcaller] Registering entity types");
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(STORM_WISP, StormWispEntity.createAttributes());
    }
}
