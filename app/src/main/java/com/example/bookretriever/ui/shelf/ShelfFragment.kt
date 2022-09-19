package com.example.bookretriever.ui.shelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookretriever.R
import com.example.bookretriever.adapters.ShelfAdapter
import com.example.bookretriever.databinding.FragmentShelveBinding
import com.example.bookretriever.utils.ExtensionFunctions.invisible
import com.example.bookretriever.utils.ExtensionFunctions.setActionBarText
import com.example.bookretriever.utils.ExtensionFunctions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ShelfFragment : Fragment() {
    private val viewModel: ShelfViewModel by viewModels()
    private val bookAdapter = ShelfAdapter()
    private lateinit var binding: FragmentShelveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shelve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentShelveBinding.bind(view)
        activity?.setActionBarText("Favourite books")

        binding.recyclerviewShelf.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiBookList.onEach {
                with(binding.noBooksTextview) {
                    if (it.isEmpty()) visible()
                    else invisible()
                }
                bookAdapter.submitList(it)
            }.collect()
        }
    }
}