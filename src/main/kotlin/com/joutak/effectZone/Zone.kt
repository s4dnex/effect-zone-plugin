package com.joutak.effectZone

import org.bukkit.Location
import org.bukkit.potion.PotionEffectType
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.Bukkit

class Zone (
    private val name : String,
    private val effect : PotionEffectType,
    private val worldName: String,
    // val worldEnv: World.Environment,
    private val x1: Double, private val y1: Double, private val z1: Double,
    private val x2: Double, private val y2: Double, private val z2: Double
    ) {
        companion object {
            private val zones = mutableMapOf<String, Zone>()

            public fun add(zone: Zone) {
                if (zones.containsKey(zone.name)) {
                    throw IllegalArgumentException("Зона с таким именем уже существует.")
                }
                
                
                zones.put(zone.name, zone)
            }

            public fun get(name: String): Zone {
                if (!zones.containsKey(name)) {
                    throw IllegalArgumentException("Зоны с таким именем не существует.")
                }

                return zones.get(name)!!
            }
    
            public fun remove(name: String) {
                if (!zones.containsKey(name)) {
                    throw IllegalArgumentException("Зоны с таким именем не существует.")
                }
    
                zones.remove(name)
            }
    
            public fun getZones(): Map<String, Zone> {
                return zones
            }
            
            public fun clear() {
                zones.clear()
            }

            public fun deserialize(values: Map<String, Any>) : Zone {
                return Zone(
                    values["name"] as String,
                    PotionEffectType.getByName(values["effect"] as String) ?: throw IllegalArgumentException("Неизвестный эффект ${values["effect"]}"),
                    values["worldName"] as String,
                    // World.Environment.valueOf(values["worldEnv"] as String),
                    values["x1"] as Double,
                    values["y1"] as Double,
                    values["z1"] as Double,
                    values["x2"] as Double,
                    values["y2"] as Double,
                    values["z2"] as Double
                )

            }
        }

        fun getName(): String {
            return name
        }

        fun getEffect(): PotionEffectType {
            return effect
        }

        fun getWorldName(): String {
            return worldName
        }

        fun getCoords(): Map<String, Double> {
            return mapOf(
                "x1" to x1,
                "y1" to y1,
                "z1" to z1,
                "x2" to x2,
                "y2" to y2,
                "z2" to z2
            )
        }
        
        fun isInside(loc: Location): Boolean {
            val world = Bukkit.getWorld(worldName) ?: throw IllegalArgumentException("Мир $worldName не найден.")

            if (loc.world?.name != world.name) return false // || (loc.world.environment != worldEnv))
            return loc.x in minOf(x1, x2)..maxOf(x1, x2) &&
                   loc.y in minOf(y1, y2)..maxOf(y1, y2) &&
                   loc.z in minOf(z1, z2)..maxOf(z1, z2)
        }

        fun spawnParticles(y: Double) {
            val world = Bukkit.getWorld(worldName) ?: throw IllegalArgumentException("Мир $worldName не найден.")
 
            for (x in x1.toInt()..x2.toInt()) {
                for (z in z1.toInt()..z2.toInt()) {
                    val loc = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
                    world.spawnParticle(Particle.CLOUD, loc, 0, 0.0, 0.0, 0.0, 0.0)
                }
            }
        }

        fun serialize(): Map<String, Any> {
            return mapOf(
                "name" to name,
                "effect" to effect.name,
                "worldName" to worldName,
                // "worldEnv" to worldEnv.name,
                "x1" to x1,
                "y1" to y1,
                "z1" to z1,
                "x2" to x2,
                "y2" to y2,
                "z2" to z2
            )
        }
    }