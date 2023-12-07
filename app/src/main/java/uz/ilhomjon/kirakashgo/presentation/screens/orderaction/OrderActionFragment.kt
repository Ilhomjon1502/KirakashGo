@file:OptIn(DelicateCoroutinesApi::class)

package uz.ilhomjon.kirakashgo.presentation.screens.orderaction

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
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.databinding.FragmentOrderActionBinding
import uz.ilhomjon.kirakashgo.presentation.common.MyGestureListener
import uz.ilhomjon.kirakashgo.presentation.models.Order
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status
import uz.ilhomjon.kirakashgo.taximetr.MyFindLocation
import kotlin.coroutines.CoroutineContext

private const val TAG = "OrderActionFragment"

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OrderActionFragment : Fragment(), CoroutineScope {

    private val binding by lazy { FragmentOrderActionBinding.inflate(layoutInflater) }
    private lateinit var gestureDetector: GestureDetectorCompat
    private var initialX = 0f // Initial X position of the ImageView
    private val viewModel: DriverProfileViewModel by viewModels()
    lateinit var order: OrdersSocketResponse

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        MySharedPreference.init(binding.root.context)
        order = MySharedPreference.oder!!

        Log.d("startOrder", "onCreateView: $order")
        if (MySharedPreference.oder!!.order_status == "start") {
            binding.arrowRight.visibility = View.GONE
        }

        MyFindLocation.orderServiceLiveData.observe(viewLifecycleOwner) {
            order.total_sum = it.umumiyNarx
            order.destination_lat = it.lastLatLng?.latitude.toString()
            order.destination_long = it.lastLatLng?.longitude.toString()
            binding.cashTv.text = it.umumiyNarx.toString()
            val timeMinutes = "${(it.kutishVaqti / 60)} : ${(it.kutishVaqti % 60)}"
            binding.waitingTime.text = timeMinutes
        }

        binding.btnWaitSwitch.setOnClickListener {
            if (MyFindLocation.orderStatus == 1) {
                if (MyFindLocation.kutishmi) {
                    binding.btnWaitSwitch.text = "Kutishni boshlash"
                    MyFindLocation.kutishmi = false
                } else {
                    binding.btnWaitSwitch.text = "Kutishni to'xtatish"
                    MyFindLocation.kutishmi = true
                }
            } else {
                Toast.makeText(
                    context,
                    "Buyurtmani avval start qiling\n Klient oldiga yetib boring",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.homeServiceAddress.text = order.name_startin_place
        binding.tvAddressTo.text = order.destination_name
        binding.tvDescription.text = order.description
        if (order.is_comfort) {
            binding.comfortBtn.visibility = View.VISIBLE
        } else {
            binding.comfortBtn.visibility = View.INVISIBLE
        }
        if (order.baggage) {
            binding.luggageBtn.visibility = View.VISIBLE
        } else {
            binding.luggageBtn.visibility = View.INVISIBLE
        }
        binding.phoneNumber.text = order.client_phone
        binding.phoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("tel:${order.client_phone}")
            startActivity(intent)
        }
        binding.cancelBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context)
            dialog.setTitle("Ogohlantirish")
            dialog.setMessage(
                "Rostdan ham ${order.id} - buyurtmani bekor qilmoqchimisiz?\n" +
                        "Agar bekor qilsangiz uni boshqa haydovchilar olib ketishi mumkin"
            )
            dialog.setPositiveButton("Ha", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    cancelOrder(order)
                }
            })
            dialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            dialog.show()
        }


        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()

//        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
//        binding.cashTv.startAnimation(animation)
//        binding.aboutBtn.startAnimation(animation)


        binding.arriveCard.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val viewWidth = binding.arriveCard.measuredWidth
        val viewWidthDp = pxToDp(viewWidth)

        gestureDetector = GestureDetectorCompat(
            binding.root.context, MyGestureListener(binding.root.context, binding.arrowRight, 1000f)
        )

        Log.d("widthLength", "onResume: $viewWidthDp")
        binding.arrowRight.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            startOrder(order)
            true
        }


        MySharedPreference.init(binding.root.context)
        val token = MySharedPreference.token

        binding.comeText.setOnClickListener {
            finishOrder(order)
        }
    }

    private fun pxToDp(px: Int): Float {
        val density = resources.displayMetrics.density
        return px / density
    }

    @SuppressLint("SetTextI18n")
    private fun cancelOrder(order: OrdersSocketResponse) {
        GlobalScope.launch(Dispatchers.Main) {
            viewModel.cancelOrder(MySharedPreference.token.access, order.id)
                .collectLatest {
//                    val progressDialog = ProgressDialog(binding.root.context)
                    when (it?.status) {
                        Status.LOADING -> {
//                            progressDialog.setMessage(it.message)
//                            progressDialog.show()
                            binding.cancelBtn.text = it.message
                        }

                        Status.ERROR -> {
//                            progressDialog.hide()
                            binding.cancelBtn.text = "Bekor qilish"
                            val dialog = AlertDialog.Builder(binding.root.context)
                            dialog.setTitle("Xatolik")
                            dialog.setMessage(it.toString())
                            dialog.show()
                        }

                        Status.SUCCESS -> {
                            binding.cancelBtn.text = "Bekor qilish"
//                            progressDialog.cancel()
//                            findNavController().popBackStack(R.id.orderActionFragment, true)
                            findNavController().navigate(R.id.homeFragment)
                            MyFindLocation.orderStatus = 0
                            MyFindLocation.ordersSocketResponse = null
                        }

                        else -> Toast.makeText(context, "Biz bilmagan xatolik", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startOrder(order: OrdersSocketResponse) {

        GlobalScope.launch(Dispatchers.Main) {
            viewModel.startOrder("${MySharedPreference.token.access}", id = order.id)
                .collect {
//                    val progressDialog = ProgressDialog(binding.root.context)
                    val dialog = AlertDialog.Builder(binding.root.context)
                    when (it?.status) {
                        Status.LOADING -> {
//                            progressDialog.setMessage(it.message)
//                            progressDialog.show()

                        }

                        Status.ERROR -> {
//                            progressDialog.cancel()
//                            Log.d(TAG, "startOrder: ${it.data}")
//                            dialog.setTitle("Xatolik")
//                            dialog.setMessage(it.message)
//                            dialog.show()
                            Toast.makeText(context, "Error start \n$it", Toast.LENGTH_SHORT).show()
                        }

                        Status.SUCCESS -> {
                            MyFindLocation.orderStatus = 1
                            MyFindLocation.ordersSocketResponse = order
                            Log.d(TAG, "startOrder: $it")
                            order.order_status = "start"
                            MySharedPreference.oder = order
                            binding.comeText.isEnabled = true
                            binding.comeText.text = "Yakunlash"
                            Toast.makeText(context, "Start order,\n$it", Toast.LENGTH_SHORT).show()
                        }

                        else -> Toast.makeText(
                            context,
                            "Biz xatolikni topa olmadik",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    private fun finishOrder(order: OrdersSocketResponse) {
        launch(Dispatchers.Main) {
            viewModel.finishOrder(
                "${MySharedPreference.token.access}",
                order.id,
                order.destination_lat,
                order.destination_long,
                order.total_sum.toString()
            ).collectLatest {
                when (it?.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        Log.d(TAG, "finishOrder: $it")
                    }

                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d(TAG, "finishOrder: $it")
//                        val dialog = AlertDialog.Builder(binding.root.context)
//                        dialog.setTitle("Xatolik")
//                        dialog.setMessage(it.toString())
//                        dialog.show()
                    }

                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d(TAG, "finishOrder: $it")
                        Toast.makeText(
                            context,
                            "Finish order ${it.data?.success}",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                        val tempOrder: OrdersSocketResponse? = null
                        MySharedPreference.oder = tempOrder
                    }

                    null -> Log.d("FinishOrder", "finishOrder: Nomalum xatolik $it")
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()

}