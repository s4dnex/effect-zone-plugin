package com.joutak.effectZone.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

import com.joutak.effectZone.Zone

object ZoneCommandExecutor: CommandExecutor, TabExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (!sender.hasPermission("zone.admin")) {
            sender.sendMessage("Недостаточно прав для использования команды.")
            return true
        }

        when (args?.getOrNull(0)) {
            "add" -> {
                if (sender !is Player) {
                    sender.sendMessage("Данную команду можно использовать только в игре.")
                    return true        
                }

                if (args.size != 9) {
                    return false
                }

                try {
                    val newZone = Zone(
                        args[1],
                        PotionEffectType.getByName(args[2]) ?: throw IllegalArgumentException("Неизвестный эффект ${args[2]}"),
                        sender.world.name,
                        // sender.world.environment, 
                        args[3].toDouble(), 
                        args[4].toDouble(), 
                        args[5].toDouble(), 
                        args[6].toDouble(), 
                        args[7].toDouble(), 
                        args[8].toDouble()
                    )
                    Zone.add(newZone)
                    sender.sendMessage("Добавлена зона с именем ${newZone.getName()}.")
                } catch (e: NumberFormatException) {
                    sender.sendMessage("Координаты должны быть числами.")
                } catch (e: IllegalArgumentException) {
                    sender.sendMessage("${e.message}")
                }
            }
            "remove" -> {
                if (args.size != 2) {
                    return false
                }

                try {
                    Zone.remove(args[1])
                    sender.sendMessage("Зона ${args[1]} удалена.")
                }
                catch (e: IllegalArgumentException) {
                    sender.sendMessage("${e.message}")
                }
            }
            "list" -> {
                if (args.size != 1) {
                    return false
                }

                if (Zone.getZones().isEmpty()) {
                    sender.sendMessage("Нет активных зон.")
                } else {
                    sender.sendMessage("Список зон:")
                    Zone.getZones().values.forEach {
                        sender.sendMessage(it.getName())
                    }
                }
            }
            "info" -> {
                if (args.size != 2) {
                    return false
                }

                try {
                    val zone = Zone.get(args[1])
                    val x1 = zone.getCoords()["x1"]
                    val y1 = zone.getCoords()["y1"]
                    val z1 = zone.getCoords()["z1"]
                    val x2 = zone.getCoords()["x2"]
                    val y2 = zone.getCoords()["y2"]
                    val z2 = zone.getCoords()["z2"]

                    sender.sendMessage("Информация о зоне ${zone.getName()}:")
                    sender.sendMessage("Эффект: ${zone.getEffect().name}")
                    // sender.sendMessage("Мир: ${zone.getWorldName()}")
                    sender.sendMessage("Координаты: (${x1}, ${y1}, ${z1} ; ${x2}, ${y2}, ${z2})")
                }
                catch (e: IllegalArgumentException) {
                    sender.sendMessage("${e.message}")
                }
            }
            else -> {
                return false
            }
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        return when (args.size) {
            1 -> listOf("add", "remove", "list", "info").filter { it.startsWith(args[0], true) }
            2 -> if (args[0] == "remove" || args[0] == "info") Zone.getZones().values.map { it.getName() }.filter { it.startsWith(args[1], true) } else emptyList()
            3 -> if (args[0] == "add") PotionEffectType.values().map { it.getName() }.filter { it.startsWith(args[2], true) } else emptyList()
            else -> emptyList()
        }
    }
}