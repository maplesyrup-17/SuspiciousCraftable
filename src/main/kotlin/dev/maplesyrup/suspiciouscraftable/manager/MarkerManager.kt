package dev.maplesyrup.suspiciouscraftable.manager

import dev.maplesyrup.suspiciouscraftable.data.SuspiciousMarkerData
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.Marker
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.AABB

object MarkerManager {

    private fun find(level: ServerLevel, pos: BlockPos): Marker? {
        val searchArea = AABB.encapsulatingFullBlocks(pos, pos)

        return level.getEntitiesOfClass(
            Marker::class.java,
            searchArea
        ) { marker ->
            val data = marker as? SuspiciousMarkerData

            data != null &&
                    data.`suspiciouscraftable$getBlockPos`() == pos
        }.firstOrNull()
    }

    fun store(level: ServerLevel, pos: BlockPos, stack: ItemStack) {
        if (stack.isEmpty) {
            remove(level, pos)
            return
        }

        val existing = find(level, pos)
        if (existing != null) {
            (existing as SuspiciousMarkerData)
                .`suspiciouscraftable$setItem`(stack.copy())
            return
        }

        val marker = EntityTypes.MARKER.create(level, EntitySpawnReason.EVENT)
            ?: return

        marker.setPos(
            pos.x + 0.5,
            pos.y.toDouble(),
            pos.z + 0.5
        )

        val data = marker as SuspiciousMarkerData
        data.`suspiciouscraftable$setItem`(stack.copy())
        data.`suspiciouscraftable$setBlockPos`(pos)

        level.addFreshEntity(marker)
    }

    fun retrieve(level: ServerLevel, pos: BlockPos): ItemStack {
        val marker = find(level, pos) ?: return ItemStack.EMPTY
        return (marker as SuspiciousMarkerData)
            .`suspiciouscraftable$getItem`()
            .copy()
    }

    fun remove(level: ServerLevel, pos: BlockPos) {
        find(level, pos)?.discard()
    }
}