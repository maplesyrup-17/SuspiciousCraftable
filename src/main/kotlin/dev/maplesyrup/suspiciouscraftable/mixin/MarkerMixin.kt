
package dev.maplesyrup.suspiciouscraftable.mixin

import dev.maplesyrup.suspiciouscraftable.data.SuspiciousMarkerData
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Marker
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Marker::class)
abstract class MarkerMixin : SuspiciousMarkerData {

    @Unique
    private var `suspiciouscraftable$item`: ItemStack = ItemStack.EMPTY

    @Unique
    private var `suspiciouscraftable$blockX`: Int = 0

    @Unique
    private var `suspiciouscraftable$blockY`: Int = 0

    @Unique
    private var `suspiciouscraftable$blockZ`: Int = 0

    override fun `suspiciouscraftable$getItem`(): ItemStack {
        return `suspiciouscraftable$item`
    }

    override fun `suspiciouscraftable$setItem`(stack: ItemStack) {
        `suspiciouscraftable$item` = stack.copy()
    }

    override fun `suspiciouscraftable$getBlockPos`(): BlockPos {
        return BlockPos(
            `suspiciouscraftable$blockX`,
            `suspiciouscraftable$blockY`,
            `suspiciouscraftable$blockZ`
        )
    }

    override fun `suspiciouscraftable$setBlockPos`(pos: BlockPos) {
        `suspiciouscraftable$blockX` = pos.x
        `suspiciouscraftable$blockY` = pos.y
        `suspiciouscraftable$blockZ` = pos.z
    }

    @Inject(
        method = ["addAdditionalSaveData"],
        at = [At("TAIL")]
    )
    private fun `suspiciouscraftable$saveData`(
        output: ValueOutput,
        ci: CallbackInfo
    ) {
        val stack = `suspiciouscraftable$item`

        if (!stack.isEmpty) {
            output.store("SuspiciousCraftableItem", ItemStack.CODEC, stack)
        }

        output.putInt("SuspiciousCraftableX", `suspiciouscraftable$blockX`)
        output.putInt("SuspiciousCraftableY", `suspiciouscraftable$blockY`)
        output.putInt("SuspiciousCraftableZ", `suspiciouscraftable$blockZ`)
    }

    @Inject(
        method = ["readAdditionalSaveData"],
        at = [At("TAIL")]
    )
    private fun `suspiciouscraftable$loadData`(
        input: ValueInput,
        ci: CallbackInfo
    ) {
        `suspiciouscraftable$item` = input.read("SuspiciousCraftableItem", ItemStack.CODEC).orElse(ItemStack.EMPTY)

        `suspiciouscraftable$blockX` = input.getIntOr("SuspiciousCraftableX", 0)

        `suspiciouscraftable$blockY` = input.getIntOr("SuspiciousCraftableY", 0)

        `suspiciouscraftable$blockZ` = input.getIntOr("SuspiciousCraftableZ", 0)
    }
}
