package dev.maplesyrup.suspiciouscraftable.data

import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack

interface SuspiciousMarkerData {
    fun `suspiciouscraftable$getItem`(): ItemStack
    fun `suspiciouscraftable$setItem`(stack: ItemStack)
    fun `suspiciouscraftable$getBlockPos`(): BlockPos
    fun `suspiciouscraftable$setBlockPos`(pos: BlockPos)
}