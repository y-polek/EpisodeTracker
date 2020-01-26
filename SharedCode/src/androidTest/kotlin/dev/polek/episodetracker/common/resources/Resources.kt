package dev.polek.episodetracker.common.resources

import java.io.File

actual object Resource {
    actual fun read(path: String): String? {
        val file = File("src/commonTest/resources/$path")
        return if (file.exists()) file.readText() else null
    }
}
