package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.libgdx.treasurehunter.ecs.components.Armor
import com.libgdx.treasurehunter.ecs.components.Boots
import com.libgdx.treasurehunter.ecs.components.Helmet
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.SlotName
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.InventoryModel
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.verticalGroup
import ktx.scene2d.stack
import kotlin.collections.get
import kotlin.toString

data class InventorySlot(
    var itemData: ItemData? = null,
    var count: Int = 0,
    var itemImage: Image?,
    var countImage: Image?,
    val index: Int,
)

data class GearSlot(
    var itemData: ItemData? = null,
    var isEquipped: Boolean = false,
    var image: Image? = null,
)

class InventoryView(
    skin: Skin,
    inventoryModel: InventoryModel,
) : Table(skin), KTable {


    private val inventorySlotCount = 25
    private val inventorySlots: MutableList<InventorySlot> by lazy {
        MutableList(inventorySlotCount) { i ->
            InventorySlot(
                itemData = null,
                count = 0,
                itemImage = null,
                countImage = null,
                index = i
            )
        }
    }

    private val gearSlots = arrayOf(
        GearSlot(),
        GearSlot(),
        GearSlot(),
        GearSlot()
    )

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
                    repeat(5) { xIndex ->
                        horizontalGroup {
                            repeat(5) { yIndex ->
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
                                            val itemCountImage = image(null) {
                                                setScaling(Scaling.none)
                                            }
                                            this@InventoryView.inventorySlots[xIndex * 5 + yIndex].countImage =
                                                itemCountImage.also {
                                                    it.onClick {
                                                        this@InventoryView.onInventoryItemClick(this@InventoryView.inventorySlots[xIndex * 5 + yIndex])
                                                        false
                                                    }
                                                }
                                        }
                                        val itemImage = image(null) {
                                            setScaling(Scaling.none)
                                        }
                                        this@InventoryView.inventorySlots[xIndex * 5 + yIndex].itemImage =
                                            itemImage.also {
                                                it.onClick {
                                                    this@InventoryView.onInventoryItemClick(this@InventoryView.inventorySlots[xIndex * 5 + yIndex])
                                                    false
                                                }
                                            }
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
                    addActor(this@InventoryView.gearSlot("helmet_off", 0))
                    horizontalGroup {
                        space(-4f)
                        addActor(this@InventoryView.gearSlot("sword_off", 1))
                        addActor(this@InventoryView.gearSlot("armor_off", 2))
                    }
                    addActor(this@InventoryView.gearSlot("boots_off", 3))
                }
            }
        }
        inventoryModel.onItemChange = { items ->
            updateInventorySlotsFromModel(items)
        }
        inventoryModel.onEquippedItemChange = { slotName, itemData ->
            updateGearSlotsFromModel(slotName,itemData)
        }
    }

    private fun updateInventorySlotsFromModel(items : List<ItemData>){
        resetInventorySlots()
        val groupedItems = items.groupBy { it.itemType }
        for ((itemType, itemList) in groupedItems) {
            val slotIndex = inventorySlots.indexOfFirst { it.itemData == null }
            if (slotIndex != -1) {
                val inventorySlot = inventorySlots[slotIndex]
                inventorySlot.itemData = itemList.first()
                inventorySlot.count = itemList.size
                inventorySlot.itemImage?.drawable = skin.getDrawable(itemType.toDrawablePath())
                inventorySlot.countImage?.drawable = skin.getDrawable("big_text_${inventorySlot.count}")
            }
        }
    }

    private fun updateGearSlotsFromModel(slotName: String, itemData: ItemData?) {
        val slotMap = listOf(
            SlotName.HELMET.toString() to "helmet_off",
            SlotName.SWORD.toString() to "sword_off",
            SlotName.ARMOR.toString() to "armor_off",
            SlotName.BOOTS.toString() to "boots_off"
        )
        val index = slotMap.indexOfFirst { it.first == slotName }
        if (index != -1) {
            gearSlots[index].apply {
                isEquipped = itemData != null
                this.itemData = itemData
                image?.drawable = skin.getDrawable(itemData?.itemType?.toDrawablePath() ?: slotMap[index].second)
            }
        }
    }

    private fun resetInventorySlots(){
        inventorySlots.forEach {
            it.itemData = null
            it.count = 0
            it.itemImage?.drawable = null
            it.countImage?.drawable = null
        }
    }

    private fun gearSlot(slotName: String, index: Int) =
        container {
            background(this@InventoryView.skin.getDrawable("text_bubble"))
            prefWidth(36f)
            prefHeight(32f)
            val slotImage = image(slotName) {
                setScaling(Scaling.none)
                onClick {
                    this@InventoryView.onGearItemClick(this@InventoryView.gearSlots[index])
                    false
                }
            }
            this@InventoryView.gearSlots[index].image = slotImage
        }

    private fun onInventoryItemClick(slot: InventorySlot) {
        if (slot.itemData == null) return
        when(slot.itemData?.itemType){
            is ItemType.Equippable -> {
                GameEventDispatcher.fireEvent(GameEvent.EquipItemRequest(
                    when(slot.itemData?.itemType) {
                        is Sword -> SlotName.SWORD.toString()
                        is Helmet -> SlotName.HELMET.toString()
                        is Armor -> SlotName.ARMOR.toString()
                        is Boots -> SlotName.BOOTS.toString()
                        else -> throw IllegalArgumentException("Unknown equippable item type: ${slot.itemData!!.itemType}")
                    },slot.itemData!!
                ))
            }
            is ItemType.Consumable ->{
                GameEventDispatcher.fireEvent(GameEvent.ConsumeItemRequest(slot.itemData!!))
            }
            else -> Unit
        }
    }

    private fun onGearItemClick(slot: GearSlot) {
        if (slot.itemData == null) return
        when(slot.itemData!!.itemType){
            is ItemType.Equippable -> {
                GameEventDispatcher.fireEvent(GameEvent.UnEquipItemRequest(
                    when(slot.itemData?.itemType) {
                        is Sword -> SlotName.SWORD.toString()
                        is Helmet -> SlotName.HELMET.toString()
                        is Armor -> SlotName.ARMOR.toString()
                        is Boots -> SlotName.BOOTS.toString()
                        else -> throw IllegalArgumentException("Unknown equippable item type: ${slot.itemData!!.itemType}")
                    },slot.itemData!!
                ))
            }
            else -> Unit
        }
    }


    override fun draw(batch: Batch?, parentAlpha: Float) {
        val oldColor = batch!!.color.cpy()
        super.draw(batch, parentAlpha)
        batch.color = oldColor
    }
}


