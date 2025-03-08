# **Vanilla Spawners Expanded**

**Enhance your spawners with Vanilla Spawners Expanded!**

Looking to customize and optimize your spawners in Minecraft? Discover **Vanilla Spawners Expanded**, a Fabric mod that offers advanced configuration options for your spawners.

## **Key Features:**

- **Spawner Calibration:** Use the **Spawner Calibrator** on a vanilla spawner to prepare it for customization. This step is essential to unlock the full potential of your spawners.

- **Soul Capture:** Capture the souls of mobs using the **Cursed Bottle**. Once captured, these souls can be placed into calibrated spawners to define the creatures they generate.

- **Available Upgrades:**
  - **Speed Upgrade:** Reduces the time between spawn cycles for faster production.
  - **Range Upgrade:** Increases the operational range of your spawners.
  - **Crowd Upgrade:** Allows spawners to function even when multiple mobs are already present.
  - **Redstone Upgrade:** Control your spawners with redstone for enhanced automation.

- **Configurable:** Adjust the mod's settings with a detailed configuration file to tailor the experience to your preferences.

## **Why Choose Vanilla Spawners Expanded?**

- **Customization:** Create varied environments with personalized spawners.
- **Efficiency:** Optimize your mob farms for increased gains with less effort.
- **Fabric Compatibility:** Designed exclusively for Fabric, this mod integrates well with other popular mods and supports mobs added by them, allowing you to customize your spawners with a wide variety of creatures.

## **Configuration Options:**

The mod comes with a JSON configuration file that allows you to customize various settings:

- **`mob_blacklist`**: List of mobs that cannot be spawned by calibrated spawners.
- **`mob_whitelist`**: List of mobs that can be spawned by calibrated spawners. If empty, all mobs are allowed except those in the blacklist.
- **`calibratedSpawnerStayLitOnBreak`**: If true, calibrated spawners will stay lit when broken.
- **`calibratedSpawnerKeepMobOnBreak`**: If true, calibrated spawners will retain their mob type when broken.
- **`calibratedSpawnerKeepUpgradesOnBreak`**: If true, calibrated spawners will keep their upgrades when broken.
- **`crowdUpgradeValue`**: Maximum number of nearby mobs allowed for the spawner to function with the Crowd Upgrade.
- **`rangeUpgradeValue`**: Range within which the spawner will function with the Range Upgrade.
- **`speedUpgradeMinValue`** and **`speedUpgradeMaxValue`**: Minimum and maximum delay (in ticks) between spawn cycles with the Speed Upgrade.
