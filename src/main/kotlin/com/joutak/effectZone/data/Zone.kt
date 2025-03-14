package com.joutak.effectZone.data

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

/**
 * Класс для хранения данных о зоне
 */
data class Zone (
    val name : String,
    val effect : PotionEffectType,
    val worldName: String,
    val x1: Double, val y1: Double, val z1: Double,
    val x2: Double, val y2: Double, val z2: Double
) {
    // Множество всех игроков в данной зоне
    private val players = mutableSetOf<Player>()

    companion object {
        /**
         * Десериализация зоны
         */
        fun deserialize(values: Map<String, Any>): Zone {
            return Zone(
                values["name"] as String,
                PotionEffectType.getByName(values["effect"] as String) ?: throw IllegalArgumentException("Неизвестный эффект ${values["effect"]}"),
                values["worldName"] as String,
                values["x1"] as Double,
                values["y1"] as Double,
                values["z1"] as Double,
                values["x2"] as Double,
                values["y2"] as Double,
                values["z2"] as Double
            )
        }
    }

    /**
     * Сериализация зоны
     */
    fun serialize(): Map<String, Any> {
        return mapOf(
            "name" to this.name,
            "effect" to this.effect.name,
            "worldName" to this.worldName,
            "x1" to this.x1,
            "y1" to this.y1,
            "z1" to this.z1,
            "x2" to this.x2,
            "y2" to this.y2,
            "z2" to this.z2
        )
    }

    /**
     * Добавление игрока в зону
     */
    fun addPlayer(player: Player) {
        players.add(player)
    }

    /**
     * Удаление игрока из зоны
     */
    fun removePlayer(player: Player) {
        players.remove(player)
    }

    /**
     * Текущее кол-во игроков в зоне
     */
    fun getPlayersCount() : Int {
        return players.count()
    }
}