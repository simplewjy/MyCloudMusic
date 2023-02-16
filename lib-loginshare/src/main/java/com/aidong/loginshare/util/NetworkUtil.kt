package com.aidong.loginshare.util

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object NetworkUtil {
    private const val TAG = "NetworkUtil"
    const val GET_TOKEN = 1
    const val CHECK_TOKEN = 2
    const val REFRESH_TOKEN = 3
    const val GET_INFO = 4
    const val GET_IMG = 5
    fun sendWxAPI(handler: Handler, url: String?, msgTag: Int) {
        val httpsThread = HttpsThread(handler, url, msgTag)
        httpsThread.start()
    }

    fun getImage(handler: Handler, url: String?, msgTag: Int) {
        val httpsThread = HttpsThread(handler, url, msgTag)
        httpsThread.start()
    }

    internal class HttpsThread(private val handler: Handler, private val httpsUrl: String?, private val msgTag: Int) : Thread() {
        override fun run() {
            if (msgTag == GET_IMG) {
                try {
                    val imgdata = httpURLConnectionGet(httpsUrl)
                    val msg = Message.obtain()
                    msg.what = msgTag
                    val data = Bundle()
                    data.putByteArray("imgdata", imgdata)
                    msg.data = data
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    Log.e(TAG, e.message!!)
                }
            } else {
                val resCode: Int
                val `in`: InputStream
                var httpResult: String? = null
                try {
                    val url = URL(httpsUrl)
                    val urlConnection = url.openConnection()
                    val httpsConn = urlConnection as HttpsURLConnection
                    httpsConn.allowUserInteraction = false
                    httpsConn.instanceFollowRedirects = true
                    httpsConn.requestMethod = "GET"
                    httpsConn.connect()
                    resCode = httpsConn.responseCode
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        `in` = httpsConn.inputStream
                        val reader = BufferedReader(
                            InputStreamReader(
                                `in`, "iso-8859-1"
                            ), 8
                        )
                        val sb = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            sb.append(line).append("\n")
                        }
                        `in`.close()
                        httpResult = sb.toString()
                        Log.i(TAG, httpResult)
                        val msg = Message.obtain()
                        msg.what = msgTag
                        val data = Bundle()
                        data.putString("result", httpResult)
                        msg.data = data
                        handler.sendMessage(msg)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message!!)
                }
            }
        }

        companion object {
            @Throws(Exception::class)
            private fun httpURLConnectionGet(url: String?): ByteArray? {
                val connection = URL(url).openConnection() as HttpURLConnection
                if (connection == null) {
                    Log.i(TAG, "open connection failed.")
                }
                val responseCode = connection.responseCode
                if (responseCode >= 300) {
                    connection.disconnect()
                    Log.w(TAG, "dz[httpURLConnectionGet 300]")
                    return null
                }
                val `is` = connection.inputStream
                val data = readStream(`is`)
                connection.disconnect()
                return data
            }

            @Throws(IOException::class)
            private fun readStream(inStream: InputStream): ByteArray {
                val buffer = ByteArray(1024)
                var len = -1
                val outStream = ByteArrayOutputStream()
                while (inStream.read(buffer).also { len = it } != -1) {
                    outStream.write(buffer, 0, len)
                }
                val data = outStream.toByteArray()
                outStream.close()
                inStream.close()
                return data
            }
        }
    }
}