package com.example.bookretriever.ui.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookretriever.R
import com.example.bookretriever.adapters.BookPagingAdapter
import com.example.bookretriever.databinding.FragmentMainBinding
import com.example.bookretriever.repositories.BooksRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var pageAdapter: BookPagingAdapter
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
        pageAdapter = BookPagingAdapter()
        initRecycler()

//        bookAdapter.notifyItemChanged(2, "s")

        lifecycleScope.launchWhenStarted {
            viewModel.fetchPagedBooks().collect {
                pageAdapter.submitData(it)
            }
        }
    }

    private fun initRecycler() {
        binding.recyclerviewBooks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pageAdapter
        }
    }
}