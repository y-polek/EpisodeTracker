package dev.polek.episodetracker.common.utils

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

fun String.emptyToNull(): String? = if (this.isEmpty()) null else this
fun String.blankToNull(): String? = if (this.isBlank()) null else this

fun string(res: StringResource): StringDesc = StringDesc.Resource(res)
fun string(res: StringResource, args: List<Any>): StringDesc = StringDesc.ResourceFormatted(res, args)
