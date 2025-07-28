package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.event.GameEvent.EntityLifeChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent
import com.libgdx.treasurehunter.tiled.TiledMapService.Companion.logEntity
import com.libgdx.treasurehunter.utils.GameObject

data class Life(
    val maxLife : Int,
    var damageTaken : Int = 0,
    var isDead : Boolean = false,
    val owner : Entity,
) : Component <Life> {
    var isPlayer : Boolean? = null
    var entityIcon : String? = null
    var currentLife : Int = maxLife
        set(value) {
            field = value
            fireLifeChangeEvent(entityIcon,isPlayer)
        }

    override fun World.onAdd(entity: Entity) {
        isPlayer = entity has EntityTag.PLAYER
        val modelName = entity[Graphic].modelName
        entityIcon = when(modelName) {
            GameObject.CAPTAIN_CLOWN.atlasKey -> "captain_clown_icon"
            GameObject.CAPTAIN_CLOWN_SWORD.atlasKey -> "captain_clown_icon"
            GameObject.CRABBY.atlasKey -> "crabby_icon"
            GameObject.FIERCE_TOOTH.atlasKey -> "fierce_tooth_icon"
            GameObject.PINK_STAR.atlasKey -> "pink_star_icon"
            else -> null
        }
        println("Entity Icon $entityIcon")
        if (isPlayer == true) fireLifeChangeEvent(entityIcon,isPlayer)
    }

    private fun fireLifeChangeEvent(entityIcon: String?, isPlayer: Boolean?) {
        if (entityIcon != null && isPlayer != null){
            fireEvent(EntityLifeChangeEvent(currentLife,maxLife,owner,isPlayer,entityIcon))
        }
    }
    override fun type() = Life

    companion object : ComponentType<Life>()

}
