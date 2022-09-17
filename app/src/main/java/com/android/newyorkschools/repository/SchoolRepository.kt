package com.android.newyorkschools.repository

import com.android.newyorkschools.cloud.service.SchoolWebService
import com.android.newyorkschools.model.School
import com.android.newyorkschools.model.SchoolScore
import com.android.newyorkschools.util.LocalDispatchers
import com.android.newyorkschools.util.LocalResult
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SchoolRepository @Inject constructor(
    private val dispatchers: LocalDispatchers,
    private val schoolWebService: SchoolWebService
) {

    suspend fun getSchools(): LocalResult<List<School>> = withContext(dispatchers.default) {
        schoolWebService.getSchools()
    }

    suspend fun getSchoolScores(dbn: String): LocalResult<SchoolScore> =
        withContext(dispatchers.default) {
            schoolWebService.getScores(dbn)
        }
}