package com.libgdx.treasurehunter.game.screens

import ParallaxBackground
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.libgdx.treasurehunter.audio.AudioManager
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MapAssets
import com.libgdx.treasurehunter.enums.SkinAsset
import com.libgdx.treasurehunter.game.TreasureHunter
import com.libgdx.treasurehunter.game.inputMultiplexer
import com.libgdx.treasurehunter.ui.model.MenuModel
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.ViewType
import com.libgdx.treasurehunter.utils.Constants.ATTACK_EFFECT_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.ATTACK_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.GamePreferences
import com.libgdx.treasurehunter.utils.GameProperties
import com.libgdx.treasurehunter.utils.fixtureDefinitionOf
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.math.vec2
import ktx.scene2d.Scene2DSkin
import ktx.tiled.isEmpty
import ktx.tiled.propertyOrNull
import kotlin.collections.set

class LoadingScreen(
    private val assetHelper: AssetHelper,
    private val spriteBatch: SpriteBatch,
    private val treasureHunter: TreasureHunter,
    private val physicWorld : World,
    private val audioManager: AudioManager,
    private val stage: Stage,
    private val gameProperties: GameProperties,
    private val gamePreferences: GamePreferences
) : KtxScreen {
    private val menuModel = MenuModel()
    private val stageNavigator = StageNavigator(stage,menuModel, gamePreferences = gamePreferences){ viewType ->
        if (viewType == ViewType.GAME){
            treasureHunter.removeScreen<LoadingScreen>()
            dispose()
            treasureHunter.addScreen(GameScreen(spriteBatch, assetHelper,physicWorld,audioManager,stage,this,gameProperties,gamePreferences))
            treasureHunter.setScreen<GameScreen>()
        }
    }
    private val bg = ParallaxBackground(stage.viewport,"graphics/bg.png", vec2(1f,1f),1f)
    private val clouds = ParallaxBackground(stage.viewport,"graphics/clouds.png", vec2(1f,1f),1f)
    private val clouds2 = ParallaxBackground(stage.viewport,"graphics/clouds_2.png", vec2(1f,1f),1f)

    override fun show() {
        assetHelper.loadAll()
        Scene2DSkin.defaultSkin = assetHelper[SkinAsset.DEFAULT]
        parseObjectCollisionShapes(tiledMap = assetHelper[MapAssets.OBJECT])
        assetHelper -= MapAssets.OBJECT
        stageNavigator.changeStageView(ViewType.MENU)
        inputMultiplexer.addProcessor(stage)
    }

    override fun hide() {
        stage.clear()
    }

    override fun render(delta: Float) {
        stage.viewport.apply()
        clouds.scrollBy(0.1f * delta,0f)
        clouds2.scrollBy(0.05f * delta,0f)
        stage.batch.use(stage.viewport.camera){
            bg.draw(0f,0f,it)
            clouds2.draw(0f,0f,it)
            clouds.draw(0f,0f,it)
        }
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height,true)
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
