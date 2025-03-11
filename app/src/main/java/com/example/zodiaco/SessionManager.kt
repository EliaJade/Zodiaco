package com.example.zodiaco

import android.content.Context
import android.content.SharedPreferences


class SessionManager (context: Context){

    companion object {
        const val FAVORITE_HOROSCOPE = "FAVORITE_HOROSCOPE"
    }

        //private val sharedPref= context.getSharedPreferences("zodiac_session", Context.MODE_PRIVATE)
        private var sharedPref:SharedPreferences? = null

        var favoriteHoroscope:String?
            get() = getFavorite()
            set(value) = setFavorite(value!!)

        init {
            sharedPref = context.getSharedPreferences("my_session", Context.MODE_PRIVATE)
        }
        fun setFavorite(id: String) {
            val editor = sharedPref?.edit()
            if (editor != null) {
            editor.putString(FAVORITE_HOROSCOPE, id)
            editor.apply()
        }
        }

        fun getFavorite(): String {
            return sharedPref?.getString("FAVORITE_HOROSCOPE", "")!!

        }

        fun isFavorite(id: String): Boolean {
            return id == getFavorite()
        }
    }
