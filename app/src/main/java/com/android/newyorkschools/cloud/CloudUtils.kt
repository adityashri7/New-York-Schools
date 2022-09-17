package com.android.newyorkschools.cloud

import com.android.newyorkschools.cloud.response.CloudResponse
import com.android.newyorkschools.util.LocalResult
import com.google.gson.Gson


/**
 * Extension function on Gson to parse a network Response into a Kotlin data Class [T] that implements [CloudResponse]
 * Returns the parsed object in a [LocalResult]
 */
fun <T> Gson.parseNetworkResponse(response: Any?, clazz: Class<T>): LocalResult<T> {
    return try {
        val jsonString: String = toJson(response)
        val result: T = fromJson(jsonString, clazz)
        LocalResult.Success(result)
    } catch (e: Exception) {
        LocalResult.Error(e)
    }
}

/**
 * Extension function that converts a [CloudResponse] [CLOUD] response object into a [LOCAL] object
 * Returns the parsed object in a [LocalResult]
 */
fun <CLOUD : CloudResponse<LOCAL>, LOCAL> LocalResult<CLOUD>.toAppModel(): LocalResult<LOCAL> {
    return when (this) {
        is LocalResult.Error -> LocalResult.Error(throwable)
        is LocalResult.Success -> {
            LocalResult.Success(
                data.toAppModel()
            )
        }
    }
}

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