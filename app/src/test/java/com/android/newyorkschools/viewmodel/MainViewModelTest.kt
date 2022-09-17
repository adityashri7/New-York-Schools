package com.android.newyorkschools.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.newyorkschools.BaseCoroutineTest
import com.android.newyorkschools.model.School
import com.android.newyorkschools.repository.SchoolRepository
import com.android.newyorkschools.ui.main.MainViewModel
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
class MainViewModelTest : BaseCoroutineTest() {
    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel

    @MockK
    private lateinit var schoolRepository: SchoolRepository

    @Before
    fun before() {
        MockKAnnotations.init(this)
        coEvery { schoolRepository.getSchools() } returns LocalResult.Success(SCHOOLS)
        mainViewModel = MainViewModel(testDispatcherProvider, schoolRepository)
    }

    @After
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `MainViewModel should return list from success response`() = runTest {
        coEvery { schoolRepository.getSchools() } returns LocalResult.Success(SCHOOLS)
        mainViewModel.uiStateFlow shouldNotBe null
        mainViewModel.uiStateFlow.value.schools.shouldBe(SCHOOLS)
    }

    @Test
    fun `MainViewModel should return Error from error response`() = runTest {
        val throwable = Throwable("Test Error")
        coEvery { schoolRepository.getSchools() } returns LocalResult.Error(throwable)
        mainViewModel.loadSchools()
        mainViewModel.uiStateFlow shouldNotBe null
        mainViewModel.uiStateFlow.value.shouldBeInstanceOf<MainViewModel.UiState.Error>()
        (mainViewModel.uiStateFlow.value as MainViewModel.UiState.Error).error shouldBe throwable
        mainViewModel.uiStateFlow.value.schools.shouldBe(SCHOOLS)
    }

    private companion object {
        private val SCHOOLS = (0..30).map {
            School(dbn = "DBN-$it")
        }
    }
}