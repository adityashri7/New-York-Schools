package com.android.newyorkschools.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newyorkschools.model.School
import com.android.newyorkschools.repository.SchoolRepository
import com.android.newyorkschools.util.LocalDispatchers
import com.android.newyorkschools.util.LocalResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatchers: LocalDispatchers,
    private val schoolRepository: SchoolRepository,
) : ViewModel() {

    private val mutableUiStateFlow: MutableStateFlow<UiState> = MutableStateFlow(
        UiState.Idle(
            emptyList()
        )
    )
    val uiStateFlow: StateFlow<UiState> = mutableUiStateFlow

    init {
        loadSchools()
    }

    fun loadSchools() {
        viewModelScope.launch(CoroutineName("loadSchools") + dispatchers.Default) {
            mutableUiStateFlow.value = UiState.Loading(mutableUiStateFlow.value.schools)
            when (val schoolsResult = schoolRepository.getSchools()) {
                is LocalResult.Error -> {
                    mutableUiStateFlow.value =
                        UiState.Error(mutableUiStateFlow.value.schools, schoolsResult.throwable)
                }
                is LocalResult.Success -> {
                    mutableUiStateFlow.value = UiState.Idle(schoolsResult.data)
                }
            }
        }
    }

    sealed class UiState(open val schools: List<School>) {
        data class Loading(override val schools: List<School>) : UiState(schools)
        data class Idle(override val schools: List<School>) : UiState(schools)
        data class Error(override val schools: List<School>, val error: Throwable?) :
            UiState(schools)
    }
}