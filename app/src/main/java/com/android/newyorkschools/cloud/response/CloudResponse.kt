package com.android.newyorkschools.cloud.response

import java.io.Serializable

abstract class CloudResponse<T> : Serializable {
    abstract fun toAppModel(): T
}