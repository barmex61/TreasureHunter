package com.libgdx.treasurehunter.enums

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Disposable
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.assets.load

enum class TextureAtlasAssets(val path : String) {
    GAMEOBJECT("graphics/gameObjects.atlas")
}

enum class MapAssets(val path : String){
    TUTORIAL("map/tutorial.tmx"),
    OBJECT("map/objects.tmx")
}

enum class ShaderAsset(val vertexShader : String,val fragmentShader : String){
    FLASH("shaders/default.vert","shaders/mapDisplayMode.frag")
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
        ShaderAsset.entries.forEach { shaderAsset->
            assetManager.load<ShaderProgram>(shaderAsset.name,ShaderProgramParameter().apply {
                vertexFile = shaderAsset.vertexShader
                fragmentFile = shaderAsset.fragmentShader
            })
        }
        assetManager.finishLoading()
        val shaderErrors = ShaderAsset.entries
            .map { it to this[it] }
            .filterNot { (_, shader) -> shader.isCompiled }
            .map { (shaderAsset, failedShader) ->
                "Shader $shaderAsset failed to compile: ${failedShader.log}"
            }
        if (shaderErrors.isNotEmpty()) {
            gdxError("Shader compilation errors:\n ${shaderErrors.joinToString("\n\n\n")}")
        }
    }

    operator fun get(textureAtlasAssets: TextureAtlasAssets) : TextureAtlas {
        return assetManager.get(textureAtlasAssets.path)
    }

    operator fun get(mapAsset : MapAssets) : TiledMap {
        return assetManager.get(mapAsset.path)
    }

    operator fun get(shaderAsset: ShaderAsset) : ShaderProgram {
        return assetManager.getAsset(shaderAsset.name)
    }


    operator fun minusAssign(mapAsset: MapAssets){
        assetManager.unload(mapAsset.path)
    }

    override fun dispose() {
        assetManager.disposeSafely()
    }
}

