package dev.polek.episodetracker.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dev.polek.episodetracker.common.analytics.Analytics

class AnalyticsImpl(private val analytics: FirebaseAnalytics) : Analytics {

    override fun logEvent(name: String, params: List<Analytics.Param>) {
        analytics.logEvent(name) {
            params.forEach { param ->
                when (param) {
                    is Analytics.Param.StringParam -> param(param.key, param.value)
                    is Analytics.Param.LongParam -> param(param.key, param.value)
                    is Analytics.Param.DoubleParam -> param(param.key, param.value)
                    else -> throw UnsupportedOperationException("Parameter of unknown type: $param")
                }
            }
        }
    }
}
