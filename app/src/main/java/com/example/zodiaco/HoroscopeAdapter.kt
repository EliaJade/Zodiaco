package com.example.zodiaco

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class HoroscopeAdapter(var items: List<Horoscope>, val onClick: (Int) -> Unit): Adapter<HoroscopeViewHolder>() {

    var highlightText: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)
            return HoroscopeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = items[position]
        holder.render(horoscope)

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    fun updateData (newItems: List<Horoscope>) {
        updateData(newItems, null)
    }

    fun updateData (newItems: List<Horoscope>, highlight: String?) {
        this.highlightText = highlight
        items = newItems
        notifyDataSetChanged()
    }
}

class HoroscopeViewHolder(view: View) : ViewHolder (view) {
    val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    val favoriteImageView: ImageView = view.findViewById(R.id.favoriteIcon)
    val descTextView: TextView = view.findViewById(R.id.descTextView)

        fun render (horoscope: Horoscope) {
            iconImageView.setImageResource(horoscope.icon)
            nameTextView.setText(horoscope.name)
            dateTextView.setText(horoscope.dates)
            //descTextView.setText(horoscope.description)

            if (SessionManager(itemView.context).isFavorite(horoscope.id)) {
                favoriteImageView.visibility = View.VISIBLE
        } else {
                favoriteImageView.visibility = View.GONE
        }
    }
}