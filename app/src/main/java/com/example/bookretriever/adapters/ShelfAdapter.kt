package com.example.bookretriever.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookretriever.databinding.ShelfItemBinding
import com.example.bookretriever.models.Book

class ShelfAdapter : ListAdapter<Book, ShelfAdapter.BookViewHolder>(BookItemDiffCallBack()) {

    class BookViewHolder(val binding: ShelfItemBinding) : RecyclerView.ViewHolder(binding.root)

    private class BookItemDiffCallBack : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
            oldItem.coverI == newItem.coverI

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ShelfItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            bookTitle.text = item.title
            bookAuthor.text = item.author

            Constant.getGlide(bookItemContainer, item.coverUrl).into(bookImage)
        }
    }
}