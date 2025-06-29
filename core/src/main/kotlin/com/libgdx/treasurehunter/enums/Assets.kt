package com.libgdx.treasurehunter.enums

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader.ShaderProgramParameter
import com.badlogic.gdx.assets.loaders.SkinLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.assets.load

enum class SoundAsset(val path: String, val volume: Float = 1f) {
    PLAYER_JUMP("sounds/player_jump.mp3"),
    CRABBY_JUMP("sounds/crabby_jump.mp3"),
    BUTTON_CLICK("sounds/button_click.mp3"),
    CRABBY_HURT("sounds/crabby_hurt.mp3"),
    FIERCE_TOOTH_FOOTSTEP("sounds/fierce_tooth_footstep.mp3"),
    FIERCE_TOOTH_HURT("sounds/fierce_tooth_hurt.mp3"),
    FIERCE_TOOTH_JUMP("sounds/fierce_tooth_jump.mp3"),
    PINK_STAR_HURT("sounds/pink_star_hurt.mp3"),
    PINK_STAR_JUMP("sounds/pink_star_jump.mp3"),
    PLAYER_FOOTSTEP("sounds/player_footstep.mp3"),
    PLAYER_HURT("sounds/player_hurt.mp3"),
    SWORD_SWING("sounds/sword_swing.mp3"),
    TOTEM_HEAD_HURT("sounds/totem_head_hurt.mp3"),

}

enum class TextureAtlasAssets(val path : String) {
    GAMEOBJECT("graphics/gameObjects.atlas")
}

enum class MapAssets(val path : String){
    TUTORIAL("map/tutorial.tmx"),
    MAP1("map/map1.tmx"),
    OBJECT("map/objects.tmx")
}

enum class ShaderAsset(val vertexShader : String,val fragmentShader : String){
    FLASH("shaders/default.vert","shaders/mapDisplayMode.frag")
}

enum class SkinAsset(val path : String){
    DEFAULT("graphics/ui/skin.json")
}

class AssetHelper : Disposable{

    private val assetManager by lazy {
        AssetManager().apply {
            setLoader(TiledMap::class.java, TmxMapLoader())
            setLoader(Skin::class.java, SkinLoader(fileHandleResolver))
        }
    }

    fun loadAll(){
        TextureAtlasAssets.entries.forEach {
            assetManager.load<TextureAtlas>(it.path)
        }
        MapAssets.entries.forEach {
            assetManager.load<TiledMap>(it.path)
        }
        SkinAsset.entries.forEach {
            assetManager.load<Skin>(it.path)
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

    operator fun get(skinAsset: SkinAsset) : Skin {
        return assetManager.get(skinAsset.path)
    }


    operator fun minusAssign(mapAsset: MapAssets){
        assetManager.unload(mapAsset.path)
    }

    override fun dispose() {
        assetManager.disposeSafely()
    }
}

