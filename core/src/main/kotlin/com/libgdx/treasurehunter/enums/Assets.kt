package com.libgdx.treasurehunter.enums

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Disposable
import ktx.assets.disposeSafely
import ktx.assets.load

enum class TextureAtlasAssets(val path : String) {
    GAMEOBJECT("graphics/gameObjects.atlas")
}

enum class MapAssets(val path : String){
    TUTORIAL("map/tutorial.tmx"),
    OBJECT("map/objects.tmx")
}

class AssetHelper : Disposable{

    private val assetManager by lazy {
        AssetManager().apply {
            setLoader(TiledMap::class.java, TmxMapLoader())
        }
    }

    fun loadAll(){
        TextureAtlasAssets.entries.forEach {
            assetManager.load<TextureAtlas>(it.path)
        }
        MapAssets.entries.forEach {
            assetManager.load<TiledMap>(it.path)
        }
        assetManager.finishLoading()
    }

    operator fun get(textureAtlasAssets: TextureAtlasAssets) : TextureAtlas {
        return assetManager.get(textureAtlasAssets.path)
    }

    operator fun get(mapAsset : MapAssets) : TiledMap {
        return assetManager.get(mapAsset.path)
    }

    operator fun minusAssign(mapAsset: MapAssets){
        assetManager.unload(mapAsset.path)
    }

    override fun dispose() {
        assetManager.disposeSafely()
    }
}

