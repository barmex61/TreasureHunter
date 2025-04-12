package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.state.StateEntity
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.state.SwordState
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.utils.GameObject

class AttackSystem(
    private val physicWorld : PhysicWorld = inject(),
    private val assetHelper: AssetHelper = inject()
) : IteratingSystem(
    family = family { all(Attack, Physic, Graphic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackComp = entity[Attack]
        val animComp = entity[Animation]
        val (_,center) = entity[Graphic]
        var (attackItem,wantsToAttack, attackState,doAttack) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    attackComp.doAttack = true
                    val center = entity[Graphic].center
                    if (attackItem.isMelee) {
                        world.entity {
                            it += AttackMeta(
                                owner = entity,
                                isFixtureMirrored = false,
                                attackItem = attackItem
                            )
                            it += Damage(damage = attackItem.attackDamage, sourceEntity = entity)
                            it += Physic(createAttackBody(center, it, BodyDef.BodyType.StaticBody))
                            it += Graphic(sprite(GameObject.ATTACK_EFFECT, attackItem.attackAnimType,center,assetHelper,0f))

                        }
                    } else {
                        val gameObject = attackItem.toGameObject() ?: return
                        world.entity {
                            it += AttackMeta(
                                owner = entity,
                                isFixtureMirrored = false,
                                attackItem = attackItem
                            )
                            it += Damage(damage = attackItem.attackDamage, sourceEntity = entity)
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
                                }
                            )
                            it += Animation(gameObject, animationData = AnimationData(
                                animationType = AnimationType.SPINNING,
                                playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
                            ))
                            it += State(
                                StateEntity(it, world, physicWorld, assetHelper),
                                SwordState.SPINNING
                            )
                        }
                    }
                    attackComp.attackState = AttackState.ATTACKING
                }
            }
            AttackState.ATTACKING -> {
                attackItem.attackCooldown -= deltaTime
                if (attackItem.attackCooldown <= 0f){
                    attackComp.attackState = AttackState.DONE
                }
            }
            AttackState.DONE -> {
                resetAttackComp(attackComp)
            }
        }
    }

    private fun resetAttackComp(attackComp: Attack) {
        when(attackComp.attackItem){
            is ItemType.Sword -> {
                attackComp.attackItem = ItemType.Sword()
            }
            else -> {}
        }
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

