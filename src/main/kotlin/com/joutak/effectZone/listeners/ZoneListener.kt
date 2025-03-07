package com.joutak.effectZone.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

import com.joutak.effectZone.Zone

object ZoneListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val to = event.to

        for (zone in Zone.getZones().values) {
            if (zone.isInside(to)) {
                zone.spawnParticles(to.getY())
                player.addPotionEffect(
                    PotionEffect(zone.getEffect(), 100, 1, false, true, false)
                )
                return
            }
        }
    }
}