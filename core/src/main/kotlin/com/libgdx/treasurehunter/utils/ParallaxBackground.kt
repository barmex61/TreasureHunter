import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import ktx.assets.disposeSafely
import ktx.math.vec2

class ParallaxBackground(
    private val gameViewport: Viewport,
    bgdTexturePath: String,
    private val scrollSpeed: Vector2 = vec2(1f, 1f),
    private val scale: Float = UNIT_SCALE,
) : Disposable {
    private val originUV = vec2(0f, 0f)
    private val originUV2 = vec2(0f, 0f)
    private var texture = wrappedTexture(bgdTexturePath)

    private var bgdSprite = Sprite(texture).apply {
        resize(gameViewport.worldWidth, gameViewport.worldHeight, scale)
        setOriginCenter()
    }

    private fun wrappedTexture(internalPath: String) = Texture(internalPath).apply {
        setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }

    // background sprite that fills out the entire viewport
    private fun Sprite.resize(worldWidth: Float, worldHeight: Float, scale: Float = UNIT_SCALE) {
        setSize(worldWidth, worldHeight)
        // tile the texture over the entire background by keeping its original aspect ratio
        originUV.set(u, v)
        originUV2.set(
            (worldWidth / (texture.width * scale)),
            (worldHeight / (texture.height * scale))
        )
        u2 = originUV2.x
        v2 = originUV2.y
    }

    fun scrollBy(amountX: Float, amountY: Float) {
        bgdSprite.u += amountX * scrollSpeed.x
        bgdSprite.u2 += amountX * scrollSpeed.x
        bgdSprite.v += amountY * scrollSpeed.y
        bgdSprite.v2 += amountY * scrollSpeed.y
    }

    fun scrollTo(scrollX: Float, scrollY: Float) {
        bgdSprite.u = originUV.x + scrollX * scrollSpeed.x
        bgdSprite.u2 = originUV2.x + scrollX * scrollSpeed.x
        bgdSprite.v = originUV.y + scrollY * scrollSpeed.y
        bgdSprite.v2 = originUV2.y + scrollY * scrollSpeed.y
    }

    fun draw(x: Float, y: Float, batch: Batch) {
        bgdSprite.setPosition(x, y)

        bgdSprite.draw(batch)
    }

    fun setTexture(texturePath: String) {
        texture = wrappedTexture(texturePath)
        bgdSprite = Sprite(texture).apply {
            resize(gameViewport.worldWidth, gameViewport.worldHeight, scale)
        }
    }

    override fun dispose() {
        texture.disposeSafely()
    }
}
