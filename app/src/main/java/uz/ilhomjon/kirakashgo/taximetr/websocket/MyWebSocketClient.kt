package uz.ilhomjon.kirakashgo.taximetr.websocket

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse
import java.net.URI

class MyWebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {
    private val TAG = "MyWebSocketClient"

    override fun onOpen(handshakedata: ServerHandshake) {
        // Websocket ochilganda bajariladigan amallar
        Log.d(TAG, "onOpen: ")
    }

    companion object{
        val ordersLiveData = MutableLiveData<ArrayList<OrdersSocketResponse>>()
    }
    override fun onMessage(message: String) {
        // Yangi xabar keldi
        Log.d(TAG, "onMessage: $message")
        //
        val list = ArrayList<OrdersSocketResponse>()
        val type = object : TypeToken<ArrayList<OrdersSocketResponse>>(){}.type
        list.addAll(Gson().fromJson(message, type))
        ordersLiveData.postValue(list)
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        // Websocket yopilganda bajariladigan amallar
        Log.d(TAG, "onClose: ")
    }

    override fun onError(ex: Exception) {
        // Xatolik yuz berdi
        Log.d(TAG, "onError: ${ex.message}")
    }
}