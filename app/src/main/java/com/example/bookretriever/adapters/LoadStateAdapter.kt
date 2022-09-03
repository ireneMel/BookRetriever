package com.example.bookretriever.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookretriever.R
import com.example.bookretriever.databinding.LoadItemBinding

class LoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder.create(parent, retry)
}

class LoadStateViewHolder(
    private val binding: LoadItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        with(binding) {
//            if (loadState is LoadState.Error) {
//                errorMsg.text = loadState.error.localizedMessage
//            }
//            errorMsg.isVisible = loadState is LoadState.Error
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder =
            LoadStateViewHolder(
                LoadItemBinding.bind(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.load_item, parent, false)
                ),
                retry
            )
    }
}