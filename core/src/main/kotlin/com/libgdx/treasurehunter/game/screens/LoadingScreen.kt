package com.libgdx.treasurehunter.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.physics.box2d.World
import com.libgdx.treasurehunter.audio.AudioManager
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MapAssets
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent
import com.libgdx.treasurehunter.game.TreasureHunter
import com.libgdx.treasurehunter.utils.Constants.ATTACK_EFFECT_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.ATTACK_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.currentMapPath
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.fixtureDefinitionOf
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.tiled.isEmpty
import ktx.tiled.propertyOrNull
import kotlin.collections.set

class LoadingScreen(
    private val assetHelper: AssetHelper,
    private val spriteBatch: SpriteBatch,
    private val treasureHunter: TreasureHunter,
    private val physicWorld : World,
    private val audioManager: AudioManager
) : KtxScreen {

    override fun show() {
        assetHelper.loadAll()
        parseObjectCollisionShapes(tiledMap = assetHelper[MapAssets.OBJECT])
        assetHelper -= MapAssets.OBJECT
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1)){
            currentMapPath = MapAssets.MAP1.name
            treasureHunter.removeScreen<LoadingScreen>()
            dispose()
            treasureHunter.addScreen(GameScreen(spriteBatch, assetHelper,physicWorld,audioManager))
            treasureHunter.setScreen<GameScreen>()
        }

    }

    fun parseObjectCollisionShapes(tiledMap: TiledMap){
        tiledMap.tileSets.forEach { tileSet ->
            tileSet.forEach{ tile ->
                tile?:return@forEach
                when(tileSet.name){
                    "objects" -> parseGameObjectFixtures(tile)
                    "attackObjects" -> parseAttackFixtures(tile,false)
                    "attackEffects" -> parseAttackFixtures(tile,true)
                }
            }
        }
    }

    private fun parseGameObjectFixtures(tile : TiledMapTile){
        val gameObjectStr : String = tile.propertyOrNull("gameObject") ?: return
        if (OBJECT_FIXTURES[GameObject.valueOf(gameObjectStr)] != null) return
        val objectFixtureDefinitions = tile.objects.map { fixtureDefinitionOf(it,true) }
        if (objectFixtureDefinitions.isEmpty()) return
        OBJECT_FIXTURES[GameObject.valueOf(gameObjectStr)] = objectFixtureDefinitions
    }

    private fun parseAttackFixtures(tile : TiledMapTile,isAttackEffect : Boolean){
        val attackTypeStr : String = tile.propertyOrNull("attackType") ?: return
        val keyFrameIx : Int = tile.propertyOrNull("keyFrameIx") ?: return
        if (tile.objects.isEmpty()) return
        val attackType = AttackType.valueOf(attackTypeStr)
        val fixtureDefList = tile.objects.map{fixtureDefinitionOf(it) }
        if (!isAttackEffect){
            ATTACK_FIXTURES[Pair(attackType,keyFrameIx)] = fixtureDefList
        }else{
            ATTACK_EFFECT_FIXTURES[Pair(attackType,keyFrameIx)] = fixtureDefList
        }
    }


}
