package com.libgdx.treasurehunter.utils

import com.libgdx.treasurehunter.enums.SoundAsset
import ktx.app.gdxError


enum class GameObject() {
    CAPTAIN_CLOWN,
    CAPTAIN_CLOWN_SWORD,
    CRABBY,
    WOOD_SPIKE,
    FIERCE_TOOTH,
    PINK_STAR,
    BACK_PALM_TREE_RIGHT,
    BACK_PALM_TREE_LEFT,
    BACK_PALM_TREE_REGULAR,
    FRONT_PALM_TREE,
    PALM_TREE_BOTTOM_RIGHT,
    PALM_TREE_BOTTOM_LEFT,
    PALM_TREE_BOTTOM_REGULAR,
    CHEST,
    BIG_MAP,
    SMALL_MAP_1,
    SMALL_MAP_2,
    SMALL_MAP_3,
    SMALL_MAP_4,
    BLUE_DIAMOND,
    BLUE_POTION,
    GOLDEN_SKULL,
    GOLD_COIN,
    GREEN_BOTTLE,
    GREEN_DIAMOND,
    RED_DIAMOND,
    RED_POTION,
    SILVER_COIN,
    TOTEM_HEAD_1,
    TOTEM_HEAD_2,
    TOTEM_HEAD_3,
    TOTEM_HEAD_4,
    TOTEM_HEAD_5,
    TOTEM_HEAD_6,
    SAIL,
    FLAG,
    SHIP,
    KEY,
    ANCHOR,
    BOX,
    BARREL,
    PADLOCK,
    CHEST_LOCKED,
    SHIP_HELM,
    DEAD_MARK,
    SWORD,
    SWORD_EMBEDDED,
    INTERROGATION_MARK,
    EXCLAMATION_MARK,
    SPIKES,
    DUST_PARTICLES,
    WATER,
    GROUND;
    val atlasKey = this.name.lowercase()
    fun toJumpSoundAsset() : SoundAsset {
        return when(this){
            CAPTAIN_CLOWN -> SoundAsset.PLAYER_JUMP
            CAPTAIN_CLOWN_SWORD -> SoundAsset.PLAYER_JUMP
            PINK_STAR-> SoundAsset.PINK_STAR_JUMP
            CRABBY -> SoundAsset.CRABBY_JUMP
            FIERCE_TOOTH -> SoundAsset.FIERCE_TOOTH_JUMP
            else -> gdxError("There is no jump sound asset for this gameObject $this")
        }
    }
}
