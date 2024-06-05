package com.example.weedy.data.server

import kotlinx.coroutines.Dispatchers
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.DefaultHandler
import org.eclipse.jetty.server.handler.HandlerList
import android.content.Context
import android.util.Log
import kotlinx.coroutines.withContext
import org.eclipse.jetty.server.Request
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object Server {

    private val TAG = "Server"

    suspend fun hostServer(context: Context) {
        withContext(Dispatchers.IO) {
            val localServer = Server(8181)

            val assetHandler = context.assets.open("index.html").bufferedReader().use { it.readText() }

            val resourceHandler = object : DefaultHandler() {
                override fun handle(target: String?, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
                    response.writer.println(assetHandler)
                    response.writer.flush()
                    baseRequest.isHandled = true
                }
            }

            val handlerList = HandlerList()
            handlerList.setHandlers(arrayOf(resourceHandler))
            localServer.handler = handlerList

            try {
                localServer.start()
                Log.d(TAG, "Server started")
                localServer.join()
                Log.d(TAG, "Server joined")
            } catch (e: Exception) {
                Log.e(TAG, "Server failed", e)
            }
        }
    }
}