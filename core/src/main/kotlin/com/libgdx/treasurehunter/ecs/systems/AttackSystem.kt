package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ai.AiEntity
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackItem.Companion.toGameObject
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.ai.SwordState
import com.libgdx.treasurehunter.ecs.components.Move

class AttackSystem(
    private val physicWorld : PhysicWorld = inject(),
    private val assetHelper: AssetHelper = inject()
) : IteratingSystem(
    family = family { all(Attack, Physic, Graphic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackComp = entity[Attack]
        val animComp = entity[Animation]
        val (_,_,center) = entity[Graphic]
        var (attackItem,wantsToAttack, attackState,attackType,attackCooldown) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    val center = entity[Graphic].center
                    if (attackComp.isMeleeAttack) {
                        world.entity {
                            it += AttackMeta(
                                isMelee = true,
                                owner = entity,
                                attackType = attackType,
                                isFixtureMirrored = false
                            )
                            it += Damage(damage = attackComp.attackDamage, sourceEntity = entity)
                            it += Physic(createAttackBody(center, it, BodyDef.BodyType.StaticBody))
                        }
                    } else {
                        val gameObject = attackItem.toGameObject() ?: return
                        world.entity {
                            it += AttackMeta(
                                isMelee = false,
                                owner = entity,
                                attackType = attackType,
                                isFixtureMirrored = false,
                            )
                            it += Damage(damage = attackComp.attackDamage, sourceEntity = entity)
                            it += Physic(createAttackBody(center, it, BodyDef.BodyType.DynamicBody))
                            it += Graphic(
                                sprite(
                                    gameObject,
                                    AnimationType.SPINNING,
                                    center,
                                    assetHelper,
                                    0f
                                ).also {
                                    it.setAlpha(0f)
                                }, gameObject
                            )
                            it += Animation(frameDuration = 0.1f, gameObject = gameObject)
                            it += State(
                                AiEntity(it, world, physicWorld, assetHelper),
                                SwordState.SPINNING
                            )
                        }
                    }
                    attackComp.attackState = AttackState.ATTACKING
                }
            }
            AttackState.ATTACKING -> {
                attackCooldown -= deltaTime
                attackComp.attackCooldown = attackCooldown
                if (attackCooldown <= 0f){
                    attackComp.attackState = AttackState.DONE
                }
            }
            AttackState.DONE -> {
                resetAttackComp(attackComp)
            }
        }
    }

    private fun resetAttackComp(attackComp: Attack) {
        attackComp.attackCooldown = 1f
        attackComp.attackState = AttackState.READY
        attackComp.wantsToAttack = false
    }

    private fun createAttackBody(centerPosition : Vector2,activeAttackEntity: Entity,bodyType: BodyDef.BodyType): Body {
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = bodyType
            position.set(centerPosition.x,centerPosition.y )
            fixedRotation = true
            gravityScale = 0f

        }).apply { userData =  activeAttackEntity }
        return attackBody
    }


}

