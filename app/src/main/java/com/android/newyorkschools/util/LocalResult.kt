package com.android.newyorkschools.util

sealed class LocalResult<out D> {
    data class Success<out D>(val data: D) : LocalResult<D>()
    data class Error(val throwable: Throwable?) : LocalResult<Nothing>()
}