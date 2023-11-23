package uz.ilhomjon.kirakashgo.presentation.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GestureDetectorCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentHomeBinding
import uz.ilhomjon.kirakashgo.presentation.common.MyGestureListener
import uz.ilhomjon.kirakashgo.taximetr.MyLocationService

class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var gestureDetector: GestureDetectorCompat
    private var initialX = 0f // Initial X position of the ImageView
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
        binding.aboutBtn.setOnClickListener {
            findNavController().navigate(R.id.driverFragment, bundleOf("1" to 1), navOption.build())
        }

        locationService()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()

        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        binding.cashTv.startAnimation(animation)
        binding.aboutBtn.startAnimation(animation)


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

    fun locationService(){
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE){
            //all permissions already granted or just granted
            ContextCompat.startForegroundService(requireContext(), Intent(requireActivity(), MyLocationService::class.java))
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(requireContext())
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }
}