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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.databinding.FragmentOrderActionBinding
import uz.ilhomjon.kirakashgo.presentation.common.MyGestureListener
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.Status

@AndroidEntryPoint
class OrderActionFragment : Fragment() {

    private val binding by lazy { FragmentOrderActionBinding.inflate(layoutInflater) }
    private lateinit var gestureDetector: GestureDetectorCompat
    private var initialX = 0f // Initial X position of the ImageView
    private val viewModel: DriverProfileViewModel by viewModels()
    var order = OrdersSocketResponse.order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding.cashTv.text = order.total_sum.toString()
        binding.waitingTime.text = "00:00"
        binding.homeServiceAddress.text = order.name_startin_place
        binding.tvAddressTo.text = order.destination_name
        binding.tvDescription.text = order.description
        binding.comfortBtn.isEnabled = order.is_comfort
        binding.luggageBtn.isEnabled = order.baggage
        binding.phoneNumber.text = order.client_phone
        binding.phoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("tel:${order.client_phone}")
            startActivity(intent)
        }
        binding.cancelBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(binding.root.context)
            dialog.setTitle("Ogohlantirish")
            dialog.setMessage("Rostdan ham ${order.id} - buyurtmani bekor qilmoqchimisiz?\n" +
                    "Agar bekor qilsangiz uni boshqa haydovchilar olib ketishi mumkin")
            dialog.setPositiveButton("Ha",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    cancelOrder(order)
                }
            })
            dialog.setNegativeButton("Yo'q", object : DialogInterface.OnClickListener{
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
            true
        }
    }

    private fun pxToDp(px: Int): Float {
        val density = resources.displayMetrics.density
        return px / density
    }

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
                        }

                        else -> Toast.makeText(context, "Biz bilmagan xatolik", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        }
    }

    private fun startOrder(order: OrdersSocketResponse){

    }

}