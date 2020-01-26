package dev.polek.episodetracker.common.resources

expect object Resource {
    fun read(path: String): String?
}
