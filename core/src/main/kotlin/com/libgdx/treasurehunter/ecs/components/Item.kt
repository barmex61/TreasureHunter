package com.libgdx.treasurehunter.ecs.components

import com.libgdx.treasurehunter.ecs.components.ItemType.Damageable
import com.libgdx.treasurehunter.ecs.components.ItemType.Throwable
import com.libgdx.treasurehunter.utils.GameObject

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.enums.ShaderEffect.*
import ktx.app.gdxError

data class Item(
    val itemData: ItemData,
) : Component<Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}

data class ItemData(
    val itemType: ItemType,
    var owner: Entity? = null,
)

enum class ThrowState {
    READY,
    THROWING,
    THROWED
}

sealed interface ItemType {
    interface Damageable : ItemType
    interface Throwable : ItemType
    interface Equippable : ItemType
    interface Consumable : ItemType

    interface Collectable : ItemType

    fun getDrawablePath(): String = when (this) {
        is Sword -> "sword_on"
        is Map -> {
            when (mapType) {
                MapType.BIG_MAP -> "big_map_1"
                MapType.SMALL_MAP_1 -> "small_map_1"
                MapType.SMALL_MAP_2 -> "small_map_2"
                MapType.SMALL_MAP_3 -> "small_map_3"
                MapType.SMALL_MAP_4 -> "small_map_4"
            }
        }

        is Key -> "key"
        is BlueDiamond -> "blue_diamond"
        is GreenDiamond -> "green_diamond"
        is RedDiamond -> "red_diamond"
        is GoldCoin -> "gold_coin"
        is SilverCoin -> "silver_coin"
        is GoldenSkull -> "golden_skull"
        is BluePotion -> "blue_potion"
        is RedPotion -> "red_potion"
        is GreenBottle -> "green_bottle"

        else -> gdxError("ItemType $this does not have a drawable path defined")
    }

    fun getProperties(): List<Pair<String, String>> = when (this) {
        is Sword -> listOf(Pair("Attack", attackAmount.toString()))
        is Armor -> listOf(Pair("Defence", defenceAmount.toString()))
        is Helmet -> listOf(Pair("Defence", defenceAmount.toString()))
        is Boots -> listOf(
            Pair("Defence", defenceAmount.toString()),
            Pair("Move Speed", moveAmount.toString())
        )
        is RedPotion -> listOf(Pair("Heal Amount", healAmount.toString()))
        else -> listOf()
    }

    fun getDescription(): String = when(this){
        is Sword -> "A sharp sword that deals additional damage."
        is Armor -> "A sturdy armor that provides additional defence."
        is Helmet -> "A protective helmet that provides additional defence."
        is Boots -> "Boots that provide additional defence and increase movement speed."
        is RedPotion -> "A potion that restores health points."
        is BluePotion -> "A potion that instantly restores your health."
        is GreenBottle -> "A mysterious green bottle. Its effect is unknown."
        is Map -> when (mapType) {
            MapType.BIG_MAP -> "A big map that shows the entire island."
            MapType.SMALL_MAP_1 -> "A small map that reveals a part of the island."
            MapType.SMALL_MAP_2 -> "A small map that reveals a part of the island."
            MapType.SMALL_MAP_3 -> "A small map that reveals a part of the island."
            MapType.SMALL_MAP_4 -> "A small map that reveals a part of the island."
        }
        is Key -> "A key that can open locked chests."
        is BlueDiamond -> "A rare blue diamond. Valuable for trading."
        is GreenDiamond -> "A rare green diamond. Valuable for trading."
        is RedDiamond -> "A rare red diamond. Valuable for trading."
        is GoldCoin -> "A shiny gold coin. Used for trading."
        is SilverCoin -> "A shiny silver coin. Used for trading."
        is GoldenSkull -> "A golden skull. Looks ancient and valuable."
        is Projectile -> "A throwable object that can damage enemies."
        else -> "An item of type ${this::class.simpleName ?: "Unknown Item Type"}."
    }

    fun getDisplayName() : String = when(this){
        is Sword -> "Sword"
        is Armor -> "Armor"
        is Helmet -> "Helmet"
        is Boots -> "Boots"
        is RedPotion -> "Red Potion"
        is BluePotion -> "Blue Potion"
        is GreenBottle -> "Green Bottle"
        is Map -> when(mapType){
            MapType.BIG_MAP -> "Big Map"
            MapType.SMALL_MAP_1 -> "Small Map 1"
            MapType.SMALL_MAP_2 -> "Small Map 2"
            MapType.SMALL_MAP_3 -> "Small Map 3"
            MapType.SMALL_MAP_4 -> "Small Map 4"
        }
        is Key -> "Key"
        is BlueDiamond -> "Blue Diamond"
        is GreenDiamond -> "Green Diamond"
        is RedDiamond -> "Red Diamond"
        is GoldCoin -> "Gold Coin"
        is SilverCoin -> "Silver Coin"
        is GoldenSkull -> "Golden Skull"
        else -> this::class.simpleName ?: "Unknown Item Type"

    }

    fun getItemShaderEffect(): ShaderEffect = when(this) {
        is Sword -> SWORD_GREEN_EFFECT
        is Armor -> ARMOR_EFFECT
        is Boots -> BOOTS_EFFECT
        is Helmet -> HELMET_EFFECT
        is GoldCoin -> ITEM_APPEAR
        is SilverCoin -> ITEM_APPEAR
        is Key -> ITEM_APPEAR
        is BlueDiamond -> ITEM_APPEAR
        is GreenDiamond -> ITEM_APPEAR
        is RedDiamond -> ITEM_APPEAR
        is GoldenSkull -> ITEM_APPEAR
        is BluePotion -> ITEM_APPEAR
        is RedPotion -> ITEM_APPEAR
        is GreenBottle -> ITEM_APPEAR
        is Map -> ITEM_APPEAR
        else -> ITEM_APPEAR
    }
}

data class Sword(val attackAmount: Int) : Damageable, Throwable, ItemType.Equippable {
}

data class Projectile(
    val projectileType: ProjectileType,
) : Damageable, Throwable

data class Map(
    val mapType: MapType,
) : ItemType.Collectable


data class Armor(val defenceAmount: Int) : ItemType.Equippable

data class Helmet(val defenceAmount: Int) : ItemType.Equippable
data class Boots(val defenceAmount: Int, val moveAmount: Int) : ItemType.Equippable

object BlueDiamond : ItemType.Collectable
object GreenDiamond : ItemType.Collectable
object RedDiamond : ItemType.Collectable

object GoldCoin : ItemType.Collectable
object SilverCoin : ItemType.Collectable

object GoldenSkull : ItemType.Collectable
object Key : ItemType.Collectable

object BluePotion : ItemType.Consumable
data class RedPotion(val healAmount: Int) : ItemType.Consumable

object GreenBottle : ItemType.Consumable


enum class MapType {
    BIG_MAP,
    SMALL_MAP_1,
    SMALL_MAP_2,
    SMALL_MAP_3,
    SMALL_MAP_4
}

enum class ProjectileType {
    WOOD_SPIKE;

    fun toGameObject(): GameObject {
        return when (this) {
            WOOD_SPIKE -> GameObject.WOOD_SPIKE
        }
    }
}


