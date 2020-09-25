package dev.polek.episodetracker.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dev.polek.episodetracker.common.analytics.Analytics

class AnalyticsImpl(private val analytics: FirebaseAnalytics) : Analytics() {

    override fun logEvent(name: String, params: List<Param>) {
        analytics.logEvent(name) {
            params.forEach { param ->
                when (param) {
                    is Param.StringParam -> param(param.key, param.value)
                    is Param.LongParam -> param(param.key, param.value)
                    is Param.DoubleParam -> param(param.key, param.value)
                    else -> throw UnsupportedOperationException("Parameter of unknown type: $param")
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String) {
        analytics.setUserProperty(name, value)
    }

    override fun removeUserProperty(name: String) {
        analytics.setUserProperty(name, null)
    }
}
