package com.android.newyorkschools.cloud.api

import com.android.newyorkschools.cloud.response.SchoolResponse
import com.android.newyorkschools.cloud.response.SchoolScoreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SchoolApi {
    @GET
    fun getEmployees(@Url endpoint: String): Call<List<SchoolResponse>?>

    @GET
    fun getScores(@Url endpoint: String, @Query("dbn") dbn: String): Call<List<SchoolScoreResponse>?>
}

open class SchoolEndpoints {
    open val schools = "resource/s3k6-pzi2.json"
    open val scores = "resource/f9bf-2cp4.json"
}