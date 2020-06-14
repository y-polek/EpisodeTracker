package dev.polek.episodetracker.common.utils

import platform.darwin.os_log_is_debug_enabled
import platform.darwin.os_log_t

actual val DEBUG_BUILD: Boolean = os_log_is_debug_enabled(os_log_t())
