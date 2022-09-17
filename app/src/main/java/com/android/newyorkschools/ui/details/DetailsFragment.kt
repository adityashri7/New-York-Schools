package com.android.newyorkschools.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.android.newyorkschools.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel

    private val safeArgs by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        viewModel.setSchool(safeArgs.school)


        viewModel.uiStateFlow.asLiveData().observe(viewLifecycleOwner) { nullableUiState ->
            nullableUiState?.let { uiState ->
                view.findViewById<TextView>(R.id.schoolTitleTextView).apply {
                    text = uiState.school.schoolName
                }

                when (uiState) {
                    is DetailsViewModel.UiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.errorLoadingSchoolDetailsMessage),
                            Toast.LENGTH_SHORT
                        ).show()
                        view.findViewById<LinearLayout>(R.id.scoresContainer).visibility = GONE
                        view.findViewById<TextView>(R.id.loadingScoresTextView).apply {
                        text = getString(R.string.noSatScoresFoundMessage)
                            visibility = VISIBLE
                        }
                    }
                    is DetailsViewModel.UiState.Loading -> {
                        view.findViewById<TextView>(R.id.loadingScoresTextView).visibility = VISIBLE
                    }
                    is DetailsViewModel.UiState.Idle -> {
                        view.findViewById<TextView>(R.id.loadingScoresTextView).visibility = GONE
                        view.findViewById<LinearLayout>(R.id.scoresContainer).visibility = VISIBLE
                        view.findViewById<TextView>(R.id.satMathScoreTextView).apply {
                            text = uiState.schoolScore.satMathAvgScore
                        }

                        view.findViewById<TextView>(R.id.satReadingScoreTextView).apply {
                            text = uiState.schoolScore.satCriticalReadingAvgScore
                        }

                        view.findViewById<TextView>(R.id.satWritingScoreTextView).apply {
                            text = uiState.schoolScore.satWritingAvgScore
                        }
                    }
                }
            }
        }
    }
}