package dev.maplesyrup.suspiciouscraftable.mixin

import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponents
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BrushableBlockEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(BlockEntity::class)
abstract class BlockEntityMixin {

    @Inject(method = ["applyImplicitComponents"], at = [At("TAIL")])
    private fun suspiciouscraftableApplyContainedItem(
        components: DataComponentGetter,
        ci: CallbackInfo
    ) {
        val blockEntity = runCatching {
            BrushableBlockEntity::class.java.cast(this)
        }.getOrNull() ?: return

        val contents = components.get(DataComponents.CONTAINER) ?: return
        val containedItem = contents.copyOne()
        if (containedItem.isEmpty) return
        val accessor = blockEntity as BrushableBlockEntityAccessor

        accessor.`suspiciouscraftable$setItem`(containedItem)
        accessor.`suspiciouscraftable$setLootTable`(null)

        blockEntity.setChanged()
    }
}