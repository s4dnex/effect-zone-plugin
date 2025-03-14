package com.joutak.effectZone

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import java.io.File
import java.io.IOException
import com.joutak.effectZone.listeners.ZoneListener
import com.joutak.effectZone.commands.ZoneCommandExecutor
import com.joutak.effectZone.data.Zone
import com.joutak.effectZone.utils.ZoneManager
import org.bukkit.event.player.PlayerMoveEvent

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

    /**
     * Загрузка (ранее созданных) зон из файла
     */
    private fun loadZones() {
        val fx = File(dataFolder, "zones.yml")
        if (!fx.exists()) {
            return
        }

        zonesFile = YamlConfiguration.loadConfiguration(fx)
        val zoneList = zonesFile.getList("zones") as? List<Map<String, Any>> ?: return

        ZoneManager.clear()

        for (value in zoneList) {
            try {
                ZoneManager.add(
                    Zone.deserialize(value)
                )
            }
            catch (e: Exception) {
                logger.severe("Ошибка при загрузке зон: ${e.message}.")
                break
            }
        }
    }

    /**
     * Сохранение зон в файл
     */
    private fun saveZones() {
        val fx = File(dataFolder, "zones.yml")
        if (!fx.exists()) {
            saveResource("zones.yml", true)
        }

        zonesFile.set("zones", ZoneManager.getZones().values.map {
            value -> value.serialize()
        })

        try {
            zonesFile.save(fx)
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

        // Периодично проверяем игроков, если они находятся в какой-либо зоне
//        Bukkit.getScheduler().runTaskTimer(this, Runnable {
//            for (player in Bukkit.getOnlinePlayers()) {
//                ZoneListener.onPlayerMove(
//                    PlayerMoveEvent(player, player.location, player.location)
//                )
//            }
//        }, 0L, 100L)

        logger.info("Effect Zone plugin version ${pluginMeta.version} enabled!")
    }

    override fun onDisable() {
        // Plugin shutdown logic
        saveZones()
    }
}
