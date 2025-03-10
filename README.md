https://modrinth.com/mod/vanilla-spawners-expanded

# **Vanilla Spawners Expanded**

**Enhance your spawners with Vanilla Spawners Expanded!**

Looking to customize and optimize your spawners in Minecraft? Discover **Vanilla Spawners Expanded**, a Fabric mod that offers advanced configuration options for your spawners.

![Calibrated Spawner](https://cdn.modrinth.com/data/cached_images/5b477b1eafb7dfbb6123c578371489bd28b5d5f7.png)

## **Features:**

**Spawner Calibration:** Use the **Spawner Calibrator** on a vanilla spawner to prepare it for customization. This step is essential to unlock the full potential of your spawners. A calibrated spawner can be mined and collected even without silk touch.

**Soul Capture:** Capture the souls of mobs using the **Cursed Bottle**. Once captured, these souls can be placed into calibrated spawners to define the creatures they generate.

**Available Upgrades:**\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-**Speed Upgrade:** Reduces the time between spawn cycles for faster production.\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-**Range Upgrade:** Increases the operational range of your spawners.\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-**Crowd Upgrade:** Allows spawners to function even when multiple mobs are already present.\
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-**Redstone Upgrade:** Control your spawners with redstone.

**Configurable:** Adjust the mod's settings with a detailed configuration file to tailor the experience to your preferences.

**Fabric Compatibility:** Designed exclusively for Fabric, this mod integrates well with other popular mods and supports mobs added by them, allowing you to customize your spawners with a wide variety of creatures.

**Jade Implementation:** The **Calibrated Spawner** block is recognized by jade and its information will be displayed in real time.

## **Configuration Options:**

The mod comes with a JSON configuration file that allows you to customize various settings (located in .minecraft/config):

- **`mob_blacklist`**: List of mobs that cannot be spawned by calibrated spawners. Ignored if the whitelist is not empty.
- **`mob_whitelist`**: List of mobs that can be spawned by calibrated spawners. If empty, all mobs are allowed except those in the blacklist.
- **`calibratedSpawnerStayLitOnBreak`**: If true, calibrated spawners will stay lit when broken.
- **`calibratedSpawnerKeepMobOnBreak`**: If true, calibrated spawners will retain their mob type when broken.
- **`calibratedSpawnerKeepUpgradesOnBreak`**: If true, calibrated spawners will keep their upgrades when broken.
- **`cursedBottleIsReusable`**: If true, cursed bottles will be reusable.
- **`crowdUpgradeValue`**: Maximum number of nearby mobs allowed for the spawner to function with the Crowd Upgrade.
- **`rangeUpgradeValue`**: Range within which the spawner will function with the Range Upgrade.
- **`speedUpgradeMinValue`**: Minimum delay (in ticks) between spawn cycles with the Speed Upgrade.
- **`speedUpgradeMaxValue`**: Maximum delay (in ticks) between spawn cycles with the Speed Upgrade.
- **`crowdDefaultValue`**: Maximum number of nearby mobs allowed for the spawner to function by default (without the crowd upgrade).
- **`rangeDefaultValue`**: Range within which the spawner will function by default (without the range upgrade).
- **`speedDefaultMinValue`**: Minimum delay (in ticks) between spawn cycles by default (without the speed upgrade).
- **`speedDefaultMaxValue`**: Maximum delay (in ticks) between spawn cycles by default (without the speed upgrade).

These settings can be changed at any time and the behavior of the calibrated spawners will change without the need to replace them.
