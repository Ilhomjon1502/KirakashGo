package uz.ilhomjon.kirakashgo.taximetr.websocket

import androidx.lifecycle.MutableLiveData
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse

object OrderObject {
    var liveList = MutableLiveData<ArrayList<OrdersSocketResponse>>()
}