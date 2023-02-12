package com.zrq.videodemo.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpUtil {
    private const val TAG = "HttpUtil"

    private val mCookies = mutableListOf<Cookie>()
    private val mmkv = MMKV.defaultMMKV()

    data class Cookies(
        var cookies: List<Cookie>
    )

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookies = mmkv.getString("cookies", "")
                    val list = mutableListOf<Cookie>()
                    val fromJson = Gson().fromJson(cookies, Cookies::class.java)
                    fromJson?.cookies?.let {
                        return fromJson.cookies
                    }
                    return mutableListOf()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    val jsonStr = Gson().toJson(Cookies(cookies), object : TypeToken<Cookies>() {}.type)
                    mmkv.putString("cookies", jsonStr)

                    mCookies.clear()
                    mCookies.addAll(cookies)
                }
            })
            .readTimeout(20000L, TimeUnit.MILLISECONDS)
            .connectTimeout(20000L, TimeUnit.MILLISECONDS)
            .build()
    }

    fun httpGet(url: String, callback: (Boolean, String) -> Unit) {
        Log.d(TAG, "load: $url")
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "服务器挂了")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.body != null) {
                    callback(true, response.body!!.string())
                } else {
                    callback(false, "服务器出问题了，请稍后刷新重试")
                }
            }
        })
    }

    fun httpGetOnUi(url: String, callback: (Boolean, String) -> Unit) {
        Log.d(TAG, "load: $url")
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "服务器挂了")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.body != null) {
                    Handler(Looper.getMainLooper()).post {
                        callback(true, response.body!!.string())
                    }
                } else {
                    callback(false, "服务器出问题了，请稍后刷新重试")
                }
            }
        })
    }

}