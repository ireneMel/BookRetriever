package com.example.bookretriever.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookretriever.databinding.BookItemBinding
import com.example.bookretriever.models.Book

class BookAdapter(
//    private val listener: BookItemClickListener,
    private val bookList: List<Book>
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            BookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = bookList[position]

        holder.binding.apply {
            bookTitle.text = item.title
//            bookItemContainer.setOnClickListener {
//TODO - open detailed info about the book
//            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}