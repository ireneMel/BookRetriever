package com.example.bookretriever.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bookretriever.R
import com.example.bookretriever.databinding.BookItemBinding
import com.example.bookretriever.models.Book

class BookAdapter : ListAdapter<Book, BookAdapter.BookViewHolder>(BookItemDiffCallBack()) {

    class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    // Define listener member variable
    private var listener: AdapterView.OnItemClickListener? = null

//    // Define the listener interface
//    interface OnItemClickListener {
//        fun onItemClick(itemView: View?, position: Int)
//    }

    private class BookItemDiffCallBack : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean =
            oldItem.openLibraryId == newItem.openLibraryId

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            bookAuthor.text = item.author
            bookTitle.text = item.title

            Glide.with(holder.binding.bookItemContainer.context)
                .load(item.coverUrl)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(holder.binding.bookImage)
        }
    }
}