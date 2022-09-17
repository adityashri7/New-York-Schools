package com.android.newyorkschools.cloud.response;

import java.io.Serializable;

abstract public class CloudResponse<T> implements Serializable {
    public abstract T toAppModel();
}
