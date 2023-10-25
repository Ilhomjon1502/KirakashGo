package uz.ilhomjon.kirakashgo.presentation.screens.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import uz.ilhomjon.kirakashgo.R
import uz.ilhomjon.kirakashgo.databinding.FragmentDriverBinding

class DriverFragment : Fragment() {

    private val binding by lazy { FragmentDriverBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val animation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)
        binding.driverPhoto.startAnimation(animation)
    }
}