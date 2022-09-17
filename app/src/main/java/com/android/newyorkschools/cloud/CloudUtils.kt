package com.android.newyorkschools.cloud

import com.android.newyorkschools.util.LocalResult

/**
 * Wraps a code [block] that returns a [LocalResult] in a try catch block and returns a [LocalResult.Error] on en exception
 */
inline fun <T, R> T.catchLocalErrors(block: T.() -> LocalResult<R>): LocalResult<R> {
    return try {
        block()
    } catch (e: Throwable) {
        LocalResult.Error(e)
    }
}