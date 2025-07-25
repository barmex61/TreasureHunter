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
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Projectile
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.ecs.components.ThrowState
import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.factory.AttackMetaDataFactory
import com.libgdx.treasurehunter.tiled.configureEntityTags
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
        val (wantsToAttack, attackState,doAttack,_) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    val attackType = getQueuedAttackType(entity,attackComp.queuedAttackType)
                    attackComp.attackMetaData = AttackMetaDataFactory.create(attackComp.attackMetaData.gameObject,attackType)
                    val newAttackMetaData = attackComp.attackMetaData.copy()
                    attackType?.let {
                        newAttackMetaData.attackType = it
                    }
                    attackComp.doAttack = true
                    val graphic = entity[Graphic]
                    val bottomCenter = graphic.bottomCenter
                    val center = graphic.center
                    if (newAttackMetaData.isMelee) {
                        world.entity {
                            it += AttackMeta(
                                owner = entity,
                                isFixtureMirrored = false,
                                attackMetaData = newAttackMetaData
                            )
                            it += Damage(damage = newAttackMetaData.attackDamage, sourceEntity = entity,false)
                            it += Physic(createAttackBody(bottomCenter, it, BodyDef.BodyType.KinematicBody))
                            it += Graphic(sprite("attack_effect", newAttackMetaData.attackType.attackAnimType,bottomCenter,assetHelper),"attack_effect",)
                        }
                    } else {
                        when (newAttackMetaData.gameObject) {
                            GameObject.SWORD -> handleRangeSwordAttack(entity, center, newAttackMetaData)
                            GameObject.WOOD_SPIKE -> handleRangeProjectileAttack(entity, center, newAttackMetaData)
                            else -> {
                                resetAttackComp(attackComp, newAttackMetaData)
                                return
                            }
                        }
                    }
                    val animDuration = if (newAttackMetaData.attackAnimPlayMode == com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL){
                        animComp.animationData.gdxAnimation?.animationDuration?:0f
                    } else {
                        0f
                    }
                    newAttackMetaData.attackCooldown += animDuration
                    attackComp.attackState = AttackState.ATTACKING
                    attackComp.attackMetaData = newAttackMetaData
                }
            }
            AttackState.ATTACKING -> {
                reduceAttackCooldown(attackComp.attackMetaData,attackComp)
            }
            AttackState.DONE -> {
                resetAttackComp(attackComp,attackComp.attackMetaData)
            }
        }
    }

    private fun handleRangeSwordAttack(
        entity: Entity,
        center: Vector2,
        newAttackMetaData : AttackMetaData
    ) {
        world.entity {
            it += AttackMeta(
                owner = entity,
                isFixtureMirrored = false,
                attackMetaData = newAttackMetaData,
                createFrameIndex = newAttackMetaData.createFrameIndex
            )
            it += Damage(damage = newAttackMetaData.attackDamage, sourceEntity = entity, false)
            it += Physic(createAttackBody(center, it, BodyDef.BodyType.DynamicBody))
            it += Graphic(
                sprite(
                    newAttackMetaData.gameObject.atlasKey,
                    AnimationType.SPINNING,
                    center,
                    assetHelper,
                    0f
                ).also {
                    it.setAlpha(0f)
                },
                newAttackMetaData.gameObject.atlasKey
            )
            it += Animation(
                newAttackMetaData.gameObject.atlasKey, animationData = AnimationData(
                    animationType = AnimationType.SPINNING,
                    playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
                )
            )
            it += State(
                StateEntity.SwordEntity(it, world, physicWorld, assetHelper),
                SwordState.SPINNING as EntityState<StateEntity>
            )
        }
    }

    private fun handleRangeProjectileAttack(
        entity: Entity,
        center: Vector2,
        newAttackMetaData : AttackMetaData
    ){
        world.entity {
            it += AttackMeta(
                owner = entity,
                isFixtureMirrored = false,
                attackMetaData = newAttackMetaData,
                createFrameIndex = newAttackMetaData.createFrameIndex
            )
            it += Damage(damage = newAttackMetaData.attackDamage, sourceEntity = entity, false)
            it += Physic(createAttackBody(center, it, BodyDef.BodyType.DynamicBody))
            it += Graphic(
                sprite(
                    newAttackMetaData.gameObject.atlasKey,
                    AnimationType.IDLE,
                    center,
                    assetHelper,
                    0f
                ).also {
                    it.setAlpha(0f)
                },
                initialFlipX = true,
                modelName = newAttackMetaData.gameObject.atlasKey
            )
            it += Animation(
                newAttackMetaData.gameObject.atlasKey, animationData = AnimationData(
                    animationType = AnimationType.IDLE,
                    playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP
                )
            )
        }
    }


    private fun reduceAttackCooldown(attackMetaData: AttackMetaData,attackComp: Attack) {
        attackMetaData.attackCooldown -= deltaTime
        if (attackMetaData.attackCooldown <= 0f  ){
            attackComp.attackState = AttackState.DONE
        }
    }

    private fun resetAttackComp(attackComp: Attack, attackMetaData: AttackMetaData) {

        attackMetaData.resetAttackCooldown()
        attackMetaData.resetAttackDestroyTime()
        attackComp.wantsToAttack = false
        attackComp.doAttack = false
        attackComp.attackState = AttackState.READY
    }

    private fun createAttackBody(centerPosition : Vector2, entity: Entity, bodyType: BodyDef.BodyType): Body {
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = bodyType
            position.set(centerPosition.x,centerPosition.y )
            fixedRotation = true
            gravityScale = 0f
        }).apply {
            userData =  entity
        }
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


}

