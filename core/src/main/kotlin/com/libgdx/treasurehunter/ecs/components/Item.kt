package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.ecs.components.ItemType.Damageable
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError
import kotlin.Float

enum class AttackType(val isMelee : Boolean) {
    ATTACK_1(true),
    ATTACK_2(true),
    ATTACK_3(true),
    AIR_ATTACK_1(true),
    AIR_ATTACK_2(true),
    NATURAL(true),
    THROW(false),;

    val attackAnimType : AnimationType
        get() = AnimationType.valueOf(this.name)
}

sealed interface ItemType{
    interface Damageable : ItemType {
        val attackMetaData : AttackMetaData
        fun toGameObject() : GameObject?{
            return when(this){
                is Sword -> GameObject.SWORD
                else -> null
            }
        }
    }

    data class Consumable(
        val effect: String
    ) : ItemType
}

data class Sword(
    override val attackMetaData: AttackMetaData = AttackMetaData(
        attackSpeed= 1f,
        attackRange= 0f,
        attackType= AttackType.ATTACK_1,
        attackDamage= 1,
        baseAttackCooldown= 1.5f,
        baseAttackDestroyTime= 1.5f,
    )

    ) : Damageable

// -------------- ILERIDE HER KARAKTER İÇİN FARKLI BİR ATTACK META DATA OLUŞTURULACAK -----------
data class Natural(
    override val attackMetaData: AttackMetaData = AttackMetaData(
        attackSpeed= 1f,
        attackRange= 1f,
        attackType= AttackType.NATURAL,
        attackDamage= 1,
        baseAttackCooldown= 1.5f,
        baseAttackDestroyTime= 1.5f,
    )

) : Damageable


data class Bomb(
    override val attackMetaData: AttackMetaData = AttackMetaData(
        attackSpeed= 1f,
        attackRange= 0f,
        attackType= AttackType.ATTACK_1,
        attackDamage= 3,
        baseAttackCooldown=2f,
        baseAttackDestroyTime= 2f,
    )
) : Damageable


data class Item(
    var itemType: ItemType,
    val owner: Entity,
    val isPickedUp: Boolean = false,
    val isUsed: Boolean = false,

    ) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}
