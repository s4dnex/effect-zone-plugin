package com.joutak.effectZone.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import com.joutak.effectZone.data.Zone

/**
 * Класс для хранения всех зон и работы с ними
 */
object ZoneManager {
    private val zones = mutableMapOf<String, Zone>()

    fun add(zone: Zone) {
        if (zones.containsKey(zone.name))
            throw IllegalArgumentException("Зона с таким именем уже существует.")

        zones[zone.name] = zone
    }

    fun get(name: String): Zone {
        if (!zones.containsKey(name))
            throw IllegalArgumentException("Зоны с таким именем не существует.")

        return zones[name]!!
    }

    fun remove(name: String) {
        if (!zones.containsKey(name))
            throw IllegalArgumentException("Зоны с таким именем не существует.")

        zones.remove(name)
        EffectsManager.removeZone(zones.getValue(name))
        PlayerManager.removeZone(zones.getValue(name))
    }

    fun getZones(): Map<String, Zone> {
        return zones
    }

    fun clear() {
        zones.clear()
    }

    /**
     * Проверка на нахождение внутри зоны игроком
     */
    fun isInside(zone: Zone, playerLoc: Location): Boolean {
        val world = Bukkit.getWorld(zone.worldName) ?: throw IllegalArgumentException("Мир $zone.worldName не найден.")

        if (playerLoc.world?.name != world.name) return false
        return playerLoc.x in zone.x1..zone.x2 &&
                playerLoc.y in zone.y1..zone.y2 &&
                playerLoc.z in zone.z1..zone.z2
    }

    /**
     * Создание партиклов на "рёбрах" зоны
     */
    fun spawnParticles(zone: Zone) {
        val world = Bukkit.getWorld(zone.worldName) ?: throw IllegalArgumentException("Мир $zone.worldName не найден.")
        val particle = Particle.CLOUD

        for (x in zone.x1.toInt()..zone.x2.toInt()) {
            world.spawnParticle(particle, x.toDouble(), zone.y1, zone.z1, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, x.toDouble(), zone.y1, zone.z2, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, x.toDouble(), zone.y2, zone.z1, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, x.toDouble(), zone.y2, zone.z2, 1, 0.0, 0.0, 0.0, 0.0)
        }

        for (y in zone.y1.toInt()..zone.y2.toInt()) {
            world.spawnParticle(particle, zone.x1, y.toDouble(), zone.z1, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x1, y.toDouble(), zone.z2, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x2, y.toDouble(), zone.z1, 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x2, y.toDouble(), zone.z2, 1, 0.0, 0.0, 0.0, 0.0)
        }

        for (z in zone.z1.toInt()..zone.z2.toInt()) {
            world.spawnParticle(particle, zone.x1, zone.y1, z.toDouble(), 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x1, zone.y2, z.toDouble(), 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x2, zone.y1, z.toDouble(), 1, 0.0, 0.0, 0.0, 0.0)
            world.spawnParticle(particle, zone.x2, zone.y2, z.toDouble(), 1, 0.0, 0.0, 0.0, 0.0)
        }
    }
}