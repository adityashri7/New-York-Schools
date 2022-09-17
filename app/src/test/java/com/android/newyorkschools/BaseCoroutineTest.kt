package com.android.newyorkschools

import com.android.newyorkschools.util.LocalDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * A base class for JUnit5 tests testing code that relies on coroutines using MockK. This class:
 * Sets the testDispatcherProvider used during unit tests
 */
@ExperimentalCoroutinesApi
abstract class BaseCoroutineTest {
    private var dispatcher = UnconfinedTestDispatcher()
    var testDispatcherProvider = object : LocalDispatchers {
        override val IO: CoroutineDispatcher = dispatcher
        override val Main: CoroutineDispatcher = dispatcher
        override val Default: CoroutineDispatcher = dispatcher
    }
}