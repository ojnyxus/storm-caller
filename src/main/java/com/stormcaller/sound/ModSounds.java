package com.stormcaller.sound;

import com.stormcaller.StormcallerMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

/**
 * Custom sound events used by the mod. The actual .ogg files are NOT bundled - see
 * assets/stormcaller/sounds.json and the project README for free, CC0 sound
 * recommendations you can drop straight into assets/stormcaller/sounds/.
 */
public class ModSounds {

    public static final SoundEvent ORB_ACTIVATE = register("item.stormcaller_orb.activate");
    public static final SoundEvent ORB_BACKFIRE = register("item.stormcaller_orb.backfire");
    public static final SoundEvent ALTAR_CHARGE = register("block.weather_altar.charge");
    public static final SoundEvent ALTAR_ACTIVATE = register("block.weather_altar.activate");

    private static SoundEvent register(String path) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, StormcallerMod.id(path),
                SoundEvent.createVariableRangeEvent(StormcallerMod.id(path)));
    }

    public static void initialize() {
        StormcallerMod.LOGGER.info("[Stormcaller] Registering sound events");
    }
}
