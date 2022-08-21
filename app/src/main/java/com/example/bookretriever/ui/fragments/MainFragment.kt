package com.example.bookretriever.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookretriever.R
import com.example.bookretriever.adapters.BookAdapter
import com.example.bookretriever.databinding.FragmentMainBinding
import com.example.bookretriever.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var bookAdapter: BookAdapter
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        bookAdapter = BookAdapter()
//        bookAdapter.setOnClickListener(BookAdapter.OnItemClickListener {
//
//            //open detailed activity
//        })

        binding.recyclerviewBooks.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bookAdapter
        }

        lifecycleScope.launch {
            viewModel.uiBookList.onEach { bookAdapter.submitList(it) }.collect()
        }
    }
}