package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.utils.animation

data class CrewEntity(
    val world: World,
    val entity: Entity
){

    val isGetHit: Boolean
        get() = getOrNull(DamageTaken) != null

    val isDead: Boolean
        get() {
            val lifeComp = getOrNull(Life) ?: return true
            return lifeComp.isDead
        }

    val isEnemyNearby : Boolean
        get() = this[AiComponent].nearbyEntities.isNotEmpty()

    val canAttack : Boolean
        get() {
            val attackComp = getOrNull(Attack) ?: return false
            return attackComp.doAttack
        }

    var stop : Boolean
        get() {
            val moveComp = getOrNull(Move) ?: return false
            return moveComp.stop
        }
        set(value) {
            val moveComp = getOrNull(Move) ?: return
            moveComp.stop = value
        }

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    inline fun <reified T: Component<*>> getOrNull(type: ComponentType<T>) : T? = with(world){
        return entity.getOrNull(type)
    }

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP,frameDuration: Float? = null) = with(world){
        animation(entity,animationType,playMode,frameDuration)
    }

}
