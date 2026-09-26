# Stormcaller — Tame the Tempest

Fabric mod for Minecraft **1.21.11** (Java 21, Fabric Loader, Fabric API, **official Mojang mappings**, split client/main source sets).

## Why Mojang mappings, and why this took a rewrite

1.21.11 is a very recent release (late 2025/2026). Fabric's own official example-mod template
and documentation for 1.21.11 use **Mojang's official mappings**, not Yarn — Yarn mappings will
stop being published after 1.21.11, and Fabric's own tooling has already switched over. So this
build uses Mojmap class/method names (`Item.Properties`, `BuiltInRegistries`, `Identifier`, etc.)
instead of the older Yarn names (`Item.Settings`, `Registry`, etc.) from the first draft of this
project. Every registration pattern, the Item/Block interaction methods, and the Storm Wisp entity
setup were checked against Fabric's version-pinned documentation for 1.21.11
(https://docs.fabricmc.net/1.21.11/) rather than guessed from memory, specifically to avoid the
compile errors from the previous build.

## What's inside

| Feature | Details |
|---|---|
| **Lightning Charge** (item) | Drops from a living mob **only** when its killing blow is a direct lightning bolt strike (`ServerLivingEntityEvents.AFTER_DEATH`, checks `damageSource.getDirectEntity() instanceof LightningBolt`). |
| **Stormcaller Orb** (item) | Right-click to attempt to brew a storm. 60s cooldown. **25% Backfire chance**: strikes the user with lightning, plays a glass-break sound, applies Blindness + Slowness. |
| **Weather Altar** (block) | Right-click while holding a Lightning Charge to load it. Right-click again to safely trigger a storm — **no Backfire risk**, ever. |
| **Storm Wisp** (entity, new) | A small GeckoLib-animated elemental that wanders around, with idle-float and fly animations. Has a spawn egg. Killed by lightning, it drops a Lightning Charge like anything else. |
| **Storm Shrine** (structure) | Generates in mountain/peak biomes. Contains a Weather Altar centerpiece and a loot chest. |

## Building

1. Unzip the project.
2. Make sure you have **JDK 21** installed and on your `PATH` (`java -version`).
3. From the project root:
   ```
   gradlew.bat build
   ```
   (on Termux/Linux/macOS use `./gradlew build`). First run downloads Minecraft, Mojang's
   mappings, Fabric API and GeckoLib — needs an internet connection.
4. The built mod jar will be in `build/libs/`.

The Gradle wrapper is pulled straight from Fabric's own official `fabric-example-mod` repo,
**1.21.11 branch** — same Loom version (`net.fabricmc.fabric-loom-remap`, `1.17-SNAPSHOT`) and
same Gradle version (9.5.1) that Fabric itself tests against for this Minecraft version. Mixing
an older Loom plugin with this newer Gradle version is what caused the `ProblemReporter` crash
in the very first build — this project no longer does that.

## GeckoLib & the Storm Wisp

GeckoLib is pulled from Modrinth's Maven, pinned to the exact build tagged
**"5.4.5 for Fabric 1.21.11"** (`maven.modrinth:8BmcQJ2H:G1BvHQDL`), so the version can't drift
out from under you. It's a normal `modImplementation` — required at runtime, bundled like Fabric
API.

The Storm Wisp's model (`assets/stormcaller/geo/storm_wisp.geo.json`) and animations
(`assets/stormcaller/animations/storm_wisp.animation.json`) are hand-built, simple Blockbench-format
files (a floating core cube + two small "shard" cubes) rather than a downloaded third-party model —
I can't fetch or redistribute someone else's copyrighted model file for you, but you can open the
`.geo.json` directly in [Blockbench](https://www.blockbench.net/) (Mojang/Java format) to reshape
it, add more bones, or paint a nicer texture over the placeholder one at
`assets/stormcaller/textures/entity/storm_wisp.png`.

## A note on reliability

This is bleeding-edge: 1.21.11 came out very recently, and its exact API is outside my normal
training knowledge, so every registration pattern, block/item interaction method, and the entity
setup here were pulled from Fabric's own version-pinned docs rather than memory. I'm fairly
confident in the core (items, blocks, sounds, the lightning-drop event) since those match Fabric's
official reference code closely. The **GeckoLib import paths** in `StormWispEntity.java`,
`StormWispModel.java`, and `StormWispRenderer.java` are the one part I could not verify against
an official up-to-date source in this session — if `gradlew.bat build` fails specifically on
`import software.bernie.geckolib...` lines, check GeckoLib's wiki
(https://github.com/bernie-g/geckolib/wiki) or your IDE's autocomplete (after the first successful
sync) for the current package names and adjust those import lines; everything else in the class
should still be correct.

## Debug commands

`/stormcallerdebug` (requires op / permission level 2) adds 20 subcommands for testing the mod's
mechanics without waiting on RNG or a real storm — see `command/ModCommands.java`. Run
`/stormcallerdebug help` in-game for the full list. Highlights: `give_all`, `spawn_wisp_count <n>`,
`strike_lightning_at <x> <y> <z>`, `force_storm [ticks]`, `simulate_backfire`, `simulate_brew`,
`reset_orb_cooldown`, `weather_status`.

## Mod Menu integration

If [Mod Menu](https://modrinth.com/mod/modmenu) is installed, Stormcaller's entry on the Mods list
gets a config/gear button (like Create's) that opens a screen showing the mod name, version, and a
short description. Mod Menu is **not** required — it's `modCompileOnly`/`modLocalRuntime` only.

`gradle.properties` pins `modmenu_version=17.0.0` (ModMenu's own docs list `>=17.0.0` as the
requirement for 1.21.11). If Gradle can't resolve that exact build, check
https://modrinth.com/mod/modmenu/versions for the current 1.21.11 release and swap the number in.

## Worldgen structure

`data/stormcaller/structure/storm_shrine.nbt` is a real, generated NBT structure (7×6×7): a
mossy-cobblestone platform, four pillars topped with lightning rods, a Weather Altar centerpiece
flanked by budding amethyst, and a loot chest. Wired up through
`worldgen/structure_set/storm_shrine.json`, `worldgen/structure/storm_shrine.json`, and
`worldgen/template_pool/storm_shrine.json`, gated on the `#stormcaller:has_structure/storm_shrine`
biome tag (mountain/peak biomes).

## Sounds — CC0 files you need to add

`assets/stormcaller/sounds.json` declares 4 custom sound events, but **no audio files are
bundled**. Grab CC0 (public domain) `.ogg` files and drop them in:

| Sound event | Suggested search (Freesound, CC0 filter) | Save as |
|---|---|---|
| Orb activates | https://freesound.org/search/?q=magic+charge&f=license%3A%22Creative+Commons+0%22 | `assets/stormcaller/sounds/item/stormcaller_orb_activate.ogg` |
| Orb backfires | https://freesound.org/search/?q=glass+break+zap&f=license%3A%22Creative+Commons+0%22 | `assets/stormcaller/sounds/item/stormcaller_orb_backfire.ogg` |
| Altar charges | https://freesound.org/search/?q=stone+rune+hum&f=license%3A%22Creative+Commons+0%22 | `assets/stormcaller/sounds/block/weather_altar_charge.ogg` |
| Altar activates | https://freesound.org/search/?q=thunder+ritual&f=license%3A%22Creative+Commons+0%22 | `assets/stormcaller/sounds/block/weather_altar_activate.ogg` |

## Mod icon

`assets/stormcaller/icon.png` (128×128) was generated from the logo image you attached.
