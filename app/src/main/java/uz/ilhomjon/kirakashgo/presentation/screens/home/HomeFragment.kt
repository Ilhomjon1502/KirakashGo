package uz.ilhomjon.kirakashgo.presentation.screens.home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentHomeBinding
import uz.ilhomjon.kirakashgo.databinding.RvOrderBinding
import uz.ilhomjon.kirakashgo.presentation.common.MyGestureListener
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse
import uz.ilhomjon.kirakashgo.presentation.screens.home.adapters.OrderHomeRvAdapter
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status
import uz.ilhomjon.kirakashgo.taximetr.websocket.MyWebSocketClient
import java.net.URI
@AndroidEntryPoint
class HomeFragment : Fragment(), OrderHomeRvAdapter.RvAction {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel:DriverProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val navOption = NavOptions.Builder()
        navOption.setEnterAnim(R.anim.ochilish_1)
        navOption.setPopEnterAnim(R.anim.ochilish_2)
        navOption.setExitAnim(R.anim.yopilish_2)
        navOption.setPopExitAnim(R.anim.yopilish_1)

        binding.menuBtn.setOnClickListener {
            findNavController().navigate(R.id.menuFragment, bundleOf("1" to 1), navOption.build())
        }
//        binding.aboutBtn.setOnClickListener {
//            findNavController().navigate(R.id.driverFragment, bundleOf("1" to 1), navOption.build())
//        }

        connectWebSocket()
        return binding.root
    }



    fun connectWebSocket(){
        MySharedPreference.init(binding.root.context)
        val serverUri = URI("ws://147.182.206.31/ws/orders/?token=${MySharedPreference.token.access}") // Websocket server manzili
        val client = MyWebSocketClient(serverUri)

        val orderAdapter = OrderHomeRvAdapter(this)
        binding.rv.adapter = orderAdapter
        try {
            client.connect() // Websocketni ulash
            Toast.makeText(context, "Buyurtmalarni ko'rish boshlandi", Toast.LENGTH_SHORT).show()

            MyWebSocketClient.ordersLiveData.observe(viewLifecycleOwner){
                orderAdapter.list.clear()
                orderAdapter.list.addAll(it)
                orderAdapter.notifyDataSetChanged()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun acceptOrder(order: OrdersSocketResponse, itemRv: RvOrderBinding) {
        MySharedPreference.init(binding.root.context)
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.acceptOrder(MySharedPreference.token.access, order.id)
                .collectLatest {
                    when(it?.status){
                        Status.LOADING->{
                            binding.rv.isEnabled = false
                            itemRv.btnAccept.text = it.message
                            itemRv.progressBar.visibility = View.VISIBLE
                            Log.d("KeshOrder", "acceptOrder: ${it.data}")
                            MySharedPreference.oder=it.data!!
                        }
                        Status.ERROR->{
                            binding.rv.isEnabled = true
                            itemRv.btnAccept.text = "Qabul qilish"
                            itemRv.progressBar.visibility = View.INVISIBLE
                            val dialog = AlertDialog.Builder(binding.root.context)
                            dialog.setTitle("Xatolik")
                            dialog.setMessage(it.toString())
                            dialog.show()
                        }
                        Status.SUCCESS->{
                            binding.rv.isEnabled = true
                            itemRv.btnAccept.text = "Qabul qilish"
                            itemRv.progressBar.visibility = View.INVISIBLE
                            OrdersSocketResponse.order = order

//                            findNavController().popBackStack(R.id.homeFragment, true)
                            findNavController().navigate(R.id.orderActionFragment)
                        }
                        else-> Toast.makeText(context, "Xatolikni topa olmadik", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}

//40.36499326373501, 71.77504958272756