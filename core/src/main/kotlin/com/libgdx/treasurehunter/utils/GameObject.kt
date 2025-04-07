package com.libgdx.treasurehunter.utils


enum class GameObject() {
    CAPTAIN_CLOWN,
    CAPTAIN_CLOWN_SWORD,
    BACK_PALM_TREE_RIGHT,
    BACK_PALM_TREE_LEFT,
    BACK_PALM_TREE_REGULAR,
    GROUND;
    val atlasKey = this.name.lowercase()
}
