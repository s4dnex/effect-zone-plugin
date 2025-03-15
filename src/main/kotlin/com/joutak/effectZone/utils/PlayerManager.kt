package com.joutak.effectZone.utils

import com.joutak.effectZone.data.Zone
import org.bukkit.entity.Player

/**
 * Класс для хранения игроков и зон, в которых они находятся
 */
object PlayerManager {
    // "Мапа" игроков с зонами, в которых они находятся
    private val playersInZone = mutableMapOf<Player, MutableSet<Zone>>()

    /**
     * Добавление зоны соответствующему игроку
     */
    fun add(player: Player, zone: Zone) {
        if (playersInZone[player] == null)
            playersInZone[player] = mutableSetOf(zone)
        else
            playersInZone[player]!!.add(zone)
    }

    /**
     * Удаление зоны у данного игрока
     */
    fun removeZone(player: Player, zone: Zone) : Boolean {
        if (playersInZone[player] == null)
            return false
        else {
            val result = playersInZone[player]!!.remove(zone)

            if (playersInZone[player]!!.isEmpty())
                playersInZone.remove(player)

            return result
        }
    }

    /**
     * Множество всех игроков, которые находятся в какой-либо зоне
     */
    fun getPlayers(): Set<Player> {
        return playersInZone.keys
    }

    /**
     * Множество всех зон, в которых находится данный игрок
     */
    fun getZones(player: Player): Set<Zone> {
        if (playersInZone[player] == null)
            return setOf()
        return playersInZone[player]!!
    }
}