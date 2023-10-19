package uz.ilhomjon.kirakashgo.presentation.screens.orders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uz.ilhomjon.kirakashgo.databinding.RvDateItemBinding
import uz.ilhomjon.kirakashgo.databinding.RvOrderItemBinding
import uz.ilhomjon.kirakashgo.presentation.models.Order

class OrderAdapter(val list: ArrayList<Order>) : RecyclerView.Adapter<ViewHolder>() {

    inner class DateVh(val rvDateItem: RvDateItemBinding) : ViewHolder(rvDateItem.root) {
        fun onBind(date: String) {
            rvDateItem.dateTv.text = date
        }
    }

    inner class OrderVh(val rvOrderItem: RvOrderItemBinding) : ViewHolder(rvOrderItem.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(order: Order) {
            rvOrderItem.addressTv.text = order.address
            rvOrderItem.price.text = "Summa: ${order.price}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) DateVh(
            RvDateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ) else OrderVh(
            RvOrderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val dateVh = holder as DateVh
            dateVh.onBind(list[position].date ?: "")
        } else {
            val orderVh = holder as OrderVh
            orderVh.onBind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].date != null) 1 else 2

    }
}