package com.libgdx.treasurehunter.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.World
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MapAssets
import com.libgdx.treasurehunter.game.TreasureHunter
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.fixtureDefinitionOf
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.tiled.propertyOrNull
import kotlin.collections.set

class LoadingScreen(
    private val assetHelper: AssetHelper,
    private val spriteBatch: SpriteBatch,
    private val treasureHunter: TreasureHunter,
    private val physicWorld : World
) : KtxScreen {

    override fun show() {
        assetHelper.loadAll()
        parseObjectCollisionShapes(tiledMap = assetHelper[MapAssets.OBJECT])
        assetHelper -= MapAssets.OBJECT
    }

    override fun render(delta: Float) {
        if (Gdx.input.isTouched || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            treasureHunter.removeScreen<LoadingScreen>()
            dispose()
            treasureHunter.addScreen(GameScreen(spriteBatch, assetHelper,physicWorld))
            treasureHunter.setScreen<GameScreen>()
        }
    }

    fun parseObjectCollisionShapes(tiledMap: TiledMap){
        val tileset = tiledMap.tileSets.getTileSet(0) ?: gdxError("There is no tileset in the ${MapAssets.OBJECT} tiledMap")
        tileset.forEach{ tile ->
            tile?:return@forEach
            val gameObjectStr : String = tile.propertyOrNull("gameObject") ?: gdxError("Missing property 'GameObject' on tile ${tile.id} ")
            val objectFixtureDefinitions = tile.objects.map { fixtureDefinitionOf(it) }
            if (objectFixtureDefinitions.isEmpty()) gdxError("No collision shapes defined for tile ${tile.id}")
            OBJECT_FIXTURES[GameObject.valueOf(gameObjectStr)] = objectFixtureDefinitions
        }
    }


}
