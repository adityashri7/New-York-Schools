package com.android.newyorkschools.service

import com.android.newyorkschools.BaseCoroutineTest
import com.android.newyorkschools.BuildConfig
import com.android.newyorkschools.cloud.api.SchoolApi
import com.android.newyorkschools.cloud.api.SchoolEndpoints
import com.android.newyorkschools.cloud.service.SchoolWebService
import com.android.newyorkschools.injection.NetworkModule
import com.android.newyorkschools.model.School
import com.android.newyorkschools.model.SchoolScore
import com.android.newyorkschools.util.LocalResult
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class SchoolWebServiceTest : BaseCoroutineTest() {

    private lateinit var directoryWebService: SchoolWebService

    @Before
    fun before() {
        MockKAnnotations.init(this)
        directoryWebService = SchoolWebService(
            testDispatcherProvider,
            schoolApi,
            successSchoolEndpoints
        )
    }

    @After
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `SchoolWebService Get School Successful Call Returns In Local Result Success`() = runTest {
        val schoolsResult = directoryWebService.getSchools()
        schoolsResult.shouldBeInstanceOf<LocalResult.Success<List<School>>>()
        schoolsResult.data.shouldNotBeEmpty()
    }

    @Test
    fun `SchoolWebService Get Score Successful Call Returns In Local Result Success`() = runTest {
        val scoresResult = directoryWebService.getScores(DBN)
        scoresResult.shouldBeInstanceOf<LocalResult.Success<SchoolScore>>()
        scoresResult.data.dbn shouldBe DBN
    }

    @Test
    fun `SchoolWebService Get School Error Call Returns In Local Result Success`() = runTest {
        directoryWebService = SchoolWebService(
            testDispatcherProvider,
            schoolApi,
            errorSchoolEndpoints
        )
        val schoolsResult = directoryWebService.getSchools()
        schoolsResult.shouldBeInstanceOf<LocalResult.Error>()
        schoolsResult.throwable shouldNotBe null
    }

    @Test
    fun `SchoolWebService Get Score Error Call Returns In Local Result Success`() = runTest {
        directoryWebService = SchoolWebService(
            testDispatcherProvider,
            schoolApi,
            errorSchoolEndpoints
        )
        val scoresResult = directoryWebService.getScores(DBN)
        scoresResult.shouldBeInstanceOf<LocalResult.Error>()
        scoresResult.throwable shouldNotBe null
    }

    companion object {
        private val testRetrofit: Retrofit
            get() {
                return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(NetworkModule.getGson()))
                    .build()
            }
        private val schoolApi: SchoolApi = testRetrofit.create(SchoolApi::class.java)
        private const val DBN = "25Q525"
        private val successSchoolEndpoints = SchoolEndpoints()
        private val errorSchoolEndpoints = object : SchoolEndpoints() {
            override val schools: String
                get() = ""

            override val scores: String
                get() = ""
        }
    }
}