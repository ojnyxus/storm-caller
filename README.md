<div align="center">

# 🌩️ STORMCALLER

**Tame the Tempest. Master the Weather.**

A minimalist Vanilla+ Minecraft mod for weather manipulation and atmospheric power.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.1-blue.svg?style=for-the-badge&logo=minecraft)](https://minecraft.net)
[![Mod Loader](https://img.shields.io/badge/Loader-Fabric-orange.svg?style=for-the-badge)](https://fabricmc.net)
[![Java Version](https://img.shields.io/badge/Java-21-red.svg?style=for-the-badge&logo=openjdk)](https://openjdk.org)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

</div>

---

## ⚡ O modzie / Overview

**Stormcaller** to lekki mod typu Vanilla+ zaprojektowany dla Fabric 1.21.1, który wprowadza mechaniki kontrolowania pogody, ołtarze miedziane oraz magiczne artefakty pozwalające na przywoływanie burz i wyładowań atmosferycznych.

---

## 🛠️ Key Features / Kluczowe Funkcje

* **🔮 Stormcaller Orb** – Magiczny artefakt służący do natychmiastowego wywoływania burzy i kontrolowania błyskawic.
* **🏛️ Weather Altar** – Struktura zbudowana z miedzi i kamienia pozwalająca na bezpieczną manipulację pogodą.
* **⚡ Lightning Charge** – Unikalny drop z atmosferycznych mobów wykorzystywany w craftingu.
* **📦 Vanilla+ Aesthetic** – Wszystkie bloki i przedmioty idealnie pasują do domyślnego stylu Minecrafta (16x16 pixel art).

---

## 🔨 Crafting & Mechanics

### 1. Stormcaller Orb
| Przedmiot | Opis |
| :--- | :--- |
| **Użycie** | Kliknięcie Prawym Przyciskiem Myszy (RMB) wywołuje natychmiastową burzę. |
| **Cooldown** | 60 sekund |
| **Ryzyko** | 25% szansy na skutek uboczny (backfire) przy nieostrożnym użyciu bez ołtarza! |

### 2. Weather Altar
| Element | Opis |
| :--- | :--- |
| **Konstrukcja** | Miedziane bloki, piorunochron oraz kamienna podstawa. |
| **Funkcja** | Bezpieczna aktywacja Kuli bez ryzyka porażenia gracza. |

---

## 💻 Tech Stack & Requirements

* **Minecraft:** `1.21.1`
* **Mod Loader:** `Fabric`
* **Fabric API:** nedeed
* **Java:** `OpenJDK 21`
* **Mappings:** `Yarn 1.21.1`

---

## 🚀 Building from Source

Jeśli chcesz samodzielnie skompilować projekt z kodu źródłowego (np. w Termuxie lub VS Code):

```bash
# Sklonuj repozytorium
git clone [https://github.com/twoj-nick/stormcaller.git](https://github.com/twoj-nick/stormcaller.git)

# Wejdź do katalogu
cd stormcaller

# Nadaj uprawnienia (Linux / Termux)
chmod +x gradlew

# Skompiluj projekt
./gradlew build
