package dev.maplesyrup.suspiciouscraftable.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class SuspiciousCraftingRecipe(
    private val base: String
) : CustomRecipe() {

    companion object {
        val CODEC: MapCodec<SuspiciousCraftingRecipe> =
            RecordCodecBuilder.mapCodec { instance ->
                instance.group(Codec.STRING.fieldOf("base").forGetter { it.base }).apply(instance, ::SuspiciousCraftingRecipe)
            }
        val STREAM_CODEC: StreamCodec<
                RegistryFriendlyByteBuf,
                SuspiciousCraftingRecipe
                > = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, { it.base }, ::SuspiciousCraftingRecipe
        )
        val SERIALIZER =
            RecipeSerializer(CODEC, STREAM_CODEC)
    }

    private fun isValidContainedItem(stack: ItemStack): Boolean {
        return !stack.isEmpty && !(stack.`is`(Items.SAND) || stack.`is`(Items.SUSPICIOUS_SAND) || stack.`is`(Items.GRAVEL) || stack.`is`(Items.SUSPICIOUS_GRAVEL))
    }

    private fun getResultItem(): net.minecraft.world.item.Item {
        return if (base == "sand") {
            Items.SUSPICIOUS_SAND
        } else {
            Items.SUSPICIOUS_GRAVEL
        }
    }

    override fun matches(
        input: CraftingInput,
        level: Level
    ): Boolean {
        val stacks = input.items().filterNot { it.isEmpty }
        if (stacks.size != 2) return false
        val baseStack = stacks.firstOrNull {
            if (base == "sand") {
                it.`is`(Items.SAND)
            } else {
                it.`is`(Items.GRAVEL)
            }
        } ?: return false
        val containedStack = stacks.firstOrNull {
            it !== baseStack
        } ?: return false

        return isValidContainedItem(containedStack)
    }

    override fun assemble(input: CraftingInput): ItemStack {
        val stacks = input.items().filterNot { it.isEmpty }
        if (stacks.size != 2) return ItemStack.EMPTY
        val baseStack = stacks.firstOrNull {
            if (base == "sand") {
                it.`is`(Items.SAND)
            } else {
                it.`is`(Items.GRAVEL)
            }
        } ?: return ItemStack.EMPTY
        val containedStack = stacks.firstOrNull {
            it !== baseStack
        } ?: return ItemStack.EMPTY
        if (!isValidContainedItem(containedStack)) {
            return ItemStack.EMPTY
        }
        val result = ItemStack(getResultItem())

        result.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(listOf(containedStack.copyWithCount(1))))

        return result
    }

    override fun getSerializer(): RecipeSerializer<out CustomRecipe> {
        return SERIALIZER
    }
}