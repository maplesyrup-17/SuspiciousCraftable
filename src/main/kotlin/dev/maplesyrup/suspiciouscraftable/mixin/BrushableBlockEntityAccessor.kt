package dev.maplesyrup.suspiciouscraftable.mixin

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BrushableBlockEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable

@Mixin(BrushableBlockEntity::class)
interface BrushableBlockEntityAccessor {

    @Accessor("item")
    fun `suspiciouscraftable$getItem`(): ItemStack

    @Accessor("item")
    fun `suspiciouscraftable$setItem`(item: ItemStack)

    @Accessor("lootTable")
    fun `suspiciouscraftable$setLootTable`(lootTable: ResourceKey<LootTable>?)
}