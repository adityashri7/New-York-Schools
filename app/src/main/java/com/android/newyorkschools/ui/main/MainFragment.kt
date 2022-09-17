package com.android.newyorkschools.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.newyorkschools.R
import com.android.newyorkschools.model.School
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
            .apply { setOnRefreshListener { viewModel.loadSchools() } }
        val schoolAdapter = SchoolAdapter {
            findNavController(this).navigate(MainFragmentDirections.navigateToDetails(school = it))
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = schoolAdapter
        }
        viewModel.uiStateFlow.asLiveData().observe(viewLifecycleOwner) { nullableUiState ->
            nullableUiState?.let { uiState ->
                swipeRefreshLayout.isRefreshing = uiState is MainViewModel.UiState.Loading
                schoolAdapter.submitList(uiState.schools)
                when (uiState) {
                    is MainViewModel.UiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.errorLoadingSchoolsMessage),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is MainViewModel.UiState.Idle,
                    is MainViewModel.UiState.Loading -> {

                    }
                }
            }
        }
    }

    private class SchoolAdapter(val schoolClicked: (school: School) -> Unit) :
        ListAdapter<School, SchoolAdapter.SchoolViewHolder>(DIFF_CALLBACK) {
        @SuppressLint("InflateParams")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
            val schoolView =
                LayoutInflater.from(parent.context).inflate(R.layout.school_item, null)
            return SchoolViewHolder(schoolView)
        }

        override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                schoolClicked(getItem(position))
            }
            holder.bind(getItem(position))
        }

        private class SchoolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(school: School) {
                itemView.findViewById<TextView>(R.id.nameTextView)?.apply {
                    text = school.schoolName
                }
                itemView.findViewById<TextView>(R.id.descriptionTextView)?.apply {
                    text = school.schoolEmail
                }
                itemView.findViewById<TextView>(R.id.addressTextView)?.apply {
                    text = school.primaryAddressLine1
                }
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<School>() {
            override fun areItemsTheSame(oldItem: School, newItem: School): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: School, newItem: School): Boolean {
                return oldItem == newItem
            }

        }
    }
}