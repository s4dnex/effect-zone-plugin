package com.joutak.effectZone.listeners

import com.joutak.effectZone.utils.EffectsManager
import com.joutak.effectZone.utils.PlayerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import com.joutak.effectZone.utils.ZoneManager

object ZoneListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val location = player.location

        // Берем каждую зону и проверяем, находится ли игрок в ней
        for (zone in ZoneManager.getZones().values) {
            // Если внутри какой-либо зоны, то "регистрируем" его
            if (ZoneManager.isInside(zone, location)) {
                PlayerManager.add(player, zone)
                zone.addPlayer(player)
            }
            // Если игрока нет в этой зоне
            else {
                // Пробуем удалить игрока из зоны, и если он в ней был, то удаляем соответствующий эффект
                if (PlayerManager.removeZone(player, zone)) {
                    EffectsManager.removeEffect(player, zone)
                }
                // Убираем из зоны данного игрока
                zone.removePlayer(player)
            }
        }

        // После проверки каждой зоны накладываем соответствующие эффекты и обновляем партиклы
        EffectsManager.applyEffects(player)
        EffectsManager.updateParticles()
    }
}