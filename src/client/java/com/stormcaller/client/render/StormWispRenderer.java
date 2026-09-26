package com.stormcaller.client.render;

import com.stormcaller.entity.custom.StormWispEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StormWispRenderer extends GeoEntityRenderer<StormWispEntity> {

    public StormWispRenderer(EntityRendererProvider.Context context) {
        super(context, new StormWispModel());
        this.shadowRadius = 0.3f;
    }
}
