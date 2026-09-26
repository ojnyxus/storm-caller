package com.stormcaller.client;

import com.stormcaller.client.render.StormWispRenderer;
import com.stormcaller.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class StormcallerModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(ModEntities.STORM_WISP, StormWispRenderer::new);
    }
}
