package com.android.newyorkschools.cloud.service

import com.android.newyorkschools.cloud.api.SchoolApi
import com.android.newyorkschools.cloud.api.SchoolEndpoints
import com.android.newyorkschools.cloud.catchLocalErrors
import com.android.newyorkschools.model.School
import com.android.newyorkschools.util.LocalDispatchers
import com.android.newyorkschools.util.LocalResult
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class SchoolWebService @Inject constructor(
    private val dispatchers: LocalDispatchers,
    private val schoolsApi: SchoolApi,
    private val schoolEndpoints: SchoolEndpoints
) {

    suspend fun getSchools(): LocalResult<List<School>> = withContext(dispatchers.io) {
        catchLocalErrors {
            val schoolsResponse =
                schoolsApi.getEmployees(schoolEndpoints.schools).awaitResponse().body()
            return@catchLocalErrors if (schoolsResponse != null) {
                return@withContext try {
                    LocalResult.Success(schoolsResponse.map { it.toAppModel() })
                } catch (e: Exception) {
                    LocalResult.Error(e)
                }
            } else {
                LocalResult.Error(Throwable("Schools Response is null"))
            }
        }
    }
}