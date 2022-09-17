package com.android.newyorkschools.cloud.api

import com.android.newyorkschools.cloud.response.GetSchoolsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface SchoolApi {
    @GET
    fun getEmployees(@Url endpoint: String?): Call<List<GetSchoolsResponse.School>?>
}

open class SchoolEndpoints {
    open val schools = "resource/s3k6-pzi2.json"
}