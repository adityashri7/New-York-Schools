package com.android.newyorkschools.cloud

import java.io.Serializable

abstract class CloudResponse<T> : Serializable {
    abstract fun toAppModel(): T
}