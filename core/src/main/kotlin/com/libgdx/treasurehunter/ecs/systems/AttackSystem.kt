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
import com.libgdx.treasurehunter.ecs.components.AttackMetaData
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Item
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
        var (wantsToAttack, attackState,doAttack,attackMetaData) = attackComp
        val attackType = getQueuedAttackType(entity,attackComp.queuedAttackType)
        attackType?.let { attackMetaData.attackType =it }
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    attackComp.doAttack = true
                    val center = entity[Graphic].center
                    if (attackMetaData.isMelee) {
                        world.entity {
                            it += AttackMeta(
                                owner = entity,
                                isFixtureMirrored = false,
                                attackMetaData = attackMetaData
                            )
                            it += Damage(damage = attackMetaData.attackDamage, sourceEntity = entity)
                            it += Physic(createAttackBody(center, it, BodyDef.BodyType.StaticBody))
                            it += Graphic(sprite(GameObject.ATTACK_EFFECT, attackMetaData.attackType.attackAnimType,center,assetHelper,0f))

                        }
                    } else {
                        val item = getAttackItem(entity)
                        if (item == null){
                            resetAttackComp(attackComp,attackMetaData)
                            return
                        }
                        val gameObject = item.toGameObject() ?: return
                        world.entity {
                            it += AttackMeta(
                                owner = entity,
                                isFixtureMirrored = false,
                                attackMetaData = attackMetaData
                            )
                            it += Damage(damage = attackMetaData.attackDamage, sourceEntity = entity)
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
                    val animDuration = if (attackMetaData.attackAnimPlayMode == com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL){
                        animComp.animationData.gdxAnimation?.animationDuration?:0f
                    } else {
                        0f
                    }
                    attackMetaData.attackCooldown += animDuration
                    attackComp.attackState = AttackState.ATTACKING
                }
            }
            AttackState.ATTACKING -> {
                reduceAttackCooldown(attackMetaData,attackComp)
            }
            AttackState.DONE -> {
                resetAttackComp(attackComp,attackMetaData)
            }
        }
    }

    private fun reduceAttackCooldown(attackMetaData: AttackMetaData,attackComp: Attack) {
        attackMetaData.attackCooldown -= deltaTime
        if (attackMetaData.attackCooldown <= 0f){
            attackComp.attackState = AttackState.DONE
        }
    }

    private fun resetAttackComp(attackComp: Attack, attackMetaData: AttackMetaData) {
        attackMetaData.resetAttackCooldown()
        attackMetaData.resetAttackDestroyTime()
        attackComp.attackState = AttackState.READY
        attackComp.wantsToAttack = false
        attackComp.doAttack = false
    }

    private fun createAttackBody(centerPosition : Vector2, attackMetaData: Entity, bodyType: BodyDef.BodyType): Body {
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = bodyType
            position.set(centerPosition.x,centerPosition.y )
            fixedRotation = true
            gravityScale = 0f

        }).apply { userData =  attackMetaData }
        return attackBody
    }

    private fun getQueuedAttackType(entity: Entity, queuedAttackType: AttackType?) : AttackType?{
        val onAir = entity[Physic].onAir
        val updatedAttackType = when{
            queuedAttackType == AttackType.THROW -> AttackType.THROW
            !onAir -> queuedAttackType
            queuedAttackType == AttackType.ATTACK_1 && onAir -> AttackType.AIR_ATTACK_1
            queuedAttackType == AttackType.ATTACK_2 && onAir -> AttackType.AIR_ATTACK_2
            else -> return null
        }
        return updatedAttackType
    }

    private fun getAttackItem(entity: Entity) : ItemType.Damageable?{
        val itemComp = entity.getOrNull(Item)?:return null
        if (itemComp.itemType !is ItemType.Damageable) return null
        val item = itemComp.itemType
        return item as ItemType.Damageable?
    }



}

