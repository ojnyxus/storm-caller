package com.stormcaller.client.render;

import com.stormcaller.StormcallerMod;
import com.stormcaller.entity.custom.StormWispEntity;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class StormWispModel extends GeoModel<StormWispEntity> {

    private static final Identifier MODEL = Identifier.fromNamespaceAndPath(StormcallerMod.MOD_ID, "geo/storm_wisp.geo.json");
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(StormcallerMod.MOD_ID, "textures/entity/storm_wisp.png");
    private static final Identifier ANIMATIONS = Identifier.fromNamespaceAndPath(StormcallerMod.MOD_ID, "animations/storm_wisp.animation.json");

    @Override
    public Identifier getModelResource(StormWispEntity animatable) {
        return MODEL;
    }

    @Override
    public Identifier getTextureResource(StormWispEntity animatable) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationResource(StormWispEntity animatable) {
        return ANIMATIONS;
    }
}
