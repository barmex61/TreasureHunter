package com.libgdx.treasurehunter.utils


enum class GameObject() {
    CAPTAIN_CLOWN,
    CAPTAIN_CLOWN_SWORD,
    BACK_PALM_TREE_RIGHT,
    BACK_PALM_TREE_LEFT,
    BACK_PALM_TREE_REGULAR,
    FRONT_PALM_TREE,
    PALM_TREE_BOTTOM_RIGHT,
    PALM_TREE_BOTTOM_LEFT,
    PALM_TREE_BOTTOM_REGULAR,
    CHEST,
    FLAG,
    SHIP_HELM,
    DEAD_MARK,
    SWORD,
    INTERROGATION_MARK,
    EXCLAMATION_MARK,
    SPIKES,
    GROUND;
    val atlasKey = this.name.lowercase()
}
