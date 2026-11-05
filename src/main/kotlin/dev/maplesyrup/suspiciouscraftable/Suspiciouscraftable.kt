package dev.maplesyrup.suspiciouscraftable

import dev.maplesyrup.suspiciouscraftable.recipe.SuspiciousRecipeRegistration
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

class Suspiciouscraftable : ModInitializer {

    val MOD_ID = "suspiciouscraftable"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        LOGGER.info("Initialized Suspicious Craftables")
        SuspiciousRecipeRegistration.register()
    }
}