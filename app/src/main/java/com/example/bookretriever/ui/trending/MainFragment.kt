package com.example.bookretriever.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookretriever.R
import com.example.bookretriever.adapters.BookPagingAdapter
import com.example.bookretriever.adapters.LoadStateAdapter
import com.example.bookretriever.databinding.FragmentMainBinding
import com.example.bookretriever.repositories.BooksRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//todo notify item changed

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private val pagingAdapter = BookPagingAdapter()
    private var concatAdapter = pagingAdapter.withLoadStateFooter(
        LoadStateAdapter { pagingAdapter.retry() }
    )
    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var repository: BooksRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initRecycler()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.querySubmitted(query ?: return false)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.bookFlow.collect {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun initRecycler() {
        binding.recyclerviewBooks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = concatAdapter
        }
    }
}