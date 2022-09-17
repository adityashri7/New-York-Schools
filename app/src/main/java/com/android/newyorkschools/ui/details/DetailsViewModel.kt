package com.android.newyorkschools.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.newyorkschools.model.School
import com.android.newyorkschools.model.SchoolScore
import com.android.newyorkschools.repository.SchoolRepository
import com.android.newyorkschools.util.LocalDispatchers
import com.android.newyorkschools.util.LocalResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val dispatchers: LocalDispatchers,
    private val schoolRepository: SchoolRepository,
) : ViewModel() {


    private val mutableUiStateFlow: MutableSharedFlow<UiState> = MutableSharedFlow(0)
    val uiStateFlow: Flow<UiState> = mutableUiStateFlow

    fun setSchool(school: School) {
        viewModelScope.launch(CoroutineName("setSchool") + dispatchers.default) {
            mutableUiStateFlow.emit(UiState.Loading(school))
        }
        viewModelScope.launch(CoroutineName("loadSchoolScores") + dispatchers.default) {
            mutableUiStateFlow.emit(UiState.Loading(school))
            when (val schoolScoresResponse = schoolRepository.getSchoolScores(school.dbn)) {
                is LocalResult.Error -> {
                    mutableUiStateFlow.emit(UiState.Error(school, schoolScoresResponse.throwable))
                }
                is LocalResult.Success -> {
                    mutableUiStateFlow.emit(UiState.Idle(school, schoolScoresResponse.data))
                }
            }
        }
    }

    sealed class UiState(open val school: School, open val schoolScore: SchoolScore?) {
        data class Loading(override val school: School) : UiState(school, null)
        data class Idle(override val school: School, override val schoolScore: SchoolScore) :
            UiState(school, schoolScore)

        data class Error(override val school: School, val error: Throwable?) :
            UiState(school, null)
    }


}