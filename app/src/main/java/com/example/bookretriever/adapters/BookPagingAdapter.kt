package com.example.bookretriever.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bookretriever.UIState
import com.example.bookretriever.databinding.BookItemBinding
import com.example.bookretriever.models.UIBook
import com.example.bookretriever.utils.Constants.getGlide

class BookPagingAdapter :
    PagingDataAdapter<UIBook, BookPagingAdapter.BookViewHolder>(BookItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        BookViewHolder(
            BookItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    private class BookItemDiffCallBack : DiffUtil.ItemCallback<UIBook>() {
        override fun areItemsTheSame(oldItem: UIBook, newItem: UIBook): Boolean =
            oldItem.coverI == newItem.coverI

        override fun areContentsTheSame(oldItem: UIBook, newItem: UIBook): Boolean =
            oldItem == newItem

    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.binding.apply {
            bookTitle.text = item.title
            bookAuthor.text = item.author

            favourite.setUIState(if (item.isLiked()) UIState.Like else UIState.Unlike, false)

//            detailedInfoButton.setOnClickListener {
//                item.onLongClick()
//            }

            bookItemContainer.setOnClickListener {
                Log.d("Book Adapter", "onBindViewHolder: clicked")
                item.onClick()
                favourite.setUIState(if (item.isLiked()) UIState.Like else UIState.Unlike, true)
            }

            getGlide(bookItemContainer, item.coverUrl).into(bookImage)
        }
    }

    //    override fun onBindViewHolder(
//        holder: BookViewHolder,
//        position: Int,
//        payloads: MutableList<Any>
//    ) {
//        if (payloads.isEmpty())
//            super.onBindViewHolder(holder, position, payloads)
//        else {
//
//        }
//    }


}