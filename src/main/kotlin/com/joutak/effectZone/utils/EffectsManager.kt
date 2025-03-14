package com.joutak.effectZone.utils

import com.joutak.effectZone.EffectZonePlugin
import com.joutak.effectZone.data.Zone
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.scheduler.BukkitTask

/**
 * Класс для управления эффектами и партиклами зоны
 */
object EffectsManager {
    // "Мапа" активных зон с соответствующим им тасками, которые спавнят партиклы в них
    private val activeZones = mutableMapOf<Zone, BukkitTask>()

    /**
     * Удаление зоны и отмена таски со спавном партиклов, если такая была
     */
    fun removeZone(zone: Zone) {
        if (activeZones[zone] != null)
            Bukkit.getScheduler().cancelTask(activeZones[zone]!!.taskId)

        activeZones.remove(zone)
    }

    /**
     * Наложение эффектов на игрока в соответствии с зонами, в которых он сейчас находится
     */
    fun applyEffects(player: Player) {
        for (zone in PlayerManager.getZones(player)) {
            player.addPotionEffect(
                PotionEffect(zone.effect, -1, 1, false, true, true)
            )
        }
    }

    /**
     * Обновление партиклов во всех зонах
     */
    fun updateParticles() {
        // Проходим через каждую зону
        for (zone in ZoneManager.getZones().values) {
            // Если в зоне есть игроки и еще нет таски с перидочным созданием партиклов, то собственно создаем такую таску
            if (zone.getPlayersCount() > 0) {
                if (activeZones[zone] == null) {
                    activeZones[zone] = Bukkit.getScheduler().runTaskTimer(EffectZonePlugin.instance, Runnable {
                        ZoneManager.spawnParticles(zone)
                    }, 0, 20L)
                }
            }
            // Если в зоне нет игроков, то отменяем таску, если такая присутствует, и удаляем из списка "активных" зон
            else {
                if (activeZones[zone] != null) {
                    Bukkit.getScheduler().cancelTask(activeZones[zone]!!.taskId)
                    activeZones.remove(zone)
                }
            }
        }
    }

    /**
     * Удаление эффекта зоны с игрока
     */
    fun removeEffect(player: Player, zone: Zone) {
        player.removePotionEffect(
            zone.effect
        )
    }
}