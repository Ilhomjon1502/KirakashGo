package uz.ilhomjon.kirakashgo.presentation.screens.home.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.RvOrderBinding
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse

class OrderHomeRvAdapter(val rvAction: RvAction, var list: ArrayList<OrdersSocketResponse> = ArrayList()) :
    RecyclerView.Adapter<OrderHomeRvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: RvOrderBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(ordersSocketResponse: OrdersSocketResponse) {

            itemRvBinding.btnAccept.setOnClickListener {
                rvAction.acceptOrder(ordersSocketResponse)
            }

            itemRvBinding.tvAddressFrom.text = ordersSocketResponse.name_startin_place
            itemRvBinding.tvAddressTo.text = ordersSocketResponse.destination_name
            itemRvBinding.tvDescription.text = ordersSocketResponse.date
            if (ordersSocketResponse.baggage) {
                itemRvBinding.luggageBtn.setBackgroundResource(R.color.blue)
                itemRvBinding.luggageBtn.setTextColor(Color.WHITE)
            }
            if (ordersSocketResponse.is_comfort) {
                itemRvBinding.comfortBtn.setBackgroundResource(R.color.blue)
                itemRvBinding.comfortBtn.setTextColor(Color.WHITE)
            }
            if (ordersSocketResponse.for_women) {
                itemRvBinding.ayolBtn.setBackgroundResource(R.color.blue)
                itemRvBinding.ayolBtn.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    interface RvAction{
        fun acceptOrder(order:OrdersSocketResponse)
    }
}