package com.example.zodiaco

class Horoscope (
    val id: String,
    val icon: Int,
    val name: Int,
    val dates: Int
) {
companion object {
    val horoscopeList = listOf(
        Horoscope("aries", R.string.horoscope_name_aries, R.string.horoscope_dates_aries, R.icon.horoscope)
    )

    fun findById(id:String): Horoscope {
        return horoscopeList.first { it.id == id }
    }
}

}