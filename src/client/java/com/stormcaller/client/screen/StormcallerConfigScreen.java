package com.stormcaller.client.screen;

import com.stormcaller.StormcallerMod;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

/**
 * A simple "about" screen for Stormcaller, opened from Mod Menu's config/gear button.
 * Shows the mod name, version, id and a short description - nothing configurable yet,
 * but it's a natural place to add real options (e.g. Backfire chance, cooldown) later.
 */
public class StormcallerConfigScreen extends Screen {

    private final Screen parent;
    private String version = "unknown";

    public StormcallerConfigScreen(Screen parent) {
        super(Component.literal("Stormcaller"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(StormcallerMod.MOD_ID);
        if (container.isPresent()) {
            ModMetadata meta = container.get().getMetadata();
            this.version = meta.getVersion().getFriendlyString();
        }

        Button doneButton = Button.builder(Component.literal("Done"), button -> {
                    if (this.minecraft != null) {
                        this.minecraft.setScreen(parent);
                    }
                })
                .bounds(this.width / 2 - 100, this.height - 32, 200, 20)
                .build();
        this.addRenderableWidget(doneButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int centerX = this.width / 2;
        int textY = 40;

        graphics.drawCenteredString(this.font, "Stormcaller - Tame the Tempest", centerX, textY, 0xFFFFFF);
        textY += 14;
        graphics.drawCenteredString(this.font, "Version " + this.version, centerX, textY, 0xAAAAAA);
        textY += 20;

        List<String> description = List.of(
                "Harness lightning strikes, brew storms",
                "with the Stormcaller Orb, and build a",
                "Weather Altar to bend the sky safely -",
                "or risk a shocking Backfire."
        );
        for (String line : description) {
            graphics.drawCenteredString(this.font, line, centerX, textY, 0xCCCCCC);
            textY += 12;
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
