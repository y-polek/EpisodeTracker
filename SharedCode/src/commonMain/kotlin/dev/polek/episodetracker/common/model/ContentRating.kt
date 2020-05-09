package dev.polek.episodetracker.common.model

enum class ContentRating(val abbr: String, val info: String) {
    TV_Y("TV-Y", "This program is designed to be appropriate for all children."),
    TV_Y7("TV-Y7", "This program is designed for children age 7 and above."),
    TV_G("TV-G", "This program is suitable for all ages."),
    TV_PG("TV-PG", "This program contains material that parents may find unsuitable for younger children."),
    TV_14("TV-14", "This program contains some material that many parents would find unsuitable for children under 14 years of age."),
    TV_MA("TV-MA", "This program is specifically designed to be viewed by adults and therefore may be unsuitable for children under 17.");

    companion object {
        fun find(name: String): ContentRating? {
            return values().find { name.equals(it.abbr, ignoreCase = true) }
        }
    }
}
