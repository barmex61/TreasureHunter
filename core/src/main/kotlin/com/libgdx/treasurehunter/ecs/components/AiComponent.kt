package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.btree.BehaviorTree
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ai.CrewEntity
import com.libgdx.treasurehunter.game.PhysicWorld

data class AiComponent(
    val nearbyEntities : MutableSet<Entity> = mutableSetOf(),
    var treePath : String = "",
    val physicWorld: PhysicWorld,
    val aiWanderRadius : Float
) : Component <AiComponent> {

    lateinit var behaviorTree : BehaviorTree<CrewEntity>

    override fun World.onAdd(entity: Entity) {
        val behaviorTreeParser = BehaviorTreeParser<CrewEntity>()
        behaviorTree = behaviorTreeParser.parse(Gdx.files.internal(treePath), CrewEntity(this,entity,physicWorld))
    }

    override fun type() = AiComponent

    companion object : ComponentType<AiComponent>()

}
