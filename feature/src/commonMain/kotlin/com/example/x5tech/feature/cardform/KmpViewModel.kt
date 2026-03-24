package com.example.x5tech.feature.cardform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

open class KmpViewModel internal constructor(
    coroutineScope: CoroutineScope? = null,
) {

    protected val viewModelScope: CoroutineScope = coroutineScope ?: CoroutineScope(
        context = SupervisorJob() + Dispatchers.Default,
    )

    internal fun clear() {
        viewModelScope.cancel()
    }
}
