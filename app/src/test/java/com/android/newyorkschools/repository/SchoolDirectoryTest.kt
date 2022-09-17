package com.android.newyorkschools.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.newyorkschools.BaseCoroutineTest
import com.android.newyorkschools.cloud.api.SchoolEndpoints
import com.android.newyorkschools.cloud.service.SchoolWebService
import com.android.newyorkschools.model.School
import com.android.newyorkschools.util.LocalResult
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SchoolDirectoryTest : BaseCoroutineTest() {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var schoolWebService: SchoolWebService

    private lateinit var schoolRepository: SchoolRepository

    @Before
    fun before() {
        MockKAnnotations.init(this)
        coEvery { schoolWebService.getSchools() } returns LocalResult.Success(SCHOOLS)
        schoolRepository = SchoolRepository(testDispatcherProvider, schoolWebService)
    }

    @After
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `SchoolRepository should return list from success response`() = runTest {
        coEvery { schoolRepository.getSchools() } returns LocalResult.Success(SCHOOLS)
        schoolRepository.getSchools() shouldNotBe null
        schoolRepository.getSchools().shouldBeInstanceOf<LocalResult.Success<List<School>>>()
    }

    @Test
    fun `SchoolRepository should return error from error response`() = runTest {
        val throwable = Throwable("Test Error")
        coEvery { schoolRepository.getSchools() } returns LocalResult.Error(throwable)
        schoolRepository.getSchools() shouldNotBe null
        schoolRepository.getSchools().shouldBeInstanceOf<LocalResult.Error>()
        (schoolRepository.getSchools() as LocalResult.Error).throwable shouldBe throwable
    }

    private companion object {
        private val SCHOOLS = (0..30).map {
            School(dbn = "DBN-$it")
        }
        private val schoolEndpoints = object : SchoolEndpoints() {
            override val schools = ""
            override val scores = ""
        }
    }
}