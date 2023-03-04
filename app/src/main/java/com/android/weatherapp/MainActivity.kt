package com.android.weatherapp

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.weatherapp.databinding.ActivityMainBinding
import com.android.weatherapp.local.Favorite
import com.android.weatherapp.local.RoomDB
import com.android.weatherapp.remote.RetrofiteInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val network  = RetrofiteInstance().apiCall()

        val room = RoomDB.invoke(this)
        lifecycleScope.launch(Dispatchers.Main) {
            val weatherResponse = async{ network.getWeatherDetails(30.61554342119405, 32.27797547385768) }

            if (weatherResponse.await().isSuccessful){
                val data = weatherResponse.await().body()
                // Come from retrofit
                Toast.makeText(this@MainActivity, data.toString(), Toast.LENGTH_SHORT).show()


                // inset in database
                data?.let {
                    room.favoriteDao().insertFavorite(Favorite(weather = data))
                }

                delay(3000)

                // retrieve from database
                val favoritesResponse = async{ room.favoriteDao().getFavorites() }
                Toast.makeText(this@MainActivity, "many of data in favorite table: ${favoritesResponse.await().size}", Toast.LENGTH_SHORT).show()
                delay(3000)


                // delete favorite from database
                room.favoriteDao().deleteFavorite(favorite = favoritesResponse.await().get(0))

            }
        }



    }
}