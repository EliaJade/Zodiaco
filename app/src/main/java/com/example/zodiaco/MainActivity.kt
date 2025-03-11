package com.example.zodiaco

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

        var  horoscopeList = Horoscope.horoscopeList

        lateinit var adapter: HoroscopeAdapter

        lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        override fun onResume() {
            super.onResume()

             adapter = HoroscopeAdapter(horoscopeList) { position ->
                val horoscope = horoscopeList[position]

                // Toast.makeText(this, horoscope.name, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_HOROSCOPE_ID, horoscope.id)
                startActivity(intent)

        }


        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Log.i("MENU", "I pressed Enter")
                return false
            }
            override fun onQueryTextChange(query: String): Boolean {
                horoscopeList = Horoscope.horoscopeList.filter {
                    getString(it.name).contains(query, true) ||
                    getString(it.dates).contains(query, true)
                }
                adapter.updateData(horoscopeList, query)
                }
                return true
            }
        })

        return true
    }
 fun navigateToDetail (horoscope: Horoscope) {
     val intent: Intent = Intent(this, DetailActivity::class.java)
     intent.putExtra(DetailActivity.EXTRA_HOROSCOPE_ID, horoscope.id)
     startActivity(intent)
}