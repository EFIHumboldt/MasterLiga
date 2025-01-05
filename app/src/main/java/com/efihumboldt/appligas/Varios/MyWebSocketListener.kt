package com.efihumboldt.appligas.Varios

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

interface MyWebSocketListener {
    fun onOpen(webSocket: WebSocket, response: Response)
    fun onMessage(webSocket: WebSocket, text: String)
    fun onMessage(webSocket: WebSocket, bytes: ByteString)
    fun onClosing(webSocket: WebSocket, code: Int, reason: String)
    fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?)
}