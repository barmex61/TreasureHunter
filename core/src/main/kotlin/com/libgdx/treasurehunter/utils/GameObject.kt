package com.libgdx.treasurehunter.utils


enum class GameObject() {
    CAPTAIN_CLOWN,
    CAPTAIN_CLOWN_SWORD,
    GROUND;
    val atlasKey = this.name.lowercase()
}
