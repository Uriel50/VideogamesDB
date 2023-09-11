package com.camu.videogamesdb.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.camu.videogamesdb.R
import com.camu.videogamesdb.databinding.SpinnerItemLayoutBinding
import com.camu.videogamesdb.util.GenreItem

class GenreAdapter(private val context: Context, private val items: List<GenreItem>) :
    ArrayAdapter<GenreItem>(context, R.layout.spinner_item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: SpinnerItemLayoutBinding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            val itemBinding = SpinnerItemLayoutBinding.inflate(inflater, parent, false)
            itemBinding.root.tag = itemBinding
            itemBinding
        } else {
            convertView.tag as SpinnerItemLayoutBinding
        }

        val item = items[position]
        binding.textView.text = item.text
        binding.imageView.setImageResource(item.imageResId)

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
