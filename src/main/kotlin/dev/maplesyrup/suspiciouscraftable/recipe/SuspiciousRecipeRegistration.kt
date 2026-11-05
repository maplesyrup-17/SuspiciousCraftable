package dev.maplesyrup.suspiciouscraftable.recipe

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier

object SuspiciousRecipeRegistration {
    fun register() {
        Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath("suspiciouscraftable", "suspicious_crafting"), SuspiciousCraftingRecipe.SERIALIZER)
    }
}