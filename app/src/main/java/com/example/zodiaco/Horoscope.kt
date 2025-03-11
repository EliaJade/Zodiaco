package com.example.zodiaco

class Horoscope (
    val id: String,
    val name: Int,
    val dates: Int,
    val icon: Int,
    //val description: Int
) {
companion object {
    val horoscopeList = listOf(
        Horoscope("aries", R.string.horoscope_name_aries, R.string.horoscope_dates_aries, R.drawable.aries_svgrepo_com),
        Horoscope("taurus", R.string.horoscope_name_taurus, R.string.horoscope_dates_taurus, R.drawable.taurus_svgrepo_com),
        Horoscope("gemini", R.string.horoscope_name_gemini, R.string.horoscope_dates_gemini, R.drawable.gemini_svgrepo_com),
        Horoscope("cancer", R.string.horoscope_name_cancer, R.string.horoscope_dates_cancer, R.drawable.cancer_svgrepo_com),
        Horoscope("leo", R.string.horoscope_name_leo, R.string.horoscope_dates_leo, R.drawable.leo_svgrepo_com),
        Horoscope("virgo", R.string.horoscope_name_virgo, R.string.horoscope_dates_virgo, R.drawable.virgo_svgrepo_com),
        Horoscope("libra", R.string.horoscope_name_libra, R.string.horoscope_dates_libra, R.drawable.libra_svgrepo_com),
        Horoscope("scorpio", R.string.horoscope_name_scorpio, R.string.horoscope_dates_taurus, R.drawable.taurus_svgrepo_com),
        Horoscope("sagittarius", R.string.horoscope_name_sagittarius, R.string.horoscope_dates_sagittarius, R.drawable.sagittarius_svgrepo_com),
        Horoscope("capricorn", R.string.horoscope_name_capricorn, R.string.horoscope_dates_capricorn, R.drawable.capricorn_svgrepo_com),
        Horoscope("aquarius", R.string.horoscope_name_aquarius, R.string.horoscope_dates_aquarius, R.drawable.aquarius_svgrepo_com),
        Horoscope("pisces", R.string.horoscope_name_pisces, R.string.horoscope_dates_pisces, R.drawable.pisces_svgrepo_com),
    )

    fun findById(id:String): Horoscope {
        return horoscopeList.first { it.id == id }
    }
}

}