package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.FlashType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Invulnarable
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.event.GameEvent.EntityLifeChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent

class DamageSystem(
    val gameCamera: OrthographicCamera = inject(),
    val stage : Stage = inject()
) : IteratingSystem(family = family{all(Life,DamageTaken).none(Invulnarable,Blink)}) {

    override fun onTickEntity(entity: Entity) {
        val damageTaken = entity[DamageTaken]
        val lifeComp = entity[Life]
        val (damageAmount,isContinuous,isCrit) = damageTaken
        lifeComp.currentLife = (lifeComp.currentLife - damageAmount).coerceAtLeast(0)
        handleDamageText(entity,damageAmount,isCrit)
        if (entity has EntityTag.PLAYER) {
            fireEvent(GameEvent.PlaySoundEvent(SoundAsset.PLAYER_HURT))
        } else {
            fireEvent(GameEvent.PlaySoundEvent(SoundAsset.FIERCE_TOOTH_HURT))
        }
        if (lifeComp.currentLife <= 0){
            entity.configure {
                it -= Life
            }
            return
        }

        entity.configure {
            it += Invulnarable(1f)
            it += Blink(1f,0.075f)
            it += Flash(shaderEffect = ShaderEffect.HIT_EFFECT, flashTimer = 1f, flashDuration = 0.15f, flashInterval = 0.15f, flashType = FlashType.BLINK)
        }

    }

    private fun handleDamageText(entity: Entity, damageAmount: Int, isCrit: Boolean) {
        val center = entity.getOrNull(Graphic)?.rotatedCenter ?: return
        val screenPos = worldToScreenCoordinates(center)
        fireEvent(GameEvent.EntityDamageTakenEvent(damageAmount, isCrit, screenPos))
    }

    private fun worldToScreenCoordinates(worldPos: Vector2): Vector2 {
        val screenPos = Vector3(worldPos.x, worldPos.y, 0f)
        gameCamera.project(screenPos)
        stage.viewport.unproject(screenPos)

        return Vector2(screenPos.x, screenPos.y)
    }
}
