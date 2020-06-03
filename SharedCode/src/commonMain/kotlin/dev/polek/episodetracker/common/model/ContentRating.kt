package dev.polek.episodetracker.common.model

import dev.icerock.moko.resources.StringResource
import dev.polek.episodetracker.MR

enum class ContentRating(val abbr: String, val info: StringResource) {
    TV_Y("TV-Y", MR.strings.rating_info_tv_y),
    TV_Y7("TV-Y7", MR.strings.rating_info_tv_y7),
    TV_G("TV-G", MR.strings.rating_info_tv_g),
    TV_PG("TV-PG", MR.strings.rating_info_tv_pg),
    TV_14("TV-14", MR.strings.rating_info_tv_14),
    TV_MA("TV-MA", MR.strings.rating_info_tv_ma);

    companion object {
        fun find(name: String): ContentRating? {
            return values().find { name.equals(it.abbr, ignoreCase = true) }
        }
    }
}
