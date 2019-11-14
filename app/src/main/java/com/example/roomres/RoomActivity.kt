package com.example.roomres

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast


import com.example.roomres.MODELS.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_reservationroom.*
import kotlinx.android.synthetic.main.activity_room.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.NavUtils

class RoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        setSupportActionBar(toolRoom as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)

        getDataUsingOkHttpEnqueue()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.frontpage_item -> {
                val intentfront = Intent(this, FrontPageActivity::class.java)
                startActivity(intentfront)
                return true // true: menu processing done, no further actions
            }
            R.id.login_item -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true // true: menu processing done, no further actions
            }
            R.id.room_item -> {
                val intentRoom = Intent(this, RoomActivity::class.java)
                startActivity(intentRoom)
                return true // true: menu processing done, no further actions
            }
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true // true: menu processing done, no further actions
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getDataUsingOkHttpEnqueue() {
        val client = OkHttpClient()
        val requestBuilder = Request.Builder()
        requestBuilder.url(URI)
        val request = requestBuilder.build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonString = response.body!!.string()
                    runOnUiThread { populateList(jsonString) }
                } else {
                    runOnUiThread {
                        val messageView = main_message_textview
                        messageView.text = URI + "\n" + response.code + " " + response.message
                    }
                }
            }

            override fun onFailure(call: Call, ex: IOException) {
                runOnUiThread {
                    val messageView = main_message_textview
                    messageView.text = ex.message
                }
            }
        })
    }

    private fun populateList(jsonString: String) {
        val gson = GsonBuilder().create()
        Log.d("ROA", jsonString)
        val rooms = gson.fromJson(jsonString, Array<Room>::class.java)
        val listView = rooms_Listview
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, rooms)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this@RoomActivity, "Room clicked: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT)
                    .show()
            val intent = Intent(baseContext, ReservationRoomActivity::class.java)
            val room = parent.getItemAtPosition(position) as Room
            intent.putExtra(ReservationRoomActivity.ROOM, room)
            startActivity(intent)
        }
    }

    companion object {
        val URI = "http://anbo-roomreservationv3.azurewebsites.net/api/rooms"
    }


}

