package com.efihumboldt.appligas.Varios

import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

class WebSocketListenerImpl : MyWebSocketListener {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        // Manejar evento de apertura
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        // Manejar mensaje de texto
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        // Manejar mensaje de bytes
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        // Manejar evento de cierre
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        // Manejar fallo
    }
}
