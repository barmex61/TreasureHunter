package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ui.model.InventoryModel
import ktx.scene2d.KTable
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.verticalGroup
import ktx.scene2d.stack

data class InventorySlot(
    var itemType: ItemType? = null,
    var count: Int = 0,
    val itemImage: Image,
    val countImage: Image,
    val index: Int
)

class InventoryView(
    skin: Skin,
    inventoryModel: InventoryModel,
) : Table(skin), KTable {

    private val itemImages = mutableListOf<Image>()
    private val itemCountImages = mutableListOf<Image>()

    private val slotCount = 25


    init {
        setFillParent(true)

        verticalGroup {

            fill()
            container {
                prefWidth(200.0f)
                prefHeight(45.0f)
                stack {
                    image("prefabs_4_ninepatch")
                    horizontalGroup {
                        align(Align.center)
                        image("big_text_i")
                        image("big_text_n")
                        image("big_text_v")
                        image("big_text_e")
                        image("big_text_n")
                        image("big_text_t")
                        image("big_text_o")
                        image("big_text_r")
                        image("big_text_y")
                    }
                }
            }
            stack {
                image("prefabs_6")
                verticalGroup {
                    pad(20f)
                    repeat(5) {
                        horizontalGroup {
                            repeat(5) {
                                container {
                                    prefWidth(36.0f)
                                    prefHeight(32.0f)
                                    padLeft(2.0f)
                                    background(skin.getDrawable("text_bubble"))
                                    stack {
                                        container {
                                            align(Align.bottomRight)
                                            prefSize(8.0f)
                                            padRight(2.0f)
                                            padBottom(2.0f)
                                            val itemCountImage = image(null){
                                                setScaling(Scaling.none)
                                            }
                                            this@InventoryView.itemCountImages.add(itemCountImage)
                                        }
                                        val itemImage = image(null) {
                                            setScaling(Scaling.none)
                                        }
                                        this@InventoryView.itemImages.add(itemImage)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        verticalGroup {
            container {
                prefWidth(145.0f)
                prefHeight(45.0f)
                stack {
                    image("prefabs_4_ninepatch")
                    horizontalGroup {
                        align(Align.center)
                        image("big_text_g")
                        image("big_text_e")
                        image("big_text_a")
                        image("big_text_r")
                    }
                }
            }
            stack {
                image("prefabs_5")
                verticalGroup {
                    padTop(17f)
                    padBottom(17f)
                    space(-4f)
                    container {
                        background(skin.getDrawable("text_bubble"))
                        prefWidth(36f)
                        prefHeight(32f)
                        image("helmet_off") {
                            setScaling(Scaling.none)
                        }
                    }
                    horizontalGroup {
                        space(-4f)
                        container {
                            background(skin.getDrawable("text_bubble"))
                            prefHeight(32f)
                            prefWidth(36f)
                            image("sword_off") {
                                setScaling(Scaling.none)
                            }
                        }
                        container {
                            background(skin.getDrawable("text_bubble"))
                            prefHeight(32f)
                            prefWidth(36f)
                            image("armor_off") {
                                setScaling(Scaling.none)
                            }
                        }
                    }
                    container {
                        background(skin.getDrawable("text_bubble"))
                        prefWidth(36f)
                        prefHeight(32f)
                        image("boots_off") {
                            setScaling(Scaling.none)
                        }
                    }
                }
            }
        }
        inventoryModel.onItemChange = { items ->
            updateInventory(items)
        }

    }
    private val slots: MutableList<InventorySlot> by lazy {
        MutableList(slotCount) { i ->
            InventorySlot(
                itemType = null,
                count = 0,
                itemImage = itemImages[i],
                countImage = itemCountImages[i],
                index = i
            )
        }
    }

    private fun updateInventory(items: List<ItemData>) {
        slots.forEach {
            it.itemType = null
            it.count = 0
        }

        val grouped = items.groupBy { it.itemType }
        var slotIndex = 0
        for ((itemType, group) in grouped) {
            if (slotIndex >= slots.size) break
            val slot = slots[slotIndex]
            slot.itemType = itemType
            slot.count = group.size
            slot.itemImage.drawable = skin.getDrawable(itemType.toDrawablePath())
            slot.countImage.drawable = skin.getDrawable("big_text_${group.size}")
            slotIndex++
        }

        for (i in slotIndex until slots.size) {
            val slot = slots[i]
            slot.itemType = null
            slot.count = 0
            slot.itemImage.drawable = null
            slot.countImage.drawable = null
        }
    }

        override fun draw(batch: Batch?, parentAlpha: Float) {
            val oldColor = batch!!.color.cpy()
            super.draw(batch, parentAlpha)
            batch.color = oldColor
        }
    }


