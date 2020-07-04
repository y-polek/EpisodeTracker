package dev.polek.episodetracker.common.analytics

interface Analytics {

    fun logShare(text: String) {
        logEvent("event_share") {
            param("event_share", text)
        }
    }

    fun logEvent(name: String, params: List<Param>)

    private inline fun logEvent(name: String, init: MutableList<Param>.() -> Unit) {
        val params = mutableListOf<Param>()
        params.init()
        logEvent(name, params)
    }


    sealed class Param constructor(val key: String) {
        class StringParam(key: String, val value: String) : Param(key)
        class LongParam(key: String, val value: Long) : Param(key)
        class DoubleParam(key: String, val value: Double) : Param(key)
    }

    private companion object {

        private fun MutableList<Param>.param(key: String, value: String) {
            add(Param.StringParam(key, value))
        }

        private fun MutableList<Param>.param(key: String, value: Long) {
            add(Param.LongParam(key, value))
        }

        private fun MutableList<Param>.param(key: String, value: Double) {
            add(Param.DoubleParam(key, value))
        }
    }
}