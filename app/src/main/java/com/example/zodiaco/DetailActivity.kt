package com.example.zodiaco

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOROSCOPE_ID = "HOROSCOPE_ID"
    }


    lateinit var nameTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var iconImageView: ImageView
    lateinit var horoscopeLuckTextView: TextView
    lateinit var progressIndicator: LinearProgressIndicator
    lateinit var navigationBar: BottomNavigationView
    lateinit var prevButton: Button
    lateinit var nextButton: Button

    var currentHoroscopeIndex: Int = -1

    lateinit var horoscope: Horoscope

    var isFavorite = false
    var favoriteMenu: MenuItem? = null

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
        val id: String = intent.getStringExtra(EXTRA_HOROSCOPE_ID)!!

        horoscope = Horoscope.findById(id)
        currentHoroscopeIndex = Horoscope.horoscopeList.indexOf(horoscope)




        initView()

        loadData()
    }
    private fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nameTextView = findViewById(R.id.nameTextView)
        datesTextView = findViewById(R.id.datesTextView)
        iconImageView = findViewById(R.id.iconImageView)
        horoscopeLuckTextView = findViewById(R.id.horoscopeLuckTextView)
        navigationBar = findViewById(R.id.navigationBar)
        progressIndicator = findViewById(R.id.progress)
        prevButton = findViewById(R.id.menu_prev)
        nextButton = findViewById(R.id.menu_next)


        prevButton.setOnClickListener {

            if (currentHoroscopeIndex == 0) {
                currentHoroscopeIndex = HoroscopeProvider().getHoroscope().size
            }
            currentHoroscopeIndex --
            loadData()

        }

        nextButton.setOnClickListener {
            currentHoroscopeIndex ++
            if (currentHoroscopeIndex == HoroscopeProvider().getHoroscope().size) {
                currentHoroscopeIndex = 0
            }
            loadData()
        }

    }

    private fun loadData() {
        horoscope = Horoscope.horoscopeList[currentHoroscopeIndex]

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)

        nameTextView.text = getString(horoscope.name)
        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        iconImageView.setImageResource(horoscope.icon)


        isFavorite = session.isFavorite(horoscope.id)
        setFavoriteIcon()

        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_daily -> {
                    getHoroscopeLuck("daily")
                }

                R.id.action_weekly -> {
                    getHoroscopeLuck("weekly")
                }

                R.id.action_monthly -> {
                    getHoroscopeLuck("monthly")
                }
            }
            return@setOnItemSelectedListener true
        }

        navigationBar.selectedItemId = R.id.action_daily

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)

        favoriteMenu = menu.findItem(R.id.action_favorite)

        setFavoriteIcon()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

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

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                //println("Menu share")
                true

            }

            else -> super.onOptionsItemSelected(item)
        }

        //return super.onOptionsItemSelected(item)


    }



    private fun setFavoriteIcon() {
        if (isFavorite) {
            favoriteMenu?.setIcon(R.drawable.favourite)

        } else {
            favoriteMenu?.setIcon(R.drawable.favorite_unselected)
        }
    }

    private fun getHoroscopeLuck(period: String) {
        progressIndicator.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            var urlConnection: HttpsURLConnection? = null

            try {
                val url =
                    URL("https://horoscope-app-api.vercel.app/api/v1/get-horoscope/$period?sign=${horoscope.id}&day=TODAY")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    val json = JSONObject(response.toString())
                    val result = json.getJSONObject("data").getString("horoscope_data")

                    runOnUiThread {
                        horoscopeLuckTextView.text = result
                        progressIndicator.visibility = View.GONE
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }
    }
}










