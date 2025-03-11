package com.example.zodiaco

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }

    private var currentHoroscopeIndex:Int = -1

    lateinit var nameTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var iconImageView: ImageView
    lateinit var horoscopeLuckTextView: TextView

    lateinit var horoscope: Horoscope

    lateinit var progress : ProgressBar
    lateinit var prevButton: Button
    lateinit var nextButton: Button

    var isFavorite = false
    lateinit var favoriteMenu: MenuItem

    lateinit var session: SessionManager



    //lateinit var bottomNavigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)
        val id = intent.getStringExtra(EXTRA_HOROSCOPE_ID)!!

        horoscope = Horoscope.findById(id)

        initView()

        loadData()
    }

        private fun loadData() {
            supportActionBar?.setTitle(horoscope.name)
            supportActionBar?.setSubtitle(horoscope.dates)

            nameTextView.text= getString(horoscope.name)
            nameTextView.setText(horoscope.name)
            datesTextView.setText(horoscope.dates)
            iconImageView.setImageResource(horoscope.icon)


            isFavorite = session.isFavorite(horoscope.id)

            getHoroscopeLuck("daily")
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)

        menu.findItem(R.id.action_favorite)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when (item.itemId) {
            R.id.action_favorite -> {
                isFavorite = !isFavorite
                if (isFavorite) {
                    session.setFavorite(horoscope.id)

                } else {
                session.setFavorite("")
                }
                setFavoriteIcon()
                //println("Menu favorite")
                true

            }

            R.id.action_share -> {
                val sendIntent = Intent()
                sendIntent.setAction(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                sendIntent.setType("text/plain")

                val shareIntent = Intent.createChooser(sendIntent, "How I would like to share")
                startActivity(shareIntent)
                //println("Menu share")
                true

            }
            else -> super.onOptionsItemSelected(item)
        }

        //return super.onOptionsItemSelected(item)


    }

        private fun initView () {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameTextView = findViewById(R.id.nameTextView)
        datesTextView = findViewById(R.id.datesTextView)
        iconImageView = findViewById(R.id.iconImageView)
        horoscopeLuckTextView = findViewById((R.id.horoscopeLuckTextView)

        progress = findViewById(R.id.progress)
        prevButton = findViewById(R.id.menu_prev)
        nextButton = findViewById(R.id.menu_next)

        prevButton.setOnClickListener {
            if(currentHoroscopeIndex == 0) {
                currentHoroscopeIndex = HoroscopeProvider().getHoroscopes().size
            }
        currentHoroscopeIndex--
        loadData()
        }

        session = session.isFavorite(horoscope.id)

        bottomNavigation = findViewById(R.id.)
        //horoscope.id == session.getFavorite()

        }

        private fun setFavoriteIcon () {
            if (isFavorite) {
                favoriteMenu.setIcon(R.drawable.favourite)

            } else {
               favoriteMenu.setIcon(R.drawable.favorite_unselected)
            }
        }

    private fun getHoroscopeLuck (period: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var urlConnection: HttpsURLConnection? = null

            try {
                val url = URL("https://horoscoope-app-api.vercel.app/api/v1/get-horoscope/$period?sign=${horoscope.id}&day=TODAY")
            val con = url.openConnection() as HttpsURLConnection
            con.requestMethod = "GET"
            val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferedReader.readLine().also { inputLine = it } ! = null) {
                    response.append(inputLine)
                }
                bufferedReader.close()
            }


            }
        }
    }

    }
