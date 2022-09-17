package com.android.newyorkschools.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface LocalDispatchers {
    val IO: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val Default: CoroutineDispatcher
}

class AppDispatchers : com.android.newyorkschools.util.LocalDispatchers {
    override val IO = Dispatchers.IO
    override val Main = Dispatchers.Main
    override val Default = Dispatchers.Default
}