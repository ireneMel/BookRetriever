package com.example.bookretriever.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookretriever.databinding.BookItemBinding
import com.example.bookretriever.models.UIBook

class BookAdapter : ListAdapter<UIBook, BookAdapter.BookViewHolder>(BookItemDiffCallBack()) {

    class BookViewHolder(val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root)

    private class BookItemDiffCallBack : DiffUtil.ItemCallback<UIBook>() {
        override fun areItemsTheSame(oldItem: UIBook, newItem: UIBook): Boolean =
            oldItem.coverI == newItem.coverI

        override fun areContentsTheSame(oldItem: UIBook, newItem: UIBook): Boolean =
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

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {

        }
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            bookTitle.text = item.title
            bookAuthor.text = item.author

            detailedInfoButton.setOnClickListener {
                item.onLongClick()
            }

            //TODO crop out the button for this listener
            bookItemContainer.setOnClickListener {
                Log.d("Book Adapter", "onBindViewHolder: clicked")
                item.onClick()
            }


//            Glide.with(bookItemContainer.context)
//                .load(item.coverUrl)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .placeholder(R.drawable.ic_launcher_foreground)

            Constant.getGlide(bookItemContainer, item.coverUrl).into(bookImage)
        }
    }

}