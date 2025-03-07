package com.joutak.effectZone

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import org.bukkit.potion.PotionEffectType
import org.bukkit.command.CommandSender
import org.bukkit.command.Command
import org.bukkit.World
import java.io.File
import java.io.IOException

import com.joutak.effectZone.listeners.ZoneListener
import com.joutak.effectZone.commands.ZoneCommandExecutor

class EffectZonePlugin : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: EffectZonePlugin
    }

    private var customConfig = YamlConfiguration()
    private var zonesFile = YamlConfiguration() 

    private fun loadConfig() {
        val fx = File(dataFolder, "config.yml")
        if (!fx.exists()) {
            saveResource("config.yml", true)
        }
    }

    private fun loadZones() {
        val fx = File(dataFolder, "zones.yml")
        if (!fx.exists()) {
            return
        }

        zonesFile = YamlConfiguration.loadConfiguration(fx)
        val zoneList = zonesFile.getList("zones") as? List<Map<String, Any>> ?: return

        Zone.clear()

        for (value in zoneList) {
            try {
                val name = value["name"] as String
                val effect = PotionEffectType.getByName(value["effect"] as String) ?: throw IllegalArgumentException("Неизвестный эффект ${value["effect"]}.")
                val world = value["world"] as String
                val x1 = value["x1"] as Double
                val y1 = value["y1"] as Double
                val z1 = value["z1"] as Double
                val x2 = value["x2"] as Double
                val y2 = value["y2"] as Double
                val z2 = value["z2"] as Double
    
                Zone.add(
                    Zone(name, effect, world, x1, y1, z1, x2, y2, z2)
                )
            }
            catch (e: Exception) {
                logger.severe("Ошибка при загрузке зон: ${e.message}.")
            }
        }
    }

    private fun saveZones() {
        val fx = File(dataFolder, "zones.yml")
        if (!fx.exists()) {
            saveResource("zones.yml", true)
        }

        zonesFile.set("zones", Zone.getZones().map { 
            (key, value) -> mapOf(
                "name" to value.getName(),
                "effect" to value.getEffect().name,
                "world" to value.getWorldName(),
                "x1" to value.getCoords()["x1"],
                "y1" to value.getCoords()["y1"],
                "z1" to value.getCoords()["z1"],
                "x2" to value.getCoords()["x2"],
                "y2" to value.getCoords()["y2"],
                "z2" to value.getCoords()["z2"]
            )
        })

        try {
            zonesFile.save(fx)
            // zonesFile.(Zone.getZones())
        } catch (e: IOException) {
            logger.severe("Ошибка при сохранении зон: ${e.message}")
        }
    }

    override fun onEnable() {
        // Plugin startup logic
        instance = this

        loadConfig()

        // Register commands and events
        Bukkit.getPluginManager().registerEvents(ZoneListener, this)
        getCommand("zone")?.setExecutor(ZoneCommandExecutor)
        loadZones()

        logger.info("Effect Zone plugin version ${pluginMeta.version} enabled!")

    }

    override fun onDisable() {
        // Plugin shutdown logic
        saveZones()
    }
}
