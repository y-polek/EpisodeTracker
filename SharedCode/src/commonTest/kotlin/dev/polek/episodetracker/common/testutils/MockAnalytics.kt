package dev.polek.episodetracker.common.testutils

import dev.polek.episodetracker.common.analytics.Analytics

class MockAnalytics : Analytics() {

    override fun logEvent(name: String, params: List<Param>) {
        /* no-op */
    }

    override fun setUserProperty(name: String, value: String) {
        /* no-op */
    }

    override fun removeUserProperty(name: String) {
        /* no-op */
    }
}
