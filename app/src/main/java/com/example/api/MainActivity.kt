package com.example.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.apicallvolley.R
import com.example.apicallvolley.databinding.ActivityMainBinding
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnQuote.setOnClickListener {
            getQuote(binding.textView)
        }
    }

    fun getQuote(view: View) { val thread = Thread {
        val url = URL("https://ron-swanson-quotes.herokuapp.com/v2/quotes")

        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.requestMethod = "GET"
        connection.connect()

        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val scanner = Scanner(connection.inputStream)
            scanner.useDelimiter("\\A")
            val jsonData = if (scanner.hasNext()) scanner.next() else ""
            Log.d("jsonData", jsonData)
            val jsonArray = JSONArray(jsonData)
            val quote = jsonArray[1].toString()
            updateTextBoxFromThread(quote)
        }
    }
        thread.start()
    }

    private fun parseJSON (jsonData: String?): String{
        val jsonArray = JSONArray(jsonData)
        val quote = jsonArray[0].toString()
        return quote
    }
    private fun updateTextBoxFromThread (text: String){
        runOnUiThread{
            binding.textView.text = text
        }
    }
}