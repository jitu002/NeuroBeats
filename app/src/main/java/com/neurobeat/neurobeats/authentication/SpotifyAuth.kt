package com.neurobeat.neurobeats.authentication

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object SpotifyAuth {
    private const val CLIENT_ID = ""
    private const val CLIENT_SECRET = ""
    private const val TOKEN_URL = "https://accounts.spotify.com/api/token"

    fun getAccessToken(callback: (String?) -> Unit) {
        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .build()

        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(requestBody)
            .header("Authorization", Credentials.basic(CLIENT_ID, CLIENT_SECRET))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody!!)
                    val accessToken = json.getString("access_token")
                    callback(accessToken)
                } else {
                    callback(null)
                }
            }
        })
    }
}
